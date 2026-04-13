package me.ahoo.costore.core.model

import java.net.URL
import java.time.Duration
import java.time.Instant

interface PresignRequest :
    BucketCapable,
    ObjectKeyCapable {
    val expiration: Duration
}

interface PresignObjectResponse {
    val url: URL
    val expiration: Instant
    val headers: Map<String, List<String>>
}

interface PresignGetObjectRequest : PresignRequest

interface PresignGetObjectResponse : PresignObjectResponse

interface PresignPutObjectRequest : PresignRequest {
    val contentType: String?
}

interface PresignPutObjectResponse : PresignObjectResponse

interface PresignDeleteObjectRequest : PresignRequest, NullableVersionIdCapable

interface PresignDeleteObjectResponse : PresignObjectResponse

data class DefaultPresignGetObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration
) : PresignGetObjectRequest

data class DefaultPresignPutObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration,
    override val contentType: String? = null
) : PresignPutObjectRequest

data class DefaultPresignDeleteObjectRequest(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration,
    override val versionId: String? = null
) : PresignDeleteObjectRequest

data class DefaultPresignGetObjectResponse(
    override val url: URL,
    override val expiration: Instant,
    override val headers: Map<String, List<String>> = emptyMap()
) : PresignGetObjectResponse

data class DefaultPresignPutObjectResponse(
    override val url: URL,
    override val expiration: Instant,
    override val headers: Map<String, List<String>> = emptyMap()
) : PresignPutObjectResponse

data class DefaultPresignDeleteObjectResponse(
    override val url: URL,
    override val expiration: Instant,
    override val headers: Map<String, List<String>> = emptyMap()
) : PresignDeleteObjectResponse
