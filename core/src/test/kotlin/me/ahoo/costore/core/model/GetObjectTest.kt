package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.time.Instant

class GetObjectTest {
    @Test
    fun `should create GetObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val contentType = "application/json"
        val versionId = "version-123"

        val request = GetObjectRequest(
            bucket = bucket,
            key = key,
            contentType = contentType,
            versionId = versionId
        )

        with(request) {
            this.bucket.assert().isEqualTo(bucket)
            this.key.assert().isEqualTo(key)
            contentType.assert().isNotNull().isEqualTo(contentType)
            this.versionId.assert().isNotNull().isEqualTo(versionId)
        }
    }

    @Test
    fun `should create GetObjectResponse instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val content: InputStream = "test content".byteInputStream()
        val contentLength = 12L
        val contentType = "text/plain"
        val lastModified = Instant.now()
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val userMetadata = mapOf("x-custom" to "value")

        val storedMetadata = StoredObjectMetadata(
            bucket = bucket,
            key = key,
            contentLength = contentLength,
            contentType = contentType,
            lastModified = lastModified,
            eTag = eTag,
            metadata = userMetadata
        )

        val response = StoredObject(
            content = content,
            metadata = storedMetadata
        )

        with(response.metadata) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            contentLength.assert().isEqualTo(contentLength)
            contentType.assert().isNotNull().isEqualTo(contentType)
            lastModified.assert().isNotNull().isEqualTo(lastModified)
            eTag.assert().isNotNull().isEqualTo(eTag)
            metadata.assert().isEqualTo(userMetadata)
        }
    }
}
