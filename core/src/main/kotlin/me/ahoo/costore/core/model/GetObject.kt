package me.ahoo.costore.core.model

interface GetObjectRequest : BucketCapable, ObjectKeyCapable, NullableContentTypeCapable, NullableVersionIdCapable

typealias GetObjectResponse = StoredObject


fun GetObject(block: GetObjectRequest.() -> Unit): GetObjectRequest {
    TODO()
}
