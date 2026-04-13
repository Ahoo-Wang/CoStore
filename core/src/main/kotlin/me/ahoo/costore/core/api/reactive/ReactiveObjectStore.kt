package me.ahoo.costore.core.api.reactive

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

interface ReactiveObjectStore :
    ReactiveGetObjectOperations,
    ReactivePutObjectOperations,
    ReactiveDeleteObjectOperations,
    ReactiveListObjectsOperations,
    ReactiveHeadObjectOperations,
    ReactivePresignObjectOperations {
    fun close(): Mono<Void>
}

interface ReactiveGetObjectOperations {
    fun getObject(request: GetObjectRequest): Mono<GetObjectResponse>
}

interface ReactivePutObjectOperations {
    fun putObject(request: PutObjectRequest): Mono<PutObjectResponse>
}

interface ReactiveDeleteObjectOperations {
    fun deleteObject(request: DeleteObjectRequest): Mono<DeleteObjectResponse>
}

interface ReactiveListObjectsOperations {
    fun listObjects(request: ListObjectsRequest): Mono<ListObjectsResponse>
}

interface ReactiveHeadObjectOperations {
    fun headObject(request: HeadObjectRequest): Mono<HeadObjectResponse>
}

interface ReactivePresignObjectOperations {
    fun presignGetObject(request: PresignGetObjectRequest): Mono<PresignGetObjectResponse>

    fun presignPutObject(request: PresignPutObjectRequest): Mono<PresignPutObjectResponse>

    fun presignDeleteObject(request: PresignDeleteObjectRequest): Mono<PresignDeleteObjectResponse>
}
