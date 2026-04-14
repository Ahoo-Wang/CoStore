package me.ahoo.costore.core.model

data class HeadObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey
) : BucketCapable, ObjectKeyCapable

typealias HeadObjectResponse = StoredObjectMetadata
