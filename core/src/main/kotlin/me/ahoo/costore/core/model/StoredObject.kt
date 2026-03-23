package me.ahoo.costore.core.model

import java.io.InputStream

interface StoredObjectMetadata : BucketCapable, ObjectKeyCapable {
    val metadata: Map<String, String>
}

interface StoredObject : StoredObjectMetadata {
    val content: InputStream
}
