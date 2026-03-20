package me.ahoo.costore.core.model

import java.time.Instant

/**
 * The HTTP method used when uploading via an [UploadToken].
 */
enum class HttpMethod {
    /** The client sends a single HTTP PUT with the file as the request body. */
    PUT,

    /**
     * The client sends a multipart/form-data POST with all [UploadToken.fields]
     * included as form fields before the file part.
     */
    POST,
}

/**
 * A temporary, policy-scoped upload token produced by
 * [me.ahoo.costore.core.StorageClient.generateUploadToken].
 *
 * The token is vendor-agnostic: consumers inspect [method] to determine the upload
 * protocol, then use [uploadUrl], [fields] (for POST), and [headers] (for PUT) to
 * construct the upload request.
 *
 * ### PUT (e.g. AWS S3 presigned URL)
 * ```
 * PUT {uploadUrl}
 * {headers entries as HTTP headers}
 * [request body: raw file bytes]
 * ```
 *
 * ### POST (e.g. Alibaba Cloud OSS Post Policy)
 * ```
 * POST {uploadUrl}
 * Content-Type: multipart/form-data
 * [form fields from {fields}, followed by the "file" part]
 * ```
 *
 * @property uploadUrl  the URL to which the upload request is sent
 * @property method     HTTP method to use ([HttpMethod.PUT] or [HttpMethod.POST])
 * @property fields     form fields required for [HttpMethod.POST] uploads;
 *                      must be included in the multipart body in insertion order
 * @property headers    HTTP headers required for [HttpMethod.PUT] uploads;
 *                      the client must include every entry as a request header
 * @property expiresAt  the instant at which this token ceases to be valid
 */
data class UploadToken(
    val uploadUrl: String,
    val method: HttpMethod,
    val fields: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap(),
    val expiresAt: Instant,
)
