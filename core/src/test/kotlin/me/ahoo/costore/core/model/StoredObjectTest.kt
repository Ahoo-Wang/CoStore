package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.io.InputStream

class StoredObjectTest {
    @Test
    fun `should create StoredObjectMetadata instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val metadata = mapOf("content-type" to "application/json")
        val instance =
            object : StoredObjectMetadata {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val metadata: Map<String, String> = metadata
            }
        instance.bucket.assert().isEqualTo(bucket)
        instance.key.assert().isEqualTo(key)
        instance.metadata.assert().isEqualTo(metadata)
    }

    @Test
    fun `should create StoredObject instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val metadata = mapOf("content-type" to "application/json")
        val content = "test content".byteInputStream()
        val instance =
            object : StoredObject {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val metadata: Map<String, String> = metadata
                override val content: InputStream = content
            }
        instance.bucket.assert().isEqualTo(bucket)
        instance.key.assert().isEqualTo(key)
        instance.metadata.assert().isEqualTo(metadata)
    }
}
