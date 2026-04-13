package me.ahoo.costore.core.model

import java.io.InputStream
import java.time.Instant

interface PutObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val content: InputStream
    val contentType: String?
    val metadata: Map<String, String>
}

data class DefaultPutObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val content: InputStream,
    override val contentType: String? = null,
    override val metadata: Map<String, String> = emptyMap()
) : PutObjectRequest

interface PutObjectResponse {
    val eTag: String?
    val versionId: String?
    val lastModified: Instant?
}

data class DefaultPutObjectResponse(
    override val eTag: String? = null,
    override val versionId: String? = null,
    override val lastModified: Instant? = null
) : PutObjectResponse
