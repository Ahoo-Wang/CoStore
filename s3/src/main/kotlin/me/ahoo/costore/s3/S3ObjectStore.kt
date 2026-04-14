package me.ahoo.costore.s3

import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.DeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.PresignDeleteObjectRequest
import me.ahoo.costore.core.model.PresignGetObjectRequest
import me.ahoo.costore.core.model.PresignObjectResponse
import me.ahoo.costore.core.model.PresignPutObjectRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.costore.core.model.StoredObject
import me.ahoo.costore.core.model.StoredObjectMetadata
import me.ahoo.costore.core.model.normalizeEtag
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PresignedDeleteObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import java.time.Instant
import software.amazon.awssdk.services.s3.model.GetObjectRequest as S3GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadObjectResponse as S3HeadObjectResponse
import software.amazon.awssdk.services.s3.model.ListObjectsResponse as S3ListObjectsResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest as S3PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Object as S3ObjectSummary

/**
 * AWS S3 implementation of [ObjectStore].
 *
 * This implementation wraps the AWS SDK v2 [S3Client] and [S3Presigner] to provide
 * a consistent interface for S3-compatible object storage operations.
 *
 * @property client The underlying S3 client for all operations
 * @property presigner The pre-signer for generating temporary access URLs
 */
class S3ObjectStore(private val client: S3Client, private val presigner: S3Presigner) : ObjectStore {

    override fun getObject(request: GetObjectRequest): GetObjectResponse {
        val sdkRequest = S3GetObjectRequest.builder()
            .bucket(request.bucket)
            .key(request.key)
            .responseContentType(request.contentType)
            .versionId(request.versionId)
            .build()

        return client.getObject(sdkRequest) { response, inputStream ->
            val metadata = StoredObjectMetadata(
                bucket = request.bucket,
                key = request.key,
                contentLength = response.contentLength(),
                contentType = response.contentType(),
                lastModified = response.lastModified(),
                eTag = response.eTag(),
                metadata = response.metadata() ?: emptyMap(),
                versionId = request.versionId
            )
            StoredObject(
                content = inputStream,
                metadata = metadata
            )
        }
    }

    override fun headObject(request: HeadObjectRequest): HeadObjectResponse {
        val sdkResponse: S3HeadObjectResponse = client.headObject {
            it.bucket(request.bucket)
                .key(request.key)
        }
        return StoredObjectMetadata(
            bucket = request.bucket,
            key = request.key,
            contentLength = sdkResponse.contentLength(),
            contentType = sdkResponse.contentType(),
            lastModified = sdkResponse.lastModified(),
            eTag = sdkResponse.eTag().normalizeEtag(),
            metadata = sdkResponse.metadata() ?: emptyMap(),
            versionId = sdkResponse.versionId()
        )
    }

    override fun putObject(request: PutObjectRequest): PutObjectResponse {
        val sdkRequest = S3PutObjectRequest.builder()
            .bucket(request.bucket)
            .key(request.key)
            .contentType(request.contentType)
            .metadata(request.metadata)
            .contentLength(request.contentLength)
            .build()
        val requestBody = RequestBody.fromInputStream(request.content, request.contentLength)
        val sdkResponse = client.putObject(sdkRequest, requestBody)

        return PutObjectResponse(
            eTag = sdkResponse.eTag().normalizeEtag(),
            versionId = sdkResponse.versionId(),
        )
    }

    override fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse {
        val sdkResponse = client.deleteObject {
            it.bucket(request.bucket)
                .key(request.key)
                .versionId(request.versionId)
        }
        return DeleteObjectResponse(
            deleteMarker = sdkResponse.deleteMarker() ?: false,
            versionId = sdkResponse.versionId()
        )
    }

    override fun listObjects(request: ListObjectsRequest): ListObjectsResponse {
        val sdkResponse: S3ListObjectsResponse = client.listObjects {
            it.bucket(request.bucket)
                .prefix(request.prefix)
                .delimiter(request.delimiter)
                .marker(request.marker)
                .maxKeys(request.maxKeys)
        }
        return ListObjectsResponse(
            objects = sdkResponse.contents().map { s3Object: S3ObjectSummary ->
                StoredObjectMetadata(
                    bucket = request.bucket,
                    key = s3Object.key(),
                    contentLength = s3Object.size(),
                    contentType = null,
                    lastModified = s3Object.lastModified(),
                    eTag = s3Object.eTag().normalizeEtag(),
                    metadata = emptyMap(),
                    versionId = null
                )
            },
            commonPrefixes = sdkResponse.commonPrefixes().map { it.prefix() },
            isTruncated = sdkResponse.isTruncated,
            nextMarker = sdkResponse.nextMarker()
        )
    }

    override fun presignGetObject(request: PresignGetObjectRequest): PresignObjectResponse.Get {
        val presignedRequest: PresignedGetObjectRequest = presigner.presignGetObject {
            it.signatureDuration(request.expiration)
                .getObjectRequest {
                    it.bucket(request.bucket)
                        .key(request.key)
                }
        }
        return PresignObjectResponse.Get(
            url = presignedRequest.url(),
            expiration = Instant.ofEpochSecond(
                presignedRequest.expiration().epochSecond,
                presignedRequest.expiration().nano.toLong()
            ),
            headers = presignedRequest.signedHeaders() ?: emptyMap()
        )
    }

    override fun presignPutObject(request: PresignPutObjectRequest): PresignObjectResponse.Put {
        val presignedRequest: PresignedPutObjectRequest = presigner.presignPutObject {
            it.signatureDuration(request.expiration)
                .putObjectRequest {
                    it.bucket(request.bucket)
                        .key(request.key)
                        .contentType(request.contentType)
                        .metadata(request.metadata)
                }
        }
        return PresignObjectResponse.Put(
            url = presignedRequest.url(),
            expiration = Instant.ofEpochSecond(
                presignedRequest.expiration().epochSecond,
                presignedRequest.expiration().nano.toLong()
            ),
            headers = presignedRequest.signedHeaders() ?: emptyMap()
        )
    }

    override fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignObjectResponse.Delete {
        val presignedRequest: PresignedDeleteObjectRequest = presigner.presignDeleteObject {
            it.signatureDuration(request.expiration)
                .deleteObjectRequest {
                    it.bucket(request.bucket)
                        .key(request.key)
                        .versionId(request.versionId)
                }
        }
        return PresignObjectResponse.Delete(
            url = presignedRequest.url(),
            expiration = Instant.ofEpochSecond(
                presignedRequest.expiration().epochSecond,
                presignedRequest.expiration().nano.toLong()
            ),
            headers = presignedRequest.signedHeaders() ?: emptyMap()
        )
    }

    override fun close() {
        client.close()
    }
}
