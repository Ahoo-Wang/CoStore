package me.ahoo.costore.core.model

import java.io.InputStream
import java.time.Instant

/** Indicates the object may have a version ID (for versioned buckets). */
interface NullableVersionIdCapable {
    val versionId: String?
}

/** Indicates the object may have a content length. */
interface NullableContentLengthCapable {
    val contentLength: Long?
}

/** Indicates the object may have a content type (MIME type). */
interface NullableContentTypeCapable {
    val contentType: String?
}

/**
 * Metadata for a stored object, excluding the content itself.
 *
 * Provides access to all object attributes such as size, type, modification time,
 * ETag, and custom user metadata.
 */
data class StoredObjectMetadata(
    val bucket: BucketName,
    val key: ObjectKey,
    val contentLength: Long? = null,
    val contentType: String? = null,
    val lastModified: Instant? = null,
    val eTag: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val versionId: String? = null
)

/**
 * A stored object including both metadata and content.
 *
 * Use [content] to read the object's data as an [InputStream].
 */
data class StoredObject(
    val content: InputStream,
    val metadata: StoredObjectMetadata
)
