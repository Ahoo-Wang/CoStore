package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.time.Instant

class PutObjectTest {
    @Test
    fun `should create PutObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val content: InputStream = "test content".byteInputStream()
        val contentType = "application/json"
        val metadata = mapOf("x-custom" to "value")

        val request =
            object : PutObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val content: InputStream = content
                override val contentType: String? = contentType
                override val metadata: Map<String, String> = metadata
            }

        with(request) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            contentType.assert().isNotNull().isEqualTo(contentType)
            this.metadata.assert().isEqualTo(metadata)
        }
    }

    @Test
    fun `should create PutObjectResponse instance`() {
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val versionId = "version-123"
        val lastModified = Instant.now()

        val response = DefaultPutObjectResponse(
            eTag = eTag,
            versionId = versionId,
            lastModified = lastModified
        )

        with(response) {
            eTag.assert().isNotNull().isEqualTo(eTag)
            this.versionId.assert().isNotNull().isEqualTo(versionId)
            this.lastModified.assert().isNotNull().isEqualTo(lastModified)
        }
    }
}
