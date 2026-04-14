package me.ahoo.costore.core.model

import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.time.Instant

/** Indicates the object may have a version ID (for versioned buckets). */
interface NullableVersionIdCapable {
    @get:VersionIdConstraint
    val versionId: String?
}

/** Indicates the object may have a content length. */
interface NullableContentLengthCapable {
    @get:ContentLengthConstraint
    val contentLength: Long?
}

interface ContentLengthCapable {
    @get:ContentLengthConstraint
    val contentLength: Long
}

interface ContentCapable {
    val content: InputStream
}

/** Indicates the object may have a content type (MIME type). */
interface NullableContentTypeCapable {
    @get:ContentTypeConstraint
    val contentType: String?
}

interface NullableETagCapable {
    val eTag: String?
}

interface NullableLastModifiedCapable {
    val lastModified: Instant?
}

interface UserMetadataCapable {
    val metadata: Map<String, String>
}

/**
 * Normalizes an ETag value by ensuring it is wrapped in double quotes.
 *
 * Some S3-compatible services return ETags without quotes, while the S3 API
 * specification requires quotes. This function ensures consistent formatting.
 *
 * @receiver The ETag string, possibly null
 * @return The normalized ETag with surrounding quotes, or null if input was null
 */
fun String?.normalizeEtag(): String? = this?.let {
    val trimmed = it.trim()
    when {
        trimmed.isEmpty() -> null
        trimmed.startsWith("\"") && trimmed.endsWith("\"") -> trimmed
        trimmed.startsWith("\"") && !trimmed.endsWith("\"") -> trimmed + "\""
        else -> "\"$trimmed\""
    }
}

/**
 * Metadata for a stored object, excluding the content itself.
 *
 * Provides access to all object attributes such as size, type, modification time,
 * ETag, and custom user metadata.
 */
data class StoredObjectMetadata(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val contentLength: Long? = null,
    override val contentType: String? = null,
    override val lastModified: Instant? = null,
    override val eTag: String? = null,
    override val metadata: Map<String, String> = emptyMap(),
    override val versionId: String? = null,
) : BucketCapable,
    ObjectKeyCapable,
    NullableContentLengthCapable,
    NullableContentTypeCapable,
    NullableETagCapable,
    UserMetadataCapable,
    NullableVersionIdCapable,
    NullableLastModifiedCapable

/**
 * A stored object including both metadata and content.
 *
 * Use [content] to read the object's data as an [InputStream].
 *
 * This object implements [Closeable] and **MUST be closed by the caller**
 * after reading to release underlying resources (e.g., HTTP connections).
 *
 * Example - REQUIRED usage with try-with-resources:
 * ```kotlin
 * store.getObject(request).use { obj ->
 *     obj.content.readBytes()
 * }
 * ```
 *
 * WARNING: Failure to close this object will cause resource leaks.
 * The object MUST be closed even if an exception occurs during reading.
 *
 * @see Closeable
 */
data class StoredObject(
    override val content: InputStream,
    val metadata: StoredObjectMetadata,
) : ContentCapable, Closeable {

    /**
     * Closes this stored object and its underlying content stream.
     *
     * @throws IOException if an I/O error occurs while closing the stream
     */
    @Throws(IOException::class)
    override fun close() {
        content.close()
    }
}
