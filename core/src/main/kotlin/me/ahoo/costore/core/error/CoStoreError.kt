package me.ahoo.costore.core.error

import me.ahoo.costore.core.model.BucketCapable
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.ObjectKeyCapable

/**
 * Base exception type for all CoStore errors.
 *
 * All library-specific exceptions extend this class to allow centralized exception handling.
 *
 * @param message The error message describing what went wrong
 * @param cause The underlying cause of this error, if any
 */
open class CoStoreError(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/**
 * Error thrown when an object cannot be found in the store.
 *
 * Implements [BucketCapable] and [ObjectKeyCapable] to provide context about
 * which bucket and key was being accessed when the error occurred.
 *
 * @property bucket The bucket where the object was expected to exist
 * @property key The object key that was not found
 * @property message Human-readable error message
 */
class ObjectNotFoundError(
    override val bucket: BucketName,
    override val key: ObjectKey,
    message: String = "Object not found: bucket='$bucket', key='$key'",
) :
    BucketCapable,
    ObjectKeyCapable, CoStoreError(message)
