package me.ahoo.costore.core.api.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

class DefaultCoroutinesObjectStore(
    private val delegate: ObjectStore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CoroutinesObjectStore {
    override suspend fun close() {
        withContext(dispatcher) {
            delegate.close()
        }
    }

    override suspend fun getObject(request: GetObjectRequest): GetObjectResponse =
        withContext(dispatcher) { delegate.getObject(request) }

    override suspend fun putObject(request: PutObjectRequest): PutObjectResponse =
        withContext(dispatcher) { delegate.putObject(request) }

    override suspend fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse =
        withContext(dispatcher) {
            delegate.deleteObject(request)
        }

    override suspend fun listObjects(request: ListObjectsRequest): ListObjectsResponse =
        withContext(dispatcher) {
            delegate.listObjects(request)
        }

    override suspend fun headObject(request: HeadObjectRequest): HeadObjectResponse =
        withContext(dispatcher) {
            delegate.headObject(request)
        }

    override suspend fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse =
        withContext(dispatcher) {
            delegate.presignGetObject(request)
        }

    override suspend fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse =
        withContext(dispatcher) {
            delegate.presignPutObject(request)
        }

    override suspend fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse =
        withContext(dispatcher) {
            delegate.presignDeleteObject(request)
        }

    override suspend fun presignObjects(request: BatchPresignRequest): BatchPresignResponse =
        withContext(dispatcher) {
            delegate.presignObjects(request)
        }
}

fun ObjectStore.asCoroutines(dispatcher: CoroutineDispatcher = Dispatchers.IO): CoroutinesObjectStore =
    DefaultCoroutinesObjectStore(this, dispatcher)
