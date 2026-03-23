package me.ahoo.costore.core.model

interface DeleteObjectRequest :
    BucketCapable,
    ObjectKeyCapable,
    NullableVersionIdCapable

interface DeleteObjectResponse : NullableVersionIdCapable {
    val deleteMarker: Boolean
}
