package me.ahoo.costore.core.model

import java.time.Instant

interface HeadObjectRequest :
    BucketCapable,
    ObjectKeyCapable

interface HeadObjectResponse {
    val contentLength: Long
    val contentType: String?
    val lastModified: Instant?
    val eTag: String?
    val metadata: Map<String, String>
}
