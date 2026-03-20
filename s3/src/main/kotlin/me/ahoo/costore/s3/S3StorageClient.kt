package me.ahoo.costore.s3

import me.ahoo.costore.core.StorageClient
import me.ahoo.costore.core.exception.ObjectNotFoundException
import me.ahoo.costore.core.exception.StorageException
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.HttpMethod
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.costore.core.model.StorageObject
import me.ahoo.costore.core.model.StorageObjectSummary
import me.ahoo.costore.core.model.UploadPolicy
import me.ahoo.costore.core.model.UploadToken
import software.amazon.awssdk.core.exception.SdkException
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest as S3GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.NoSuchKeyException
import software.amazon.awssdk.services.s3.model.PutObjectRequest as S3PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration
import java.util.UUID

/**
 * AWS S3 implementation of [StorageClient].
 *
 * Upload tokens are produced as pre-signed PUT URLs.  The URL is signed with the
 * requested expiry; when a content type or max content length is provided those
 * values are embedded in the signature so the server-side policy is enforced.
 *
 * Use [S3StorageClientFactory] or construct directly, passing configured SDK clients.
 *
 * @param s3Client    configured [S3Client]; ownership is transferred – calling [close]
 *                    on this instance will close it
 * @param s3Presigner configured [S3Presigner]; ownership is transferred
 */
class S3StorageClient(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
) : StorageClient {

    override fun putObject(request: PutObjectRequest): PutObjectResponse {
        return try {
            val s3Request = S3PutObjectRequest.builder()
                .bucket(request.bucket)
                .key(request.key)
                .apply {
                    request.contentType?.let { contentType(it) }
                    if (request.metadata.isNotEmpty()) metadata(request.metadata)
                }
                .build()
            val body = RequestBody.fromInputStream(request.inputStream, request.contentLength)
            val response = s3Client.putObject(s3Request, body)
            PutObjectResponse(eTag = response.eTag(), versionId = response.versionId())
        } catch (e: SdkException) {
            throw StorageException("Failed to put object '${request.key}' in bucket '${request.bucket}'", e)
        }
    }

    override fun getObject(request: GetObjectRequest): StorageObject {
        return try {
            val s3Request = S3GetObjectRequest.builder()
                .bucket(request.bucket)
                .key(request.key)
                .build()
            val response = s3Client.getObject(s3Request)
            val metadata = response.response()
            StorageObject(
                key = request.key,
                content = response,
                contentLength = metadata.contentLength() ?: 0L,
                contentType = metadata.contentType(),
                metadata = metadata.metadata() ?: emptyMap(),
                lastModified = metadata.lastModified() ?: java.time.Instant.EPOCH,
            )
        } catch (e: NoSuchKeyException) {
            throw ObjectNotFoundException(bucket = request.bucket, key = request.key, cause = e)
        } catch (e: SdkException) {
            throw StorageException("Failed to get object '${request.key}' from bucket '${request.bucket}'", e)
        }
    }

    override fun deleteObject(request: DeleteObjectRequest) {
        try {
            s3Client.deleteObject { b ->
                b.bucket(request.bucket)
                b.key(request.key)
            }
        } catch (e: SdkException) {
            throw StorageException(
                "Failed to delete object '${request.key}' from bucket '${request.bucket}'",
                e,
            )
        }
    }

    override fun listObjects(request: ListObjectsRequest): ListObjectsResponse {
        return try {
            val s3Request = ListObjectsV2Request.builder()
                .bucket(request.bucket)
                .maxKeys(request.maxKeys)
                .apply {
                    request.prefix?.let { prefix(it) }
                    request.delimiter?.let { delimiter(it) }
                    request.continuationToken?.let { continuationToken(it) }
                }
                .build()
            val response = s3Client.listObjectsV2(s3Request)
            ListObjectsResponse(
                objects = response.contents().map { obj ->
                    StorageObjectSummary(
                        key = obj.key(),
                        size = obj.size() ?: 0L,
                        lastModified = obj.lastModified() ?: java.time.Instant.EPOCH,
                        eTag = obj.eTag(),
                    )
                },
                isTruncated = response.isTruncated ?: false,
                nextContinuationToken = response.nextContinuationToken(),
            )
        } catch (e: SdkException) {
            throw StorageException("Failed to list objects in bucket '${request.bucket}'", e)
        }
    }

    /**
     * Generates a pre-signed PUT URL for the given [UploadPolicy].
     *
     * The object key is `{keyPrefix}{UUID}` when [UploadPolicy.keyPrefix] is set,
     * or a bare UUID otherwise.  If [UploadPolicy.allowedContentTypes] is non-empty
     * the first entry is embedded in the signature, meaning the client **must** send
     * that exact `Content-Type` header.  If [UploadPolicy.maxContentLength] is set
     * the value is embedded as the required `Content-Length`.
     *
     * @return [UploadToken] with [HttpMethod.PUT] and the required upload headers
     */
    override fun generateUploadToken(policy: UploadPolicy): UploadToken {
        val objectKey = buildObjectKey(policy.keyPrefix)
        val expiresAt = policy.expiresAt()

        val requiredHeaders = mutableMapOf<String, String>()
        val s3PutRequest = S3PutObjectRequest.builder()
            .bucket(policy.bucket)
            .key(objectKey)
            .apply {
                policy.allowedContentTypes?.firstOrNull()?.let {
                    contentType(it)
                    requiredHeaders["Content-Type"] = it
                }
                policy.maxContentLength?.let {
                    contentLength(it)
                    requiredHeaders["Content-Length"] = it.toString()
                }
            }
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(policy.expireSeconds))
            .putObjectRequest(s3PutRequest)
            .build()

        val presigned = try {
            s3Presigner.presignPutObject(presignRequest)
        } catch (e: SdkException) {
            throw StorageException("Failed to generate upload token for bucket '${policy.bucket}'", e)
        }

        return UploadToken(
            uploadUrl = presigned.url().toString(),
            method = HttpMethod.PUT,
            headers = requiredHeaders,
            expiresAt = expiresAt,
        )
    }

    override fun generatePresignedDownloadUrl(bucket: String, key: String, expireSeconds: Long): String {
        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(expireSeconds))
            .getObjectRequest { b -> b.bucket(bucket).key(key) }
            .build()
        return try {
            s3Presigner.presignGetObject(presignRequest).url().toString()
        } catch (e: SdkException) {
            throw StorageException("Failed to generate presigned download URL for '$key' in '$bucket'", e)
        }
    }

    override fun close() {
        s3Client.close()
        s3Presigner.close()
    }

    private fun buildObjectKey(keyPrefix: String?): String {
        val uuid = UUID.randomUUID().toString()
        return if (keyPrefix.isNullOrEmpty()) uuid else "$keyPrefix$uuid"
    }
}
