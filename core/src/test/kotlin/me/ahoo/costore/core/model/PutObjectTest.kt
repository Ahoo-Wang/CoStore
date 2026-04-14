package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.io.InputStream

class PutObjectTest {
    @Test
    fun `should create PutObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val content: InputStream = "test content".byteInputStream()
        val contentLength = "test content".toByteArray().size.toLong()
        val contentType = "application/json"
        val metadata = mapOf("x-custom" to "value")

        val request = PutObjectRequest(
            bucket = bucket,
            key = key,
            content = content,
            contentLength = contentLength,
            contentType = contentType,
            metadata = metadata
        )

        with(request) {
            this.bucket.assert().isEqualTo(bucket)
            this.key.assert().isEqualTo(key)
            this.contentLength.assert().isEqualTo(contentLength)
            contentType.assert().isNotNull().isEqualTo(contentType)
            this.metadata.assert().isEqualTo(metadata)
        }
    }

    @Test
    fun `should create PutObjectResponse instance`() {
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val versionId = "version-123"

        val response = PutObjectResponse(
            eTag = eTag,
            versionId = versionId
        )

        with(response) {
            this.eTag.assert().isNotNull().isEqualTo(eTag)
            this.versionId.assert().isNotNull().isEqualTo(versionId)
        }
    }
}
