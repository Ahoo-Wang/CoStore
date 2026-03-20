package me.ahoo.costore.core.model

import java.io.InputStream

/**
 * Request to upload an object to cloud storage.
 *
 * @property bucket      destination bucket name
 * @property key         object key (path within the bucket)
 * @property inputStream content to upload; the caller is responsible for closing it
 * @property contentLength exact byte length of [inputStream]
 * @property contentType  MIME type of the content (e.g. "image/jpeg"), or null
 * @property metadata     optional provider-specific metadata key-value pairs
 */
data class PutObjectRequest(
    val bucket: String,
    val key: String,
    val inputStream: InputStream,
    val contentLength: Long,
    val contentType: String? = null,
    val metadata: Map<String, String> = emptyMap(),
)

/**
 * Response returned after a successful object upload.
 *
 * @property eTag      the ETag of the uploaded object, if returned by the provider
 * @property versionId the version ID of the object, if versioning is enabled
 */
data class PutObjectResponse(
    val eTag: String? = null,
    val versionId: String? = null,
)
