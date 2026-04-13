package me.ahoo.costore.core.model

interface ListObjectsRequest : BucketCapable {
    val prefix: String?
    val delimiter: String?
    val marker: String?
    val maxKeys: Int
}

data class DefaultListObjectsRequest(
    override val bucket: BucketName,
    override val prefix: String? = null,
    override val delimiter: String? = null,
    override val marker: String? = null,
    override val maxKeys: Int = 100
) : ListObjectsRequest

interface ListObjectsResponse {
    val objects: List<StoredObjectMetadata>
    val commonPrefixes: List<String>
    val isTruncated: Boolean
    val nextMarker: String?
}

data class DefaultListObjectsResponse(
    override val objects: List<StoredObjectMetadata> = emptyList(),
    override val commonPrefixes: List<String> = emptyList(),
    override val isTruncated: Boolean = false,
    override val nextMarker: String? = null
) : ListObjectsResponse
