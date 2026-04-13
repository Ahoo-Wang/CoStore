package me.ahoo.costore.core.model

interface GetObjectRequest : BucketCapable, ObjectKeyCapable, NullableContentTypeCapable, NullableVersionIdCapable

typealias GetObjectResponse = StoredObject

data class DefaultGetObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val contentType: String? = null,
    override val versionId: String? = null
) : GetObjectRequest
