package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.Duration
import java.time.Instant

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

        with(request) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            this.expiration.assert().isEqualTo(expiration)
        }
    }

    @Test
    fun `should create PresignGetObjectResponse instance`() {
        val url = URL("https://s3.amazonaws.com/bucket/key?signature=...")
        val expiration = Instant.now()
        val headers = mapOf("Content-Type" to listOf("application/json"))

        val response = DefaultPresignGetObjectResponse(
            url = url,
            expiration = expiration,
            headers = headers
        )

        with(response) {
            this.url.assert().isEqualTo(url)
            this.expiration.assert().isEqualTo(expiration)
            headers.assert().hasSize(1).containsKey("Content-Type")
        }
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

        with(request) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            this.expiration.assert().isEqualTo(expiration)
            contentType.assert().isNotNull().isEqualTo(contentType)
        }
    }

    @Test
    fun `should create PresignPutObjectResponse instance`() {
        val url = URL("https://s3.amazonaws.com/bucket/key?signature=...")
        val expiration = Instant.now()

        val response = DefaultPresignPutObjectResponse(
            url = url,
            expiration = expiration,
            headers = emptyMap()
        )

        with(response) {
            this.url.assert().isEqualTo(url)
            this.expiration.assert().isEqualTo(expiration)
            headers.assert().isEmpty()
        }
    }

    @Test
    fun `should create PresignDeleteObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val expiration = Duration.ofHours(1)
        val versionId = "version-123"

        val request =
            object : PresignDeleteObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val expiration: Duration = expiration
                override val versionId: String? = versionId
            }

        with(request) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            this.expiration.assert().isEqualTo(expiration)
            this.versionId.assert().isNotNull().isEqualTo(versionId)
        }
    }

    @Test
    fun `should create PresignDeleteObjectResponse instance`() {
        val url = URL("https://s3.amazonaws.com/bucket/key?signature=...")
        val expiration = Instant.now()

        val response = DefaultPresignDeleteObjectResponse(
            url = url,
            expiration = expiration,
            headers = emptyMap()
        )

        with(response) {
            this.url.assert().isEqualTo(url)
            this.expiration.assert().isEqualTo(expiration)
            headers.assert().isEmpty()
        }
    }
}
