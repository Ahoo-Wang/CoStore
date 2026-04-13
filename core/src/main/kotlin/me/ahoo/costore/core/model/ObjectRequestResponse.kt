package me.ahoo.costore.core.model

/**
 * Marker interface for all object store request types.
 *
 * Request types are typed data classes that carry parameters for store operations.
 * @see GetObjectRequest
 * @see PutObjectRequest
 * @see DeleteObjectRequest
 * @see ListObjectsRequest
 * @see HeadObjectRequest
 */
interface ObjectRequest

/**
 * Marker interface for all object store response types.
 *
 * Response types contain the results of store operations, including metadata
 * and, for read operations, the object content itself.
 * @see GetObjectResponse
 * @see PutObjectResponse
 * @see DeleteObjectResponse
 * @see ListObjectsResponse
 */
interface ObjectResponse
