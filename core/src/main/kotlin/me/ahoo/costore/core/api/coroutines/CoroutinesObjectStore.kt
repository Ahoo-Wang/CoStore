package me.ahoo.costore.core.api.coroutines

import me.ahoo.costore.core.model.BatchPresignRequest
import me.ahoo.costore.core.model.BatchPresignResponse
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
import me.ahoo.costore.core.model.PresignRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse

/**
 * Coroutines-based object store interface using Kotlin suspend functions.
 *
 * Provides structured concurrency for managing objects in S3-compatible storage backends.
 * Operations are suspending functions that can be used with Kotlin coroutines and structured concurrency.
 *
 * @see ObjectStore for the synchronous counterpart
 */
interface CoroutinesObjectStore :
    CoroutinesGetObjectOperations,
    CoroutinesPutObjectOperations,
    CoroutinesDeleteObjectOperations,
    CoroutinesListObjectsOperations,
    CoroutinesHeadObjectOperations,
    CoroutinesPresignObjectOperations {
    /** Closes the store and releases resources. */
    suspend fun close()
}

/** Suspending get object operation. */
interface CoroutinesGetObjectOperations {
    suspend fun getObject(request: GetObjectRequest): GetObjectResponse
}

/** Suspending put object operation. */
interface CoroutinesPutObjectOperations {
    suspend fun putObject(request: PutObjectRequest): PutObjectResponse
}

/** Suspending delete object operation. */
interface CoroutinesDeleteObjectOperations {
    suspend fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse
}

/** Suspending list objects operation. */
interface CoroutinesListObjectsOperations {
    suspend fun listObjects(request: ListObjectsRequest): ListObjectsResponse
}

/** Suspending head object operation. */
interface CoroutinesHeadObjectOperations {
    suspend fun headObject(request: HeadObjectRequest): HeadObjectResponse
}

/** Suspending pre-sign operations for generating temporary access URLs. */
interface CoroutinesPresignObjectOperations {
    suspend fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse

    suspend fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse

    suspend fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse

    suspend fun presignObjects(request: BatchPresignRequest): BatchPresignResponse
}
