package me.ahoo.costore.core.model

data class ListObjectsRequest(
    override val bucket: BucketName,
    val prefix: String? = null,
    val delimiter: String? = null,
    val marker: String? = null,
    val maxKeys: Int = 100
) : BucketCapable

data class ListObjectsResponse(
    val objects: List<StoredObjectMetadata> = emptyList(),
    val commonPrefixes: List<String> = emptyList(),
    val isTruncated: Boolean = false,
    val nextMarker: String? = null
)
