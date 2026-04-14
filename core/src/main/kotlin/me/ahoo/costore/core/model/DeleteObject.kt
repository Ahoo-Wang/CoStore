package me.ahoo.costore.core.model

data class DeleteObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val versionId: String? = null
) : BucketCapable, ObjectKeyCapable, NullableVersionIdCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}

data class DeleteObjectResponse(
    val deleteMarker: Boolean = false,
    override val versionId: String? = null
) : NullableVersionIdCapable
