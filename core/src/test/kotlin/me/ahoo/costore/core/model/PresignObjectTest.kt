package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.time.Duration

class PresignObjectTest {
    @Test
    fun `should create PresignGetObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val expiration = Duration.ofHours(1)

        val request =
            object : PresignGetObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val expiration: Duration = expiration
            }

        request.bucket.assert().isEqualTo(bucket)
        request.key.assert().isEqualTo(key)
        request.expiration.assert().isEqualTo(expiration)
    }

    @Test
    fun `should create PresignGetObjectResponse instance`() {
        val presignedUrl = "https://s3.amazonaws.com/bucket/key?signature=..."

        val response =
            object : PresignGetObjectResponse {
                override val presignedUrl: String = presignedUrl
            }

        response.presignedUrl.assert().isEqualTo(presignedUrl)
    }

    @Test
    fun `should create PresignPutObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val expiration = Duration.ofHours(1)
        val contentType = "application/json"

        val request =
            object : PresignPutObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val expiration: Duration = expiration
                override val contentType: String? = contentType
            }

        request.bucket.assert().isEqualTo(bucket)
        request.key.assert().isEqualTo(key)
        request.expiration.assert().isEqualTo(expiration)
        request.contentType.assert().isEqualTo(contentType)
    }

    @Test
    fun `should create PresignPutObjectResponse instance`() {
        val presignedUrl = "https://s3.amazonaws.com/bucket/key?signature=..."

        val response =
            object : PresignPutObjectResponse {
                override val presignedUrl: String = presignedUrl
            }

        response.presignedUrl.assert().isEqualTo(presignedUrl)
    }

    @Test
    fun `should create PresignDeleteObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val expiration = Duration.ofHours(1)

        val request =
            object : PresignDeleteObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val expiration: Duration = expiration
            }

        request.bucket.assert().isEqualTo(bucket)
        request.key.assert().isEqualTo(key)
        request.expiration.assert().isEqualTo(expiration)
    }

    @Test
    fun `should create PresignDeleteObjectResponse instance`() {
        val presignedUrl = "https://s3.amazonaws.com/bucket/key?signature=..."

        val response =
            object : PresignDeleteObjectResponse {
                override val presignedUrl: String = presignedUrl
            }

        response.presignedUrl.assert().isEqualTo(presignedUrl)
    }
}
