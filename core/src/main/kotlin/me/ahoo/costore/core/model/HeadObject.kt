package me.ahoo.costore.core.model

interface HeadObjectRequest :
    BucketCapable,
    ObjectKeyCapable

typealias HeadObjectResponse = StoredObjectMetadata

data class DefaultHeadObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey
) : HeadObjectRequest
