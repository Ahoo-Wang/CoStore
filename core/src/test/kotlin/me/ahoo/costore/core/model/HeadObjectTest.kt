package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.time.Instant

class HeadObjectTest {
    @Test
    fun `should create HeadObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"

        val request =
            object : HeadObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
            }

        request.bucket.assert().isEqualTo(bucket)
        request.key.assert().isEqualTo(key)
    }

    @Test
    fun `should create HeadObjectResponse instance`() {
        val contentLength = 1024L
        val contentType = "application/json"
        val lastModified = Instant.now()
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val metadata = mapOf("x-custom" to "value")

        val response =
            object : HeadObjectResponse {
                override val contentLength: Long = contentLength
                override val contentType: String? = contentType
                override val lastModified: Instant? = lastModified
                override val eTag: String? = eTag
                override val metadata: Map<String, String> = metadata
            }

        response.contentLength.assert().isEqualTo(contentLength)
        response.contentType.assert().isEqualTo(contentType)
        response.lastModified.assert().isEqualTo(lastModified)
        response.eTag.assert().isEqualTo(eTag)
        response.metadata.assert().isEqualTo(metadata)
    }
}
