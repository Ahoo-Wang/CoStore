package me.ahoo.costore.core.model

data class ListObjectsRequest(
    override val bucket: BucketName,
    val prefix: String? = null,
    val delimiter: String? = null,
    val marker: String? = null,
    val maxKeys: Int = 100
) : BucketCapable {
    init {
        require(maxKeys > 0) { "maxKeys must be positive, but was $maxKeys" }
        require(maxKeys <= 1000) { "maxKeys must not exceed 1000, but was $maxKeys" }
    }
}

data class ListObjectsResponse(
    val objects: List<StoredObjectMetadata> = emptyList(),
    val commonPrefixes: List<String> = emptyList(),
    val isTruncated: Boolean = false,
    val nextMarker: String? = null
)
