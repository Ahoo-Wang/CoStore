package me.ahoo.costore.core.api.sync

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

interface ObjectStore :
    GetObjectOperations,
    HeadObjectOperations,
    PutObjectOperations,
    DeleteObjectOperations,
    ListObjectsOperations,
    PresignObjectOperations,
    AutoCloseable

interface DeleteObjectOperations {
    fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse
}

interface GetObjectOperations {
    fun getObject(request: GetObjectRequest): GetObjectResponse
}

interface HeadObjectOperations {
    fun headObject(request: HeadObjectRequest): HeadObjectResponse
}

interface ListObjectsOperations {
    fun listObjects(request: ListObjectsRequest): ListObjectsResponse
}

interface PutObjectOperations {
    fun putObject(request: PutObjectRequest): PutObjectResponse
}

interface PresignObjectOperations {
    fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse
    fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse
    fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse
}
