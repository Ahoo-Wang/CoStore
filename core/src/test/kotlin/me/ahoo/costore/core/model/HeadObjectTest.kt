package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.time.Instant

class HeadObjectTest {
    @Test
    fun `should create HeadObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"

        val request = HeadObjectRequest(
            bucket = bucket,
            key = key
        )

        with(request) {
            this.bucket.assert().isEqualTo(bucket)
            this.key.assert().isEqualTo(key)
        }
    }

    @Test
    fun `should create HeadObjectResponse instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val contentLength = 1024L
        val contentType = "application/json"
        val lastModified = Instant.now()
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val metadata = mapOf("x-custom" to "value")

        val response = StoredObjectMetadata(
            bucket = bucket,
            key = key,
            contentLength = contentLength,
            contentType = contentType,
            lastModified = lastModified,
            eTag = eTag,
            metadata = metadata
        )

        with(response) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            contentLength.assert().isEqualTo(contentLength)
            contentType.assert().isNotNull().isEqualTo(contentType)
            lastModified.assert().isNotNull().isEqualTo(lastModified)
            eTag.assert().isNotNull().isEqualTo(eTag)
            this.metadata.assert().isEqualTo(metadata)
        }
    }
}
