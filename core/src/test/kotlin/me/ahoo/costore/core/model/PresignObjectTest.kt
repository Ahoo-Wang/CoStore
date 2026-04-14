package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.Duration
import java.time.Instant

class PresignObjectTest {
    @Test
    fun `should create PresignRequest_Get instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val expiration = Duration.ofHours(1)

        val request = PresignRequest.Get(
            bucket = bucket,
            key = key,
            expiration = expiration
        )

        with(request) {
            this.bucket.assert().isEqualTo(bucket)
            this.key.assert().isEqualTo(key)
            this.expiration.assert().isEqualTo(expiration)
        }
    }

    @Test
    fun `should create PresignObjectResponse_Get instance`() {
        val url = URL("https://s3.amazonaws.com/bucket/key?signature=...")
        val expiration = Instant.now()
        val headers = mapOf("Content-Type" to listOf("application/json"))

        val response = PresignObjectResponse.Get(
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
    fun `should create PresignRequest_Put instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val expiration = Duration.ofHours(1)
        val contentType = "application/json"

        val request = PresignRequest.Put(
            bucket = bucket,
            key = key,
            expiration = expiration,
            contentType = contentType
        )

        with(request) {
            this.bucket.assert().isEqualTo(bucket)
            this.key.assert().isEqualTo(key)
            this.expiration.assert().isEqualTo(expiration)
            contentType.assert().isNotNull().isEqualTo(contentType)
        }
    }

    @Test
    fun `should create PresignObjectResponse_Put instance`() {
        val url = URL("https://s3.amazonaws.com/bucket/key?signature=...")
        val expiration = Instant.now()

        val response = PresignObjectResponse.Put(
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
    fun `should create PresignRequest_Delete instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val expiration = Duration.ofHours(1)
        val versionId = "version-123"

        val request = PresignRequest.Delete(
            bucket = bucket,
            key = key,
            expiration = expiration,
            versionId = versionId
        )

        with(request) {
            this.bucket.assert().isEqualTo(bucket)
            this.key.assert().isEqualTo(key)
            this.expiration.assert().isEqualTo(expiration)
            this.versionId.assert().isNotNull().isEqualTo(versionId)
        }
    }

    @Test
    fun `should create PresignObjectResponse_Delete instance`() {
        val url = URL("https://s3.amazonaws.com/bucket/key?signature=...")
        val expiration = Instant.now()

        val response = PresignObjectResponse.Delete(
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
