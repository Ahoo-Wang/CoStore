package me.ahoo.costore.core.model

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern

data class ListObjectsRequest(
    override val bucket: BucketName,
    @field:Pattern(regexp = OBJECT_KEY_PATTERN, message = PREFIX_MESSAGE)
    val prefix: String? = null,
    @field:Pattern(regexp = OBJECT_KEY_PATTERN, message = DELIMITER_MESSAGE)
    val delimiter: String? = null,
    @field:Pattern(regexp = OBJECT_KEY_PATTERN, message = MARKER_MESSAGE)
    val marker: String? = null,
    @field:Min(value = MIN_MAX_KEYS, message = MIN_MAX_KEYS_MESSAGE)
    @field:Max(value = MAX_MAX_KEYS, message = MAX_MAX_KEYS_MESSAGE)
    val maxKeys: Int = 100,
) : BucketCapable {
    companion object {
        const val OBJECT_KEY_PATTERN = "^[^\\n\\r\\t]*$"
        const val PREFIX_MESSAGE = "Prefix must not contain control characters"
        const val DELIMITER_MESSAGE = "Delimiter must not contain control characters"
        const val MARKER_MESSAGE = "Marker must not contain control characters"
        const val MIN_MAX_KEYS: Long = 1
        const val MIN_MAX_KEYS_MESSAGE = "Max keys must be at least 1"
        const val MAX_MAX_KEYS: Long = 1000
        const val MAX_MAX_KEYS_MESSAGE = "Max keys must not exceed 1000"
    }
}

data class ListObjectsResponse(
    val objects: List<StoredObjectMetadata> = emptyList(),
    val commonPrefixes: List<String> = emptyList(),
    val isTruncated: Boolean = false,
    val nextMarker: String? = null,
)
