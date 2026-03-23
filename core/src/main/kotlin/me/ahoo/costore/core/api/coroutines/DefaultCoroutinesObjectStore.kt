package me.ahoo.costore.core.api.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

class DefaultCoroutinesObjectStore(
    private val delegate: ObjectStore,
) : CoroutinesObjectStore {
    override suspend fun close() {
        withContext(Dispatchers.IO) {
            delegate.close()
        }
    }

    override suspend fun getObject(request: GetObjectRequest): GetObjectResponse =
        withContext(Dispatchers.IO) { delegate.getObject(request) }

    override suspend fun putObject(request: PutObjectRequest): PutObjectResponse =
        withContext(Dispatchers.IO) { delegate.putObject(request) }

    override suspend fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse =
        withContext(Dispatchers.IO) {
            delegate.deleteObject(request)
        }

    override suspend fun listObjects(request: ListObjectsRequest): ListObjectsResponse =
        withContext(Dispatchers.IO) {
            delegate.listObjects(request)
        }

    override suspend fun headObject(request: HeadObjectRequest): HeadObjectResponse =
        withContext(Dispatchers.IO) {
            delegate.headObject(request)
        }

    override suspend fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse =
        withContext(Dispatchers.IO) {
            delegate.presignGetObject(request)
        }

    override suspend fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse =
        withContext(Dispatchers.IO) {
            delegate.presignPutObject(request)
        }

    override suspend fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse =
        withContext(Dispatchers.IO) {
            delegate.presignDeleteObject(request)
        }
}

fun ObjectStore.asCoroutines(): CoroutinesObjectStore = DefaultCoroutinesObjectStore(this)
