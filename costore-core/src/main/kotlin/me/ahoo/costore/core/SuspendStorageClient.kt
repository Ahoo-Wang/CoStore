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
 * Combines [SuspendObjectOperations] (put, get, delete, list) with
 * [SuspendCredentialsOperations] (upload tokens, presigned URLs) into a single
 * unified client.
 *
 * All operations are non-blocking when called from a coroutine.  The default
 * implementation, [DefaultSuspendStorageClient], executes each call on
 * [Dispatchers.IO] so that blocking I/O is offloaded from the coroutine dispatcher.
 *
 * Obtain an instance from an existing [StorageClient] via the [asSuspend] extension:
 * ```kotlin
 * val suspendClient = myStorageClient.asSuspend()
 * ```
 *
 * @see StorageClient the synchronous (blocking) equivalent
 * @see DefaultSuspendStorageClient the standard adapter implementation
 */
interface SuspendStorageClient : SuspendObjectOperations, SuspendCredentialsOperations, AutoCloseable

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
