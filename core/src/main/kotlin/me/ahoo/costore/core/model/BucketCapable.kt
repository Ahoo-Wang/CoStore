package me.ahoo.costore.core.model

/** Type alias for bucket names, represented as strings. */
typealias BucketName = String

/**
 * Interface for types that reference a specific bucket.
 *
 * All object operations require a bucket reference to know where the object is stored.
 */
interface BucketCapable {
    /** The name of the bucket. */
    @get:BucketNameConstraint
    val bucket: BucketName
}
