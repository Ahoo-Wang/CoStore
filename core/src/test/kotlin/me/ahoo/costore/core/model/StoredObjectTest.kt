package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.time.Instant

class StoredObjectTest {
    @Test
    fun `should create StoredObjectMetadata instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val contentLength = 1024L
        val contentType = "application/json"
        val lastModified = Instant.now()
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val metadata = mapOf("x-custom" to "value")

        val instance =
            object : StoredObjectMetadata {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val contentLength: Long = contentLength
                override val contentType: String? = contentType
                override val lastModified: Instant? = lastModified
                override val eTag: String? = eTag
                override val metadata: Map<String, String> = metadata
            }

        instance.bucket.assert().isEqualTo(bucket)
        instance.key.assert().isEqualTo(key)
        instance.contentLength.assert().isEqualTo(contentLength)
        instance.contentType.assert().isEqualTo(contentType)
        instance.lastModified.assert().isEqualTo(lastModified)
        instance.eTag.assert().isEqualTo(eTag)
        instance.metadata.assert().isEqualTo(metadata)
    }

    @Test
    fun `should create StoredObject instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val contentLength = 1024L
        val contentType = "application/json"
        val lastModified = Instant.now()
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val metadata = mapOf("x-custom" to "value")
        val content: InputStream = "test content".byteInputStream()

        val instance =
            object : StoredObject {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val contentLength: Long = contentLength
                override val contentType: String? = contentType
                override val lastModified: Instant? = lastModified
                override val eTag: String? = eTag
                override val metadata: Map<String, String> = metadata
                override val content: InputStream = content
            }

        instance.bucket.assert().isEqualTo(bucket)
        instance.key.assert().isEqualTo(key)
        instance.contentLength.assert().isEqualTo(contentLength)
        instance.contentType.assert().isEqualTo(contentType)
        instance.lastModified.assert().isEqualTo(lastModified)
        instance.eTag.assert().isEqualTo(eTag)
        instance.metadata.assert().isEqualTo(metadata)
    }
}
