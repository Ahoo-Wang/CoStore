package me.ahoo.costore.core.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.net.URL
import java.time.Duration
import java.time.Instant

typealias PresignMethod = String

interface PresignMethodCapable {
    val method: PresignMethod

    companion object {
        const val PROPERTY_NAME = "method"
    }
}

object PresignMethods {

    const val GET = "GET"
    const val PUT = "PUT"
    const val DELETE = "DELETE"
}

/**
 * Base interface for pre-signed URL requests.
 *
 * Pre-signed URLs grant temporary access to private objects without requiring
 * AWS credentials in the request. The URL is valid for a limited time period.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = PresignMethodCapable.PROPERTY_NAME
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PresignRequest.Get::class, name = PresignMethods.GET),
    JsonSubTypes.Type(value = PresignRequest.Put::class, name = PresignMethods.PUT),
    JsonSubTypes.Type(value = PresignRequest.Delete::class, name = PresignMethods.DELETE),
)
sealed interface PresignRequest :
    BucketCapable,
    ObjectKeyCapable,
    PresignMethodCapable {
    /** How long the pre-signed URL should remain valid. */
    val expiration: Duration

    data class Get(
        override val bucket: BucketName,
        override val key: ObjectKey,
        override val expiration: Duration,
    ) : PresignRequest {
        override val method: PresignMethod = PresignMethods.GET
    }

    data class Put(
        override val bucket: BucketName,
        override val key: ObjectKey,
        override val expiration: Duration,
        override val contentType: String? = null,
        override val metadata: Map<String, String> = emptyMap(),
    ) : PresignRequest, NullableContentTypeCapable, UserMetadataCapable {
        override val method: PresignMethod = PresignMethods.PUT
    }

    data class Delete(
        override val bucket: BucketName,
        override val key: ObjectKey,
        override val expiration: Duration,
        override val versionId: String? = null,
    ) : PresignRequest, NullableVersionIdCapable {
        override val method: PresignMethod = PresignMethods.DELETE
    }
}

/**
 * Base interface for pre-signed URL responses.
 *
 * Contains the generated URL and metadata about when it expires.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = PresignMethodCapable.PROPERTY_NAME
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PresignObjectResponse.Get::class, name = PresignMethods.GET),
    JsonSubTypes.Type(value = PresignObjectResponse.Put::class, name = PresignMethods.PUT),
    JsonSubTypes.Type(value = PresignObjectResponse.Delete::class, name = PresignMethods.DELETE),
)
sealed interface PresignObjectResponse : PresignMethodCapable {
    /** The pre-signed URL that can be used to access the object. */
    val url: URL

    /** The instant at which this URL expires and no longer works. */
    val expiration: Instant

    /** Headers that must be included with requests using this URL. */
    val headers: Map<String, List<String>>

    data class Get(
        override val url: URL,
        override val expiration: Instant,
        override val headers: Map<String, List<String>> = emptyMap(),
    ) : PresignObjectResponse {
        override val method: PresignMethod = PresignMethods.GET
    }

    data class Put(
        override val url: URL,
        override val expiration: Instant,
        override val headers: Map<String, List<String>> = emptyMap(),
    ) : PresignObjectResponse {
        override val method: PresignMethod = PresignMethods.PUT
    }

    data class Delete(
        override val url: URL,
        override val expiration: Instant,
        override val headers: Map<String, List<String>>,
        override val versionId: String? = null,
    ) : PresignObjectResponse, NullableVersionIdCapable {
        override val method: PresignMethod = PresignMethods.DELETE
    }
}

typealias PresignGetObjectRequest = PresignRequest.Get
typealias PresignPutObjectRequest = PresignRequest.Put
typealias PresignDeleteObjectRequest = PresignRequest.Delete

typealias PresignGetObjectResponse = PresignObjectResponse.Get
typealias PresignPutObjectResponse = PresignObjectResponse.Put
typealias PresignDeleteObjectResponse = PresignObjectResponse.Delete

/**
 * Request for batch pre-signed URL generation.
 *
 * Allows generating multiple pre-signed URLs in a single request.
 *
 * @param requests The list of individual pre-sign requests
 */
data class BatchPresignRequest(
    val requests: List<PresignRequest>,
)

/**
 * Response for batch pre-signed URL generation.
 *
 * Contains the generated pre-signed URLs corresponding to each request.
 *
 * @param responses The list of pre-signed URL responses
 */
data class BatchPresignResponse(
    val responses: List<PresignObjectResponse>,
)
