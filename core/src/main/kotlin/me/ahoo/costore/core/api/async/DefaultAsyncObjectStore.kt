package me.ahoo.costore.core.api.async

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
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool

class DefaultAsyncObjectStore(
    private val delegate: ObjectStore,
    private val executor: Executor = ForkJoinPool.commonPool(),
) : AsyncObjectStore {
    override fun close(): CompletableFuture<Void> = CompletableFuture.runAsync({ delegate.close() }, executor)

    override fun getObject(request: GetObjectRequest): CompletableFuture<GetObjectResponse> =
        CompletableFuture.supplyAsync({ delegate.getObject(request) }, executor)

    override fun putObject(request: PutObjectRequest): CompletableFuture<PutObjectResponse> =
        CompletableFuture.supplyAsync({ delegate.putObject(request) }, executor)

    override fun deleteObject(request: DeleteObjectRequest): CompletableFuture<DeleteObjectResponse> =
        CompletableFuture.supplyAsync({ delegate.deleteObject(request) }, executor)

    override fun listObjects(request: ListObjectsRequest): CompletableFuture<ListObjectsResponse> =
        CompletableFuture.supplyAsync({ delegate.listObjects(request) }, executor)

    override fun headObject(request: HeadObjectRequest): CompletableFuture<HeadObjectResponse> =
        CompletableFuture.supplyAsync({ delegate.headObject(request) }, executor)

    override fun presignGetObject(request: PresignGetObjectRequest): CompletableFuture<PresignGetObjectResponse> =
        CompletableFuture.supplyAsync({ delegate.presignGetObject(request) }, executor)

    override fun presignPutObject(request: PresignPutObjectRequest): CompletableFuture<PresignPutObjectResponse> =
        CompletableFuture.supplyAsync({ delegate.presignPutObject(request) }, executor)

    override fun presignDeleteObject(request: PresignDeleteObjectRequest): CompletableFuture<PresignDeleteObjectResponse> =
        CompletableFuture.supplyAsync({ delegate.presignDeleteObject(request) }, executor)

    override fun presignObjects(request: BatchPresignRequest): CompletableFuture<BatchPresignResponse> =
        CompletableFuture.supplyAsync({ delegate.presignObjects(request) }, executor)
}

fun ObjectStore.asAsync(
    executor: Executor = ForkJoinPool.commonPool(),
): AsyncObjectStore {
    return DefaultAsyncObjectStore(this, executor)
}
