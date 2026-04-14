package me.ahoo.costore.core.model

import java.io.InputStream
import java.time.Instant

data class PutObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val content: InputStream,
    override val contentType: String? = null,
    override val metadata: Map<String, String> = emptyMap(),
) : BucketCapable, NullableContentTypeCapable, ObjectKeyCapable, ContentCapable, UserMetadataCapable

data class PutObjectResponse(
    override val eTag: String? = null,
    override val versionId: String? = null,
    override val lastModified: Instant? = null,
) : NullableETagCapable, NullableVersionIdCapable, NullableLastModifiedCapable
