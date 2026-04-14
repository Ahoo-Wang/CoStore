package me.ahoo.costore.core.model

/**
 * Validation utilities for CoStore requests.
 *
 * Provides validation for bucket names and object keys to ensure they meet
 * the requirements of S3-compatible storage backends.
 */
object ObjectStoreValidation {

    /**
     * Validates a bucket name.
     *
     * @param bucket The bucket name to validate
     * @throws IllegalArgumentException if the bucket name is invalid
     */
    fun validateBucketName(bucket: BucketName) {
        require(bucket.isNotBlank()) { "Bucket name must not be blank" }
        require(!bucket.contains("\n")) { "Bucket name must not contain newline characters" }
        require(!bucket.contains("\r")) { "Bucket name must not contain carriage return characters" }
        require(!bucket.contains("\t")) { "Bucket name must not contain tab characters" }
    }

    /**
     * Validates an object key.
     *
     * @param key The object key to validate
     * @throws IllegalArgumentException if the object key is invalid
     */
    fun validateObjectKey(key: ObjectKey) {
        require(key.isNotBlank()) { "Object key must not be blank" }
        require(!key.contains("\n")) { "Object key must not contain newline characters" }
        require(!key.contains("\r")) { "Object key must not contain carriage return characters" }
        require(!key.contains("\t")) { "Object key must not contain tab characters" }
    }
}
