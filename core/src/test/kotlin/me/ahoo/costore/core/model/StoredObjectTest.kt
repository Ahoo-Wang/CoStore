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

        val instance = DefaultStoredObjectMetadata(
            bucket = bucket,
            key = key,
            contentLength = contentLength,
            contentType = contentType,
            lastModified = lastModified,
            eTag = eTag,
            metadata = metadata
        )

        with(instance) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            contentLength.assert().isEqualTo(contentLength)
            contentType.assert().isEqualTo(contentType)
            lastModified.assert().isNotNull().isEqualTo(lastModified)
            eTag.assert().isNotNull().isEqualTo(eTag)
            this.metadata.assert().isEqualTo(metadata)
        }
    }

    @Test
    fun `should create StoredObject instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val contentLength = 1024L
        val contentType = "application/json"
        val lastModified = Instant.now()
        val eTag = "\"d41d8cd98f00b204e9800998ecf8427e\""
        val userMetadata = mapOf("x-custom" to "value")
        val content: InputStream = "test content".byteInputStream()

        val storedMetadata = DefaultStoredObjectMetadata(
            bucket = bucket,
            key = key,
            contentLength = contentLength,
            contentType = contentType,
            lastModified = lastModified,
            eTag = eTag,
            metadata = userMetadata
        )

        val instance = DefaultStoredObject(
            content = content,
            metadata = storedMetadata
        )

        with(instance.metadata) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            contentLength.assert().isEqualTo(contentLength)
            contentType.assert().isEqualTo(contentType)
            lastModified.assert().isNotNull().isEqualTo(lastModified)
            eTag.assert().isNotNull().isEqualTo(eTag)
            this.metadata.assert().isEqualTo(userMetadata)
        }
    }
}
