package me.ahoo.costore.core.api.reactive

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
import reactor.core.publisher.Mono

/**
 * Reactive object store interface using Project Reactor [Mono].
 *
 * Provides reactive, non-blocking operations for managing objects in S3-compatible storage backends.
 * All operations return [Mono] for lazy evaluation and proper backpressure support.
 *
 * @see ObjectStore for the synchronous counterpart
 */
interface ReactiveObjectStore :
    ReactiveGetObjectOperations,
    ReactivePutObjectOperations,
    ReactiveDeleteObjectOperations,
    ReactiveListObjectsOperations,
    ReactiveHeadObjectOperations,
    ReactivePresignObjectOperations {
    /** Closes the store and releases resources. */
    fun close(): Mono<Void>
}

/** Reactive get object operation. */
interface ReactiveGetObjectOperations {
    fun getObject(request: GetObjectRequest): Mono<GetObjectResponse>
}

/** Reactive put object operation. */
interface ReactivePutObjectOperations {
    fun putObject(request: PutObjectRequest): Mono<PutObjectResponse>
}

/** Reactive delete object operation. */
interface ReactiveDeleteObjectOperations {
    fun deleteObject(request: DeleteObjectRequest): Mono<DeleteObjectResponse>
}

/** Reactive list objects operation. */
interface ReactiveListObjectsOperations {
    fun listObjects(request: ListObjectsRequest): Mono<ListObjectsResponse>
}

/** Reactive head object operation. */
interface ReactiveHeadObjectOperations {
    fun headObject(request: HeadObjectRequest): Mono<HeadObjectResponse>
}

/** Reactive pre-sign operations for generating temporary access URLs. */
interface ReactivePresignObjectOperations {
    fun presignGetObject(request: PresignGetObjectRequest): Mono<PresignGetObjectResponse>

    fun presignPutObject(request: PresignPutObjectRequest): Mono<PresignPutObjectResponse>

    fun presignDeleteObject(request: PresignDeleteObjectRequest): Mono<PresignDeleteObjectResponse>

    fun presignObjects(request: BatchPresignRequest): Mono<BatchPresignResponse> {
        val monos = request.requests.map { presignRequest ->
            when (presignRequest) {
                is PresignRequest.Get -> presignGetObject(presignRequest)
                is PresignRequest.Put -> presignPutObject(presignRequest)
                is PresignRequest.Delete -> presignDeleteObject(presignRequest)
            }
        }
        return Mono.zip(monos) { results ->
            BatchPresignResponse(results.map { it as me.ahoo.costore.core.model.PresignObjectResponse }.toList())
        }
    }
}
