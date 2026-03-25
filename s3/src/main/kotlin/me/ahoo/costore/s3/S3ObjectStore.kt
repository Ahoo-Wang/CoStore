package me.ahoo.costore.s3

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
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

class S3ObjectStore(private val client: S3Client, private val presigner: S3Presigner) : ObjectStore {
    override fun getObject(request: GetObjectRequest): GetObjectResponse {
        val getObjectResponse = client.getObject {
            it.bucket(request.bucket)
                .key(request.key)
                .responseContentType(request.contentType)
                .versionId(request.versionId)
        }
        val contentLength = getObjectResponse.response().expires()
        TODO("Not yet implemented")
    }

    override fun headObject(request: HeadObjectRequest): HeadObjectResponse {
        val headObjectResponse = client.headObject {
            it.bucket(request.bucket)
                .key(request.key)
        }
        TODO("Not yet implemented")
    }

    override fun putObject(request: PutObjectRequest): PutObjectResponse {

        TODO("Not yet implemented")
    }

    override fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse {
        val deleteObjectResponse = client.deleteObject {
            it.bucket(request.bucket)
                .key(request.key)
                .versionId(request.versionId)
        }
        val deleteMarker = deleteObjectResponse.deleteMarker()
        TODO("Not yet implemented")
    }

    override fun listObjects(request: ListObjectsRequest): ListObjectsResponse {
        val listObjectsResponse = client.listObjects {
            it.bucket(request.bucket)
                .prefix(request.prefix)
                .delimiter(request.delimiter)
                .marker(request.marker)
                .maxKeys(request.maxKeys)
        }
        TODO("Not yet implemented")
    }

    override fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse {
        val presignedGetObject = presigner.presignGetObject { builder ->
            builder.signatureDuration(request.expiration)
                .getObjectRequest {
                    it.bucket(request.bucket)
                        .key(request.key)
                }
        }
        TODO("Not yet implemented")
    }

    override fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse {
        val presignedPutObject = presigner.presignPutObject { builder ->
            builder.signatureDuration(request.expiration)
                .putObjectRequest {
                    it.bucket(request.bucket)
                        .key(request.key)
                        .contentType(request.contentType)
                }
        }
        TODO("Not yet implemented")
    }

    override fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse {
        val presignedDeleteObject = presigner.presignDeleteObject { builder ->
            builder.signatureDuration(request.expiration)
                .deleteObjectRequest {
                    it.bucket(request.bucket)
                        .key(request.key)
                        .versionId(request.versionId)
                }
        }
        val url = presignedDeleteObject.url()
        TODO("Not yet implemented")
    }

    override fun close() {
        client.close()
    }
}