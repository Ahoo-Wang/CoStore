package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.Closeable
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

        val instance = StoredObjectMetadata(
            bucket = bucket,
            key = key,
            contentLength = contentLength,
            contentType = contentType,
            lastModified = lastModified,
            eTag = eTag,
            metadata = metadata
        )

        with(instance) {
            this.bucket.assert().isEqualTo(bucket)
            this.key.assert().isEqualTo(key)
            contentLength.assert().isEqualTo(contentLength)
            this.contentType.assert().isEqualTo(contentType)
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

        val storedMetadata = StoredObjectMetadata(
            bucket = bucket,
            key = key,
            contentLength = contentLength,
            contentType = contentType,
            lastModified = lastModified,
            eTag = eTag,
            metadata = userMetadata
        )

        val instance = StoredObject(
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
            metadata.assert().isEqualTo(userMetadata)
        }
    }

    @Test
    fun `StoredObject should implement Closeable`() {
        val metadata = StoredObjectMetadata(
            bucket = "test-bucket",
            key = "test/key"
        )
        val content = ByteArrayInputStream("test content".toByteArray())

        val instance = StoredObject(
            content = content,
            metadata = metadata
        )

        instance.assert().isInstanceOf(AutoCloseable::class.java)
        instance.assert().isInstanceOf(java.io.Closeable::class.java)
    }

    @Test
    fun `StoredObject close should close content stream`() {
        val metadata = StoredObjectMetadata(
            bucket = "test-bucket",
            key = "test/key"
        )
        val content = ByteArrayInputStream("test content".toByteArray())

        val instance = StoredObject(
            content = content,
            metadata = metadata
        )

        instance.close()

        // Verify close was called - content implements Closeable
        (instance.content is Closeable).assert().isTrue()
    }

    @Test
    fun `StoredObject should work with use extension`() {
        val metadata = StoredObjectMetadata(
            bucket = "test-bucket",
            key = "test/key"
        )

        val bytes = metadata.bucket.byteInputStream().use { inputStream ->
            inputStream.readAllBytes()
        }

        bytes.assert().isEqualTo("test-bucket".toByteArray())
    }
}
