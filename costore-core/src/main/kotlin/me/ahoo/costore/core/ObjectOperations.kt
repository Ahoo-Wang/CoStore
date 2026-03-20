package me.ahoo.costore.core

import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.costore.core.model.StorageObject

/**
 * Synchronous object CRUD operations against cloud storage.
 *
 * All methods block the calling thread until the provider responds.
 *
 * @see SuspendObjectOperations the coroutine-friendly equivalent
 * @see StorageClient combines this with [CredentialsOperations] into a single client interface
 */
interface ObjectOperations {

    /**
     * Uploads an object to the specified bucket.
     *
     * @param request the put-object request containing bucket, key, and content
     * @return [PutObjectResponse] containing the resulting ETag and optional version ID
     */
    fun putObject(request: PutObjectRequest): PutObjectResponse

    /**
     * Downloads an object from the specified bucket.
     *
     * @param request the get-object request containing bucket and key
     * @return [StorageObject] with content stream and metadata
     * @throws me.ahoo.costore.core.exception.ObjectNotFoundException if the object does not exist
     */
    fun getObject(request: GetObjectRequest): StorageObject

    /**
     * Deletes an object from the specified bucket.
     *
     * @param request the delete-object request containing bucket and key
     */
    fun deleteObject(request: DeleteObjectRequest)

    /**
     * Lists objects in a bucket, with optional prefix filtering and pagination.
     *
     * @param request the list-objects request
     * @return [ListObjectsResponse] with the page of results
     */
    fun listObjects(request: ListObjectsRequest): ListObjectsResponse
}

/**
 * Coroutine-friendly object CRUD operations against cloud storage.
 *
 * All methods are declared as `suspend fun` and are safe to call from any coroutine
 * without blocking the calling thread.
 *
 * @see ObjectOperations the synchronous (blocking) equivalent
 * @see SuspendStorageClient combines this with [SuspendCredentialsOperations] into a single client interface
 */
interface SuspendObjectOperations {

    /**
     * Uploads an object to the specified bucket.
     *
     * @param request the put-object request containing bucket, key, and content
     * @return [PutObjectResponse] containing the resulting ETag and optional version ID
     */
    suspend fun putObject(request: PutObjectRequest): PutObjectResponse

    /**
     * Downloads an object from the specified bucket.
     *
     * @param request the get-object request containing bucket and key
     * @return [StorageObject] with content stream and metadata
     * @throws me.ahoo.costore.core.exception.ObjectNotFoundException if the object does not exist
     */
    suspend fun getObject(request: GetObjectRequest): StorageObject

    /**
     * Deletes an object from the specified bucket.
     *
     * @param request the delete-object request containing bucket and key
     */
    suspend fun deleteObject(request: DeleteObjectRequest)

    /**
     * Lists objects in a bucket, with optional prefix filtering and pagination.
     *
     * @param request the list-objects request
     * @return [ListObjectsResponse] with the page of results
     */
    suspend fun listObjects(request: ListObjectsRequest): ListObjectsResponse
}
