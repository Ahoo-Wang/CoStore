package me.ahoo.costore.oss

import com.aliyun.oss.OSS
import com.aliyun.oss.model.GeneratePresignedUrlRequest
import com.aliyun.oss.model.ObjectMetadata
import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DefaultDeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.DefaultListObjectsResponse
import me.ahoo.costore.core.model.DefaultPresignDeleteObjectResponse
import me.ahoo.costore.core.model.DefaultPresignGetObjectResponse
import me.ahoo.costore.core.model.DefaultPresignPutObjectResponse
import me.ahoo.costore.core.model.DefaultPutObjectResponse
import me.ahoo.costore.core.model.DefaultStoredObject
import me.ahoo.costore.core.model.DefaultStoredObjectMetadata
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PresignDeleteObjectRequest
import me.ahoo.costore.core.model.PresignGetObjectRequest
import me.ahoo.costore.core.model.PresignPutObjectRequest
import me.ahoo.costore.core.model.PutObjectRequest
import java.io.ByteArrayInputStream
import java.time.Instant

class OssObjectStore(private val client: OSS) : ObjectStore {

    private fun normalizeEtag(etag: String?): String? = etag?.let {
        if (it.startsWith("\"")) it else "\"$it\""
    }

    override fun getObject(request: GetObjectRequest): GetObjectResponse {
        val sdkRequest = com.aliyun.oss.model.GetObjectRequest(request.bucket, request.key)
        client.getObject(sdkRequest).use { ossObject ->
            val contentBytes = ossObject.objectContent.readAllBytes()
            val objectMetadata = ossObject.objectMetadata
            val storedMetadata = DefaultStoredObjectMetadata(
                bucket = request.bucket,
                key = request.key,
                contentLength = objectMetadata.contentLength,
                contentType = objectMetadata.contentType,
                lastModified = objectMetadata.lastModified?.time?.let { Instant.ofEpochMilli(it) },
                eTag = normalizeEtag(objectMetadata.eTag),
                metadata = objectMetadata.userMetadata ?: emptyMap(),
                versionId = objectMetadata.versionId
            )
            return DefaultStoredObject(
                content = ByteArrayInputStream(contentBytes),
                metadata = storedMetadata
            )
        }
    }

    override fun headObject(request: HeadObjectRequest): HeadObjectResponse {
        val metadata = client.getObjectMetadata(request.bucket, request.key)
        return DefaultStoredObjectMetadata(
            bucket = request.bucket,
            key = request.key,
            contentLength = metadata.contentLength,
            contentType = metadata.contentType,
            lastModified = metadata.lastModified?.time?.let { Instant.ofEpochMilli(it) },
            eTag = normalizeEtag(metadata.eTag),
            metadata = metadata.userMetadata ?: emptyMap(),
            versionId = metadata.versionId
        )
    }

    override fun putObject(request: PutObjectRequest): DefaultPutObjectResponse {
        val contentBytes = request.content.readAllBytes()
        val objectMetadata = ObjectMetadata().apply {
            contentType = request.contentType
            userMetadata = request.metadata
        }
        val result = client.putObject(request.bucket, request.key, ByteArrayInputStream(contentBytes), objectMetadata)
        return DefaultPutObjectResponse(
            eTag = normalizeEtag(result.eTag),
            versionId = null,
            lastModified = null
        )
    }

    override fun deleteObject(request: DeleteObjectRequest): DefaultDeleteObjectResponse {
        client.deleteObject(request.bucket, request.key)
        return DefaultDeleteObjectResponse(
            deleteMarker = false,
            versionId = null
        )
    }

    override fun listObjects(request: ListObjectsRequest): DefaultListObjectsResponse {
        val sdkRequest = com.aliyun.oss.model.ListObjectsRequest(request.bucket).apply {
            prefix = request.prefix
            delimiter = request.delimiter
            marker = request.marker
            maxKeys = request.maxKeys
        }
        val result = client.listObjects(sdkRequest)
        return DefaultListObjectsResponse(
            objects = result.objectSummaries.map { summary ->
                DefaultStoredObjectMetadata(
                    bucket = request.bucket,
                    key = summary.key,
                    contentLength = summary.size,
                    contentType = null,
                    lastModified = summary.lastModified?.time?.let { Instant.ofEpochMilli(it) },
                    eTag = normalizeEtag(summary.eTag),
                    metadata = emptyMap(),
                    versionId = null
                )
            },
            commonPrefixes = result.commonPrefixes,
            isTruncated = result.isTruncated,
            nextMarker = result.nextMarker
        )
    }

    override fun presignGetObject(request: PresignGetObjectRequest): DefaultPresignGetObjectResponse {
        val sdkRequest = GeneratePresignedUrlRequest(request.bucket, request.key).apply {
            expiration = java.util.Date(System.currentTimeMillis() + request.expiration.toMillis())
        }
        val url = client.generatePresignedUrl(sdkRequest)
        return DefaultPresignGetObjectResponse(
            url = url,
            expiration = Instant.ofEpochMilli(System.currentTimeMillis() + request.expiration.toMillis()),
            headers = emptyMap()
        )
    }

    override fun presignPutObject(request: PresignPutObjectRequest): DefaultPresignPutObjectResponse {
        val sdkRequest = GeneratePresignedUrlRequest(request.bucket, request.key).apply {
            expiration = java.util.Date(System.currentTimeMillis() + request.expiration.toMillis())
        }
        val url = client.generatePresignedUrl(sdkRequest)
        return DefaultPresignPutObjectResponse(
            url = url,
            expiration = Instant.ofEpochMilli(System.currentTimeMillis() + request.expiration.toMillis()),
            headers = emptyMap()
        )
    }

    override fun presignDeleteObject(request: PresignDeleteObjectRequest): DefaultPresignDeleteObjectResponse {
        val sdkRequest = GeneratePresignedUrlRequest(request.bucket, request.key).apply {
            expiration = java.util.Date(System.currentTimeMillis() + request.expiration.toMillis())
        }
        val url = client.generatePresignedUrl(sdkRequest)
        return DefaultPresignDeleteObjectResponse(
            url = url,
            expiration = Instant.ofEpochMilli(System.currentTimeMillis() + request.expiration.toMillis()),
            headers = emptyMap()
        )
    }

    override fun close() {
        client.shutdown()
    }
}
