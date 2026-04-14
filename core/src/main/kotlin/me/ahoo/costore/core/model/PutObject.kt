package me.ahoo.costore.core.model

import java.io.InputStream

data class PutObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val content: InputStream,
    override val contentLength: Long,
    override val contentType: String? = null,
    override val metadata: Map<String, String> = emptyMap(),
) : BucketCapable,
    ContentLengthCapable,
    NullableContentTypeCapable,
    ObjectKeyCapable,
    ContentCapable,
    UserMetadataCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}

data class PutObjectResponse(
    override val eTag: String? = null,
    override val versionId: String? = null,
) : NullableETagCapable, NullableVersionIdCapable
