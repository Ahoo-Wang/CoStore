package me.ahoo.costore.core.api.reactive

import me.ahoo.costore.core.api.sync.ObjectStore
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
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import reactor.core.publisher.Mono

class DefaultReactiveObjectStore(
    private val delegate: ObjectStore,
) : ReactiveObjectStore {
    override fun close(): Mono<Void> =
        Mono.fromRunnable {
            delegate.close()
        }

    override fun getObject(request: GetObjectRequest): Mono<GetObjectResponse> = Mono.fromCallable {
        delegate.getObject(
            request
        )
    }

    override fun putObject(request: PutObjectRequest): Mono<PutObjectResponse> = Mono.fromCallable {
        delegate.putObject(
            request
        )
    }

    override fun deleteObject(request: DeleteObjectRequest): Mono<DeleteObjectResponse> =
        Mono.fromCallable { delegate.deleteObject(request) }

    override fun listObjects(request: ListObjectsRequest): Mono<ListObjectsResponse> = Mono.fromCallable {
        delegate.listObjects(
            request
        )
    }

    override fun headObject(request: HeadObjectRequest): Mono<HeadObjectResponse> = Mono.fromCallable {
        delegate.headObject(
            request
        )
    }

    override fun presignGetObject(request: PresignGetObjectRequest): Mono<PresignGetObjectResponse> =
        Mono.fromCallable { delegate.presignGetObject(request) }

    override fun presignPutObject(request: PresignPutObjectRequest): Mono<PresignPutObjectResponse> =
        Mono.fromCallable { delegate.presignPutObject(request) }

    override fun presignDeleteObject(request: PresignDeleteObjectRequest): Mono<PresignDeleteObjectResponse> =
        Mono.fromCallable { delegate.presignDeleteObject(request) }

    override fun presignObjects(request: BatchPresignRequest): Mono<BatchPresignResponse> =
        Mono.fromCallable { delegate.presignObjects(request) }
}

fun ObjectStore.asReactive(): ReactiveObjectStore = DefaultReactiveObjectStore(this)
