package me.ahoo.costore.core.model

import java.time.Instant

interface GetObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val range: LongRange?
    val ifModifiedSince: Instant?
    val ifNoneMatch: String?
}

interface GetObjectResponse : StoredObject {
    val contentLength: Long
    val contentType: String?
    val lastModified: Instant?
    val eTag: String?
}
