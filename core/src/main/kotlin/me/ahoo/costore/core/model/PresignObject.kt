package me.ahoo.costore.core.model

import java.net.URL
import java.time.Duration
import java.time.Instant

interface PresignedUrlCapable {
    val presignedUrl: String
}

interface PresignRequest : BucketCapable,
    ObjectKeyCapable {
    val expiration: Duration
}

interface PresignObjectResponse : PresignedUrlCapable {
    val url: URL
    val expiration: Instant
    val headers: Map<String, List<String>>
}

interface PresignGetObjectRequest : PresignRequest

interface PresignGetObjectResponse : PresignObjectResponse

interface PresignPutObjectRequest : PresignRequest {
    val contentType: String?
}

interface PresignPutObjectResponse : PresignedUrlCapable

interface PresignDeleteObjectRequest : PresignRequest, NullableVersionIdCapable

interface PresignDeleteObjectResponse : PresignedUrlCapable
