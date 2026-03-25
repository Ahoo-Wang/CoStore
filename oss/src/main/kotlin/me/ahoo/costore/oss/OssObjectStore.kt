package me.ahoo.costore.oss

import com.aliyun.oss.OSS
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

class OssObjectStore(private val client: OSS) : ObjectStore {
    override fun getObject(request: GetObjectRequest): GetObjectResponse {
        TODO("Not yet implemented")
    }

    override fun headObject(request: HeadObjectRequest): HeadObjectResponse {
        TODO("Not yet implemented")
    }

    override fun putObject(request: PutObjectRequest): PutObjectResponse {
        TODO("Not yet implemented")
    }

    override fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse {
        TODO("Not yet implemented")
    }

    override fun listObjects(request: ListObjectsRequest): ListObjectsResponse {
        TODO("Not yet implemented")
    }

    override fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse {
        TODO("Not yet implemented")
    }

    override fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse {
        TODO("Not yet implemented")
    }

    override fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}