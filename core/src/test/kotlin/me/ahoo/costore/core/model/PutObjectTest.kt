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
        val storageClass = "STANDARD"

        val request =
            object : PutObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val content: InputStream = content
                override val contentType: String? = contentType
                override val metadata: Map<String, String> = metadata
                override val storageClass: String? = storageClass
            }

        request.bucket.assert().isEqualTo(bucket)
        request.key.assert().isEqualTo(key)
        request.contentType.assert().isEqualTo(contentType)
        request.metadata.assert().isEqualTo(metadata)
        request.storageClass.assert().isEqualTo(storageClass)
    }

    @Test
    fun `should create PutObjectResponse instance`() {
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val versionId = "version-123"
        val lastModified = Instant.now()

        val response =
            object : PutObjectResponse {
                override val eTag: String? = eTag
                override val versionId: String? = versionId
                override val lastModified: Instant? = lastModified
            }

        response.eTag.assert().isEqualTo(eTag)
        response.versionId.assert().isEqualTo(versionId)
        response.lastModified.assert().isEqualTo(lastModified)
    }
}
