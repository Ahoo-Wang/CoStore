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
        val range = 0L..1023L
        val ifModifiedSince = Instant.now()
        val ifNoneMatch = "\"etag-value\""

        val request =
            object : GetObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val range: LongRange? = range
                override val ifModifiedSince: Instant? = ifModifiedSince
                override val ifNoneMatch: String? = ifNoneMatch
            }

        request.bucket.assert().isEqualTo(bucket)
        request.key.assert().isEqualTo(key)
        request.range.assert().isEqualTo(range)
        request.ifModifiedSince.assert().isEqualTo(ifModifiedSince)
        request.ifNoneMatch.assert().isEqualTo(ifNoneMatch)
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
        val metadata = mapOf("x-custom" to "value")

        val response =
            object : GetObjectResponse {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val metadata: Map<String, String> = metadata
                override val content: InputStream = content
                override val contentLength: Long = contentLength
                override val contentType: String? = contentType
                override val lastModified: Instant? = lastModified
                override val eTag: String? = eTag
            }

        response.bucket.assert().isEqualTo(bucket)
        response.key.assert().isEqualTo(key)
        response.metadata.assert().isEqualTo(metadata)
        response.contentLength.assert().isEqualTo(contentLength)
        response.contentType.assert().isEqualTo(contentType)
        response.lastModified.assert().isEqualTo(lastModified)
        response.eTag.assert().isEqualTo(eTag)
    }
}
