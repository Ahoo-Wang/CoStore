package me.ahoo.costore.core.model

/**
 * Request to delete an object from cloud storage.
 *
 * @property bucket bucket name
 * @property key    object key to delete
 */
data class DeleteObjectRequest(
    val bucket: String,
    val key: String,
)
