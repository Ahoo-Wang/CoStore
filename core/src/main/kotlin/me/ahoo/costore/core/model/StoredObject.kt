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
interface StoredObjectMetadata :
    BucketCapable,
    ObjectKeyCapable,
    NullableContentLengthCapable,
    NullableContentTypeCapable,
    NullableVersionIdCapable {
    /** Last modification timestamp, if available. */
    val lastModified: Instant?

    /** ETag value for content identification, typically an MD5 hash. */
    val eTag: String?

    /** Custom user-defined metadata key-value pairs. */
    val metadata: Map<String, String>
}

/** Interface for types that carry object metadata. */
interface StoredObjectMetadataCapable {
    val metadata: StoredObjectMetadata
}

/**
 * A stored object including both metadata and content.
 *
 * Use [content] to read the object's data as an [InputStream].
 */
interface StoredObject : StoredObjectMetadataCapable {
    /** The object's content as a stream. */
    val content: InputStream
}

data class DefaultStoredObjectMetadata(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val contentLength: Long? = null,
    override val contentType: String? = null,
    override val lastModified: Instant? = null,
    override val eTag: String? = null,
    override val metadata: Map<String, String> = emptyMap(),
    override val versionId: String? = null
) : StoredObjectMetadata

data class DefaultStoredObject(
    override val content: InputStream,
    override val metadata: StoredObjectMetadata
) : StoredObject
