package me.ahoo.costore.core.model

import java.io.InputStream
import java.time.Instant

data class PutObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    val content: InputStream,
    val contentType: String? = null,
    val metadata: Map<String, String> = emptyMap()
) : BucketCapable, ObjectKeyCapable

data class PutObjectResponse(
    val eTag: String? = null,
    val versionId: String? = null,
    val lastModified: Instant? = null
)
