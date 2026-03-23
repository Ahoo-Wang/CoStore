package me.ahoo.costore.core.model

interface DeleteObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val versionId: String?
}

interface DeleteObjectResponse {
    val deleteMarker: Boolean
    val versionId: String?
}
