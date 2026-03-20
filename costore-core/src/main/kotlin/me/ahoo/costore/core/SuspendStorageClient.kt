package me.ahoo.costore.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.costore.core.model.StorageObject
import me.ahoo.costore.core.model.UploadPolicy
import me.ahoo.costore.core.model.UploadToken

/**
 * Coroutine-friendly counterpart to [StorageClient] — **asynchronous (suspend) API**.
 *
 * All operations are declared as `suspend fun` and are safe to call from any
 * coroutine without blocking the calling thread.  The default implementation,
 * [DefaultSuspendStorageClient], executes each call on [Dispatchers.IO] so that
 * blocking I/O is offloaded from the coroutine dispatcher.
 *
 * Obtain an instance from an existing [StorageClient] via the [asSuspend] extension:
 * ```kotlin
 * val suspendClient = myStorageClient.asSuspend()
 * ```
 *
 * @see StorageClient the synchronous (blocking) equivalent
 * @see DefaultSuspendStorageClient the standard adapter implementation
 */
interface SuspendStorageClient : AutoCloseable {

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

    /**
     * Generates a temporary upload token governed by the given [UploadPolicy].
     *
     * @param policy the policy that constrains the allowed bucket, key prefix,
     *               content types, max content length, and token lifetime
     * @return [UploadToken] ready for use by the calling client
     */
    suspend fun generateUploadToken(policy: UploadPolicy): UploadToken

    /**
     * Generates a presigned URL that allows downloading the specified object
     * without requiring cloud-provider credentials.
     *
     * @param bucket        the bucket containing the object
     * @param key           the object key
     * @param expireSeconds how many seconds the URL remains valid (default 3600)
     * @return a presigned download URL string
     */
    suspend fun generatePresignedDownloadUrl(bucket: String, key: String, expireSeconds: Long = 3600): String
}

/**
 * Default implementation of [SuspendStorageClient] that adapts a blocking
 * [StorageClient] for use in coroutine contexts.
 *
 * Every operation dispatches the underlying blocking call to [Dispatchers.IO],
 * so the calling coroutine is never blocked.
 *
 * @param delegate the synchronous [StorageClient] to wrap
 */
class DefaultSuspendStorageClient(
    private val delegate: StorageClient,
) : SuspendStorageClient {

    override suspend fun putObject(request: PutObjectRequest): PutObjectResponse =
        withContext(Dispatchers.IO) { delegate.putObject(request) }

    override suspend fun getObject(request: GetObjectRequest): StorageObject =
        withContext(Dispatchers.IO) { delegate.getObject(request) }

    override suspend fun deleteObject(request: DeleteObjectRequest): Unit =
        withContext(Dispatchers.IO) { delegate.deleteObject(request) }

    override suspend fun listObjects(request: ListObjectsRequest): ListObjectsResponse =
        withContext(Dispatchers.IO) { delegate.listObjects(request) }

    override suspend fun generateUploadToken(policy: UploadPolicy): UploadToken =
        withContext(Dispatchers.IO) { delegate.generateUploadToken(policy) }

    override suspend fun generatePresignedDownloadUrl(bucket: String, key: String, expireSeconds: Long): String =
        withContext(Dispatchers.IO) { delegate.generatePresignedDownloadUrl(bucket, key, expireSeconds) }

    override fun close() = delegate.close()
}

/**
 * Wraps this [StorageClient] in a [DefaultSuspendStorageClient], making all
 * operations available as `suspend fun` on [Dispatchers.IO].
 *
 * ```kotlin
 * val suspendClient: SuspendStorageClient = myStorageClient.asSuspend()
 * ```
 */
fun StorageClient.asSuspend(): SuspendStorageClient = DefaultSuspendStorageClient(this)
