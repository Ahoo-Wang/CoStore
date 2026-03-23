package me.ahoo.costore.core.api.reactive

import me.ahoo.costore.core.api.sync.ObjectStore
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
import reactor.core.scheduler.Schedulers

class DefaultReactiveObjectStore(
    private val delegate: ObjectStore,
) : ReactiveObjectStore {

    private val scheduler = Schedulers.boundedElastic()

    override fun close(): Mono<Void> =
        Mono.fromRunnable {
            delegate.close()
        }.subscribeOn(scheduler).then()

    override fun getObject(request: GetObjectRequest): Mono<GetObjectResponse> =
        Mono.fromCallable { delegate.getObject(request) }.subscribeOn(scheduler)

    override fun putObject(request: PutObjectRequest): Mono<PutObjectResponse> =
        Mono.fromCallable { delegate.putObject(request) }.subscribeOn(scheduler)

    override fun deleteObject(request: DeleteObjectRequest): Mono<DeleteObjectResponse> =
        Mono.fromCallable { delegate.deleteObject(request) }.subscribeOn(scheduler)

    override fun listObjects(request: ListObjectsRequest): Mono<ListObjectsResponse> =
        Mono.fromCallable { delegate.listObjects(request) }.subscribeOn(scheduler)

    override fun headObject(request: HeadObjectRequest): Mono<HeadObjectResponse> =
        Mono.fromCallable { delegate.headObject(request) }.subscribeOn(scheduler)

    override fun presignGetObject(request: PresignGetObjectRequest): Mono<PresignGetObjectResponse> =
        Mono.fromCallable { delegate.presignGetObject(request) }.subscribeOn(scheduler)

    override fun presignPutObject(request: PresignPutObjectRequest): Mono<PresignPutObjectResponse> =
        Mono.fromCallable { delegate.presignPutObject(request) }.subscribeOn(scheduler)

    override fun presignDeleteObject(request: PresignDeleteObjectRequest): Mono<PresignDeleteObjectResponse> =
        Mono.fromCallable { delegate.presignDeleteObject(request) }.subscribeOn(scheduler)
}

fun ObjectStore.asReactive(): ReactiveObjectStore = DefaultReactiveObjectStore(this)
