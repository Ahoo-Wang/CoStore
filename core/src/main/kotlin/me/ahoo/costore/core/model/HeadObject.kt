package me.ahoo.costore.core.model

data class HeadObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey
) : BucketCapable, ObjectKeyCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}

typealias HeadObjectResponse = StoredObjectMetadata
