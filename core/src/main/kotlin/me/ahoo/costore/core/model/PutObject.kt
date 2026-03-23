package me.ahoo.costore.core.model

import java.io.InputStream
import java.time.Instant

interface PutObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val content: InputStream
    val contentType: String?
    val metadata: Map<String, String>
    val storageClass: String?
}

interface PutObjectResponse {
    val eTag: String?
    val versionId: String?
    val lastModified: Instant?
}
