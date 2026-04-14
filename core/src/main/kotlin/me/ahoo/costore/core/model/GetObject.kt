package me.ahoo.costore.core.model

data class GetObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val contentType: String? = null,
    override val versionId: String? = null
) : BucketCapable, ObjectKeyCapable, NullableContentTypeCapable, NullableVersionIdCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}

typealias GetObjectResponse = StoredObject
