package me.ahoo.costore.core.model

interface DeleteObjectRequest :
    BucketCapable,
    ObjectKeyCapable,
    NullableVersionIdCapable

data class DefaultDeleteObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val versionId: String? = null
) : DeleteObjectRequest

interface DeleteObjectResponse : NullableVersionIdCapable {
    val deleteMarker: Boolean
}

data class DefaultDeleteObjectResponse(
    override val deleteMarker: Boolean = false,
    override val versionId: String? = null
) : DeleteObjectResponse
