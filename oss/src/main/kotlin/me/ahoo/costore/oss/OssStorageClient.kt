package me.ahoo.costore.oss

import com.aliyun.oss.OSSClient
import com.aliyun.oss.OSSException
import com.aliyun.oss.model.MatchMode
import com.aliyun.oss.model.ObjectMetadata
import com.aliyun.oss.model.PolicyConditions
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
import java.util.Base64
import java.util.Date
import java.util.UUID
import com.aliyun.oss.model.GetObjectRequest as OssGetObjectRequest
import com.aliyun.oss.model.ListObjectsV2Request as OssListObjectsV2Request

/**
 * Alibaba Cloud OSS implementation of [StorageClient].
 *
 * Upload tokens are produced as OSS [Post Policy](https://www.alibabacloud.com/help/en/oss/developer-reference/postobject)
 * form fields, which natively support:
 * - object key prefix restrictions
 * - content-type restrictions
 * - content-length range restrictions
 *
 * Clients should POST a `multipart/form-data` request to [UploadToken.uploadUrl]
 * including all [UploadToken.fields] as form fields before the file part.
 *
 * Use [OssStorageClientFactory] or construct directly, passing a configured [OSSClient].
 *
 * @param ossClient   configured [OSSClient]; ownership is transferred – calling [close]
 *                    on this instance will shut it down
 * @param accessKeyId the Alibaba Cloud access key ID; embedded in the generated upload token
 */
class OssStorageClient(
    private val ossClient: OSSClient,
    private val accessKeyId: String,
) : StorageClient {

    override fun putObject(request: PutObjectRequest): PutObjectResponse {
        return try {
            val metadata = ObjectMetadata().apply {
                request.contentType?.let { contentType = it }
                contentLength = request.contentLength
                request.metadata.forEach { (k, v) -> addUserMetadata(k, v) }
            }
            val result = ossClient.putObject(request.bucket, request.key, request.inputStream, metadata)
            PutObjectResponse(eTag = result.eTag)
        } catch (e: OSSException) {
            throw StorageException("Failed to put object '${request.key}' in bucket '${request.bucket}'", e)
        }
    }

    override fun getObject(request: GetObjectRequest): StorageObject {
        return try {
            val ossRequest = OssGetObjectRequest(request.bucket, request.key)
            val ossObject = ossClient.getObject(ossRequest)
            val metadata = ossObject.objectMetadata
            StorageObject(
                key = request.key,
                content = ossObject.objectContent,
                contentLength = metadata.contentLength,
                contentType = metadata.contentType,
                metadata = metadata.userMetadata ?: emptyMap(),
                lastModified = metadata.lastModified?.toInstant() ?: java.time.Instant.EPOCH,
            )
        } catch (e: OSSException) {
            if (e.errorCode == "NoSuchKey") {
                throw ObjectNotFoundException(bucket = request.bucket, key = request.key, cause = e)
            }
            throw StorageException("Failed to get object '${request.key}' from bucket '${request.bucket}'", e)
        }
    }

    override fun deleteObject(request: DeleteObjectRequest) {
        try {
            ossClient.deleteObject(request.bucket, request.key)
        } catch (e: OSSException) {
            throw StorageException(
                "Failed to delete object '${request.key}' from bucket '${request.bucket}'",
                e,
            )
        }
    }

    override fun listObjects(request: ListObjectsRequest): ListObjectsResponse {
        return try {
            val ossRequest = OssListObjectsV2Request(request.bucket).apply {
                request.prefix?.let { prefix = it }
                request.delimiter?.let { delimiter = it }
                maxKeys = request.maxKeys
                request.continuationToken?.let { continuationToken = it }
            }
            val result = ossClient.listObjectsV2(ossRequest)
            ListObjectsResponse(
                objects = result.objectSummaries.map { obj ->
                    StorageObjectSummary(
                        key = obj.key,
                        size = obj.size,
                        lastModified = obj.lastModified?.toInstant() ?: java.time.Instant.EPOCH,
                        eTag = obj.eTag,
                    )
                },
                isTruncated = result.isTruncated,
                nextContinuationToken = result.nextContinuationToken,
            )
        } catch (e: OSSException) {
            throw StorageException("Failed to list objects in bucket '${request.bucket}'", e)
        }
    }

    /**
     * Generates an OSS Post Policy upload token governed by [policy].
     *
     * The returned [UploadToken] uses [HttpMethod.POST] and contains all the form
     * fields required by OSS:
     * - `key` – the target object key (prefix + UUID)
     * - `OSSAccessKeyId` – the access key ID used to sign the policy
     * - `policy` – the base64-encoded policy JSON
     * - `Signature` – the HMAC-SHA1 signature of the policy
     * - `Content-Type` – only present when a single content type is allowed
     *
     * @return [UploadToken] with [HttpMethod.POST] ready for browser or server upload
     */
    override fun generateUploadToken(policy: UploadPolicy): UploadToken {
        val objectKey = buildObjectKey(policy.keyPrefix)
        val expiresAt = policy.expiresAt()
        val expiration = Date.from(expiresAt)

        val conditions = PolicyConditions().apply {
            // Key condition: exact key or prefix match
            if (policy.keyPrefix.isNullOrEmpty()) {
                addConditionItem(PolicyConditions.COND_KEY, objectKey)
            } else {
                addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, policy.keyPrefix)
            }
            // Content-type condition
            policy.allowedContentTypes?.firstOrNull()?.let { contentType ->
                addConditionItem(PolicyConditions.COND_CONTENT_TYPE, contentType)
            }
            // Content-length range condition
            policy.maxContentLength?.let { maxLen ->
                addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 1L, maxLen)
            }
        }

        return try {
            val postPolicy = ossClient.generatePostPolicy(expiration, conditions)
            val encodedPolicy = Base64.getEncoder().encodeToString(postPolicy.toByteArray(Charsets.UTF_8))
            val signature = ossClient.calculatePostSignature(postPolicy)

            val fields = buildMap<String, String> {
                put("key", objectKey)
                put("OSSAccessKeyId", accessKeyId)
                put("policy", encodedPolicy)
                put("Signature", signature)
                policy.allowedContentTypes?.firstOrNull()?.let { put("Content-Type", it) }
            }

            UploadToken(
                uploadUrl = buildUploadUrl(policy.bucket),
                method = HttpMethod.POST,
                fields = fields,
                expiresAt = expiresAt,
            )
        } catch (e: OSSException) {
            throw StorageException("Failed to generate upload token for bucket '${policy.bucket}'", e)
        }
    }

    override fun generatePresignedDownloadUrl(bucket: String, key: String, expireSeconds: Long): String {
        val expiration = Date(System.currentTimeMillis() + expireSeconds * 1000L)
        return try {
            ossClient.generatePresignedUrl(bucket, key, expiration).toString()
        } catch (e: OSSException) {
            throw StorageException("Failed to generate presigned download URL for '$key' in '$bucket'", e)
        }
    }

    override fun close() {
        ossClient.shutdown()
    }

    private fun buildObjectKey(keyPrefix: String?): String {
        val uuid = UUID.randomUUID().toString()
        return if (keyPrefix.isNullOrEmpty()) uuid else "$keyPrefix$uuid"
    }

    private fun buildUploadUrl(bucket: String): String {
        val endpoint = ossClient.endpoint.toString().trimEnd('/')
        return "$endpoint/$bucket"
    }
}
