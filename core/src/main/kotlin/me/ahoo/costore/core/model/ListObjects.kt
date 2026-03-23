package me.ahoo.costore.core.model

interface ListObjectsRequest : BucketCapable {
    val prefix: String?
    val delimiter: String?
    val marker: String?
    val maxKeys: Int
}

interface ListObjectsResponse {
    val objects: List<StoredObjectMetadata>
    val commonPrefixes: List<String>
    val isTruncated: Boolean
    val nextMarker: String?
}
