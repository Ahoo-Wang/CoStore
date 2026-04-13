package me.ahoo.costore.core.api.async

import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.DeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.PresignDeleteObjectRequest
import me.ahoo.costore.core.model.PresignDeleteObjectResponse
import me.ahoo.costore.core.model.PresignGetObjectRequest
import me.ahoo.costore.core.model.PresignGetObjectResponse
import me.ahoo.costore.core.model.PresignPutObjectRequest
import me.ahoo.costore.core.model.PresignPutObjectResponse
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import java.util.concurrent.CompletableFuture

/**
 * Asynchronous object store interface using [CompletableFuture].
 *
 * Provides non-blocking operations for managing objects in S3-compatible storage backends.
 * Each operation returns a [CompletableFuture] that completes with the result or fails with an exception.
 *
 * @see ObjectStore for the synchronous counterpart
 */
interface AsyncObjectStore :
    AsyncGetObjectOperations,
    AsyncPutObjectOperations,
    AsyncDeleteObjectOperations,
    AsyncListObjectsOperations,
    AsyncHeadObjectOperations,
    AsyncPresignObjectOperations {
    /** Closes the store and releases resources. */
    fun close(): CompletableFuture<Void>
}

/** Asynchronous get object operation. */
interface AsyncGetObjectOperations {
    fun getObject(request: GetObjectRequest): CompletableFuture<GetObjectResponse>
}

/** Asynchronous put object operation. */
interface AsyncPutObjectOperations {
    fun putObject(request: PutObjectRequest): CompletableFuture<PutObjectResponse>
}

/** Asynchronous delete object operation. */
interface AsyncDeleteObjectOperations {
    fun deleteObject(request: DeleteObjectRequest): CompletableFuture<DeleteObjectResponse>
}

/** Asynchronous list objects operation. */
interface AsyncListObjectsOperations {
    fun listObjects(request: ListObjectsRequest): CompletableFuture<ListObjectsResponse>
}

/** Asynchronous head object operation. */
interface AsyncHeadObjectOperations {
    fun headObject(request: HeadObjectRequest): CompletableFuture<HeadObjectResponse>
}

/** Asynchronous pre-sign operations for generating temporary access URLs. */
interface AsyncPresignObjectOperations {
    fun presignGetObject(request: PresignGetObjectRequest): CompletableFuture<PresignGetObjectResponse>

    fun presignPutObject(request: PresignPutObjectRequest): CompletableFuture<PresignPutObjectResponse>

    fun presignDeleteObject(request: PresignDeleteObjectRequest): CompletableFuture<PresignDeleteObjectResponse>
}
