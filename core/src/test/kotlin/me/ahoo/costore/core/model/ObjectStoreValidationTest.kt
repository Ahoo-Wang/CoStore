package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ObjectStoreValidationTest {

    @Test
    fun `validateBucketName with valid bucket`() {
        ObjectStoreValidation.validateBucketName("valid-bucket")
        ObjectStoreValidation.validateBucketName("valid.bucket")
        ObjectStoreValidation.validateBucketName("valid_bucket")
        ObjectStoreValidation.validateBucketName("bucket123")
    }

    @Test
    fun `validateBucketName should throw for blank bucket`() {
        assertThrows<IllegalArgumentException> {
            ObjectStoreValidation.validateBucketName("")
        }.also { it.message.assert().contains("blank")
        }
    }

    @Test
    fun `validateBucketName should throw for bucket with newline`() {
        assertThrows<IllegalArgumentException> {
            ObjectStoreValidation.validateBucketName("bucket\nname")
        }.also { it.message.assert().contains("newline")
        }
    }

    @Test
    fun `validateBucketName should throw for bucket with carriage return`() {
        assertThrows<IllegalArgumentException> {
            ObjectStoreValidation.validateBucketName("bucket\rname")
        }.also { it.message.assert().contains("carriage return")
        }
    }

    @Test
    fun `validateBucketName should throw for bucket with tab`() {
        assertThrows<IllegalArgumentException> {
            ObjectStoreValidation.validateBucketName("bucket\tname")
        }.also { it.message.assert().contains("tab")
        }
    }

    @Test
    fun `validateObjectKey with valid key`() {
        ObjectStoreValidation.validateObjectKey("valid/key")
        ObjectStoreValidation.validateObjectKey("valid.key")
        ObjectStoreValidation.validateObjectKey("valid_key")
        ObjectStoreValidation.validateObjectKey("key/with/multiple/segments")
    }

    @Test
    fun `validateObjectKey should throw for blank key`() {
        assertThrows<IllegalArgumentException> {
            ObjectStoreValidation.validateObjectKey("")
        }.also { it.message.assert().contains("blank")
        }
    }

    @Test
    fun `validateObjectKey should throw for key with newline`() {
        assertThrows<IllegalArgumentException> {
            ObjectStoreValidation.validateObjectKey("key\nname")
        }.also { it.message.assert().contains("newline")
        }
    }

    @Test
    fun `validateObjectKey should throw for key with carriage return`() {
        assertThrows<IllegalArgumentException> {
            ObjectStoreValidation.validateObjectKey("key\rname")
        }.also { it.message.assert().contains("carriage return")
        }
    }

    @Test
    fun `validateObjectKey should throw for key with tab`() {
        assertThrows<IllegalArgumentException> {
            ObjectStoreValidation.validateObjectKey("key\tname")
        }.also { it.message.assert().contains("tab")
        }
    }
}