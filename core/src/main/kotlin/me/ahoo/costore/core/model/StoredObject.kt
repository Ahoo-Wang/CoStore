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
