package me.ahoo.costore.core.model

import java.io.InputStream
import java.time.Instant

interface StoredObjectMetadata :
    BucketCapable,
    ObjectKeyCapable {
    val contentLength: Long
    val contentType: String?
    val lastModified: Instant?
    val eTag: String?
    val metadata: Map<String, String>
}

interface StoredObject : StoredObjectMetadata {
    val content: InputStream
}
