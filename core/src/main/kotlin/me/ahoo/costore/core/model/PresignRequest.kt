package me.ahoo.costore.core.model

import java.net.URL
import java.time.Duration
import java.time.Instant

/**
 * Base interface for pre-signed URL requests.
 *
 * Pre-signed URLs grant temporary access to private objects without requiring
 * AWS credentials in the request. The URL is valid for a limited time period.
 */
interface PresignRequest :
    BucketCapable,
    ObjectKeyCapable {
    /** How long the pre-signed URL should remain valid. */
    val expiration: Duration
}

/**
 * Base interface for pre-signed URL responses.
 *
 * Contains the generated URL and metadata about when it expires.
 */
interface PresignObjectResponse {
    /** The pre-signed URL that can be used to access the object. */
    val url: URL

    /** The instant at which this URL expires and no longer works. */
    val expiration: Instant

    /** Headers that must be included with requests using this URL. */
    val headers: Map<String, List<String>>
}

/** Request to generate a pre-signed URL for reading an object. */
interface PresignGetObjectRequest : PresignRequest

/** Response containing a pre-signed URL for reading an object. */
interface PresignGetObjectResponse : PresignObjectResponse

/**
 * Request to generate a pre-signed URL for uploading an object.
 *
 * @property contentType The Content-Type header that must be used when uploading.
 */
interface PresignPutObjectRequest : PresignRequest {
    val contentType: String?
}

/** Response containing a pre-signed URL for uploading an object. */
interface PresignPutObjectResponse : PresignObjectResponse

/**
 * Request to generate a pre-signed URL for deleting an object.
 *
 * May optionally specify a version ID for deleting specific versions in versioned buckets.
 */
interface PresignDeleteObjectRequest : PresignRequest, NullableVersionIdCapable

/** Response containing a pre-signed URL for deleting an object. */
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
