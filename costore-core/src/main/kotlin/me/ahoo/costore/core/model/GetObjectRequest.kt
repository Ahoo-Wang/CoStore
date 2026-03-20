package me.ahoo.costore.core.model

import java.io.InputStream
import java.time.Instant

/**
 * Request to retrieve an object from cloud storage.
 *
 * @property bucket bucket name
 * @property key    object key
 */
data class GetObjectRequest(
    val bucket: String,
    val key: String,
)

/**
 * A cloud storage object returned by a get-object operation.
 *
 * @property key           object key
 * @property content       raw content stream; the caller must close it after use
 * @property contentLength byte length of [content]
 * @property contentType   MIME type, or null if not set
 * @property metadata      provider-specific metadata key-value pairs
 * @property lastModified  timestamp of the last modification
 */
data class StorageObject(
    val key: String,
    val content: InputStream,
    val contentLength: Long,
    val contentType: String?,
    val metadata: Map<String, String>,
    val lastModified: Instant,
)
