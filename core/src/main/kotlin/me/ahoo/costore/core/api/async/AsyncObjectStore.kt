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

interface AsyncObjectStore :
    AsyncGetObjectOperations,
    AsyncPutObjectOperations,
    AsyncDeleteObjectOperations,
    AsyncListObjectsOperations,
    AsyncHeadObjectOperations,
    AsyncPresignObjectOperations {
    fun close(): CompletableFuture<Void>
}

interface AsyncGetObjectOperations {
    fun getObject(request: GetObjectRequest): CompletableFuture<GetObjectResponse>
}

interface AsyncPutObjectOperations {
    fun putObject(request: PutObjectRequest): CompletableFuture<PutObjectResponse>
}

interface AsyncDeleteObjectOperations {
    fun deleteObject(request: DeleteObjectRequest): CompletableFuture<DeleteObjectResponse>
}

interface AsyncListObjectsOperations {
    fun listObjects(request: ListObjectsRequest): CompletableFuture<ListObjectsResponse>
}

interface AsyncHeadObjectOperations {
    fun headObject(request: HeadObjectRequest): CompletableFuture<HeadObjectResponse>
}

interface AsyncPresignObjectOperations {
    fun presignGetObject(request: PresignGetObjectRequest): CompletableFuture<PresignGetObjectResponse>

    fun presignPutObject(request: PresignPutObjectRequest): CompletableFuture<PresignPutObjectResponse>

    fun presignDeleteObject(request: PresignDeleteObjectRequest): CompletableFuture<PresignDeleteObjectResponse>
}
