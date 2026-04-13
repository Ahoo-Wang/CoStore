package me.ahoo.costore.core.model

/** Type alias for object keys, represented as strings. Object keys identify objects within a bucket. */
typealias ObjectKey = String

/**
 * Interface for types that reference a specific object key.
 *
 * Object keys are the unique identifier for an object within a bucket, similar to a file path.
 */
interface ObjectKeyCapable {
    /** The object key that uniquely identifies the object within its bucket. */
    val key: ObjectKey
}
