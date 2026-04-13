package me.ahoo.costore.core.error

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class CoStoreErrorTest {
    @Test
    fun `should create CoStoreError with message`() {
        val error = CoStoreError("test error")
        error.message.assert().isEqualTo("test error")
        error.cause.assert().isNull()
    }

    @Test
    fun `should create CoStoreError with message and cause`() {
        val cause = RuntimeException("original error")
        val error = CoStoreError("test error", cause)
        error.message.assert().isEqualTo("test error")
        error.cause.assert().isSameAs(cause)
    }

    @Test
    fun `should create ObjectNotFoundError`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val error = ObjectNotFoundError(bucket, key)

        error.bucket.assert().isEqualTo(bucket)
        error.key.assert().isEqualTo(key)
        error.message.assert().isEqualTo("Object not found: bucket='$bucket', key='$key'")
    }

    @Test
    fun `should create ObjectNotFoundError with custom message`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val customMessage = "custom not found message"
        val error = ObjectNotFoundError(bucket, key, customMessage)

        error.bucket.assert().isEqualTo(bucket)
        error.key.assert().isEqualTo(key)
        error.message.assert().isEqualTo(customMessage)
    }
}
