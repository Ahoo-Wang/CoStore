package me.ahoo.costore.core.model

import java.time.Instant

/**
 * Request to list objects in a bucket.
 *
 * @property bucket             bucket name
 * @property prefix             key prefix filter; only objects whose keys start with
 *                              this prefix are returned. Null means no filter.
 * @property delimiter          groups keys sharing a common prefix into virtual
 *                              "directories". Null means no grouping.
 * @property maxKeys            maximum number of objects to return per page (1–1000)
 * @property continuationToken  opaque token for fetching the next page, taken from
 *                              [ListObjectsResponse.nextContinuationToken]
 */
data class ListObjectsRequest(
    val bucket: String,
    val prefix: String? = null,
    val delimiter: String? = null,
    val maxKeys: Int = 1000,
    val continuationToken: String? = null,
)

/**
 * A lightweight summary of a single cloud storage object in a listing.
 *
 * @property key          object key
 * @property size         object size in bytes
 * @property lastModified timestamp of the last modification
 * @property eTag         entity tag (checksum), or null if not provided by the provider
 */
data class StorageObjectSummary(
    val key: String,
    val size: Long,
    val lastModified: Instant,
    val eTag: String? = null,
)

/**
 * Paginated response from a list-objects operation.
 *
 * @property objects               list of object summaries in the current page
 * @property isTruncated           true when there are more objects beyond this page
 * @property nextContinuationToken opaque token to pass in the next request to
 *                                 retrieve the following page; null when [isTruncated]
 *                                 is false
 */
data class ListObjectsResponse(
    val objects: List<StorageObjectSummary>,
    val isTruncated: Boolean,
    val nextContinuationToken: String? = null,
)
