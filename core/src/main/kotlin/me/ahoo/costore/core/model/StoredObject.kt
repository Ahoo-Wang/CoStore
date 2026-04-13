package me.ahoo.costore.core.model

import java.io.InputStream
import java.time.Instant

interface NullableVersionIdCapable {
    val versionId: String?
}

interface NullableContentLengthCapable {
    val contentLength: Long?
}

interface NullableContentTypeCapable {
    val contentType: String?
}

interface StoredObjectMetadata :
    BucketCapable,
    ObjectKeyCapable,
    NullableContentLengthCapable,
    NullableContentTypeCapable,
    NullableVersionIdCapable {
    val lastModified: Instant?
    val eTag: String?
    val metadata: Map<String, String>
}

interface StoredObjectMetadataCapable {
    val metadata: StoredObjectMetadata
}

interface StoredObject : StoredObjectMetadataCapable {
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
