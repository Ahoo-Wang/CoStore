package me.ahoo.costore.core.api.coroutines

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

interface CoroutinesObjectStore :
    CoroutinesGetObjectOperations,
    CoroutinesPutObjectOperations,
    CoroutinesDeleteObjectOperations,
    CoroutinesListObjectsOperations,
    CoroutinesHeadObjectOperations,
    CoroutinesPresignObjectOperations {
    suspend fun close()
}

interface CoroutinesGetObjectOperations {
    suspend fun getObject(request: GetObjectRequest): GetObjectResponse
}

interface CoroutinesPutObjectOperations {
    suspend fun putObject(request: PutObjectRequest): PutObjectResponse
}

interface CoroutinesDeleteObjectOperations {
    suspend fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse
}

interface CoroutinesListObjectsOperations {
    suspend fun listObjects(request: ListObjectsRequest): ListObjectsResponse
}

interface CoroutinesHeadObjectOperations {
    suspend fun headObject(request: HeadObjectRequest): HeadObjectResponse
}

interface CoroutinesPresignObjectOperations {
    suspend fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse

    suspend fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse

    suspend fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse
}
