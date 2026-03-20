package me.ahoo.costore.core.exception

/**
 * Base exception for all cloud storage errors raised by CoStore.
 *
 * @property message human-readable description of the failure
 * @property cause   the underlying provider exception, if available
 */
open class StorageException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)

/**
 * Thrown when a requested object does not exist in the bucket.
 *
 * @property bucket the bucket that was queried
 * @property key    the object key that was not found
 */
class ObjectNotFoundException(
    val bucket: String,
    val key: String,
    cause: Throwable? = null,
) : StorageException("Object not found: bucket='$bucket', key='$key'", cause)
