package me.ahoo.costore.core.model

data class GetObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
) : BucketCapable,
    ObjectKeyCapable

typealias GetObjectResponse = StoredObject
