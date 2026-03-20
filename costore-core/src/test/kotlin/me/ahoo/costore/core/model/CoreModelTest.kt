package me.ahoo.costore.core.model

import me.ahoo.costore.core.exception.ObjectNotFoundException
import me.ahoo.costore.core.exception.StorageException
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UploadPolicyTest {

    @Test
    fun `valid policy with all fields`() {
        val policy = UploadPolicy(
            bucket = "my-bucket",
            keyPrefix = "uploads/",
            maxContentLength = 5 * 1024 * 1024,
            allowedContentTypes = listOf("image/jpeg", "image/png"),
            expireSeconds = 600,
        )
        assertEquals("my-bucket", policy.bucket)
        assertEquals("uploads/", policy.keyPrefix)
        assertEquals(5 * 1024 * 1024, policy.maxContentLength)
        assertEquals(listOf("image/jpeg", "image/png"), policy.allowedContentTypes)
        assertEquals(600, policy.expireSeconds)
    }

    @Test
    fun `valid policy with only required bucket`() {
        val policy = UploadPolicy(bucket = "bucket")
        assertNull(policy.keyPrefix)
        assertNull(policy.maxContentLength)
        assertNull(policy.allowedContentTypes)
        assertEquals(3600, policy.expireSeconds)
    }

    @Test
    fun `expiresAt is in the future`() {
        val before = Instant.now()
        val policy = UploadPolicy(bucket = "bucket", expireSeconds = 60)
        val expiresAt = policy.expiresAt()
        assertTrue(expiresAt.isAfter(before))
        assertTrue(expiresAt.isBefore(before.plusSeconds(120)))
    }

    @Test
    fun `blank bucket throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            UploadPolicy(bucket = "  ")
        }
    }

    @Test
    fun `zero expireSeconds throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            UploadPolicy(bucket = "bucket", expireSeconds = 0)
        }
    }

    @Test
    fun `negative expireSeconds throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            UploadPolicy(bucket = "bucket", expireSeconds = -1)
        }
    }

    @Test
    fun `zero maxContentLength throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            UploadPolicy(bucket = "bucket", maxContentLength = 0)
        }
    }

    @Test
    fun `negative maxContentLength throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            UploadPolicy(bucket = "bucket", maxContentLength = -100)
        }
    }
}

class UploadTokenTest {

    @Test
    fun `PUT token has expected fields`() {
        val expiresAt = Instant.now().plusSeconds(3600)
        val token = UploadToken(
            uploadUrl = "https://bucket.s3.amazonaws.com/key?X-Amz-Signature=abc",
            method = HttpMethod.PUT,
            headers = mapOf("Content-Type" to "image/jpeg"),
            expiresAt = expiresAt,
        )
        assertEquals(HttpMethod.PUT, token.method)
        assertTrue(token.fields.isEmpty())
        assertEquals("image/jpeg", token.headers["Content-Type"])
        assertEquals(expiresAt, token.expiresAt)
    }

    @Test
    fun `POST token has expected fields`() {
        val expiresAt = Instant.now().plusSeconds(3600)
        val token = UploadToken(
            uploadUrl = "https://bucket.oss-cn-hangzhou.aliyuncs.com",
            method = HttpMethod.POST,
            fields = mapOf(
                "OSSAccessKeyId" to "ACCESS_KEY",
                "policy" to "BASE64_POLICY",
                "Signature" to "SIG",
                "key" to "uploads/file.jpg",
            ),
            expiresAt = expiresAt,
        )
        assertEquals(HttpMethod.POST, token.method)
        assertTrue(token.headers.isEmpty())
        assertEquals("ACCESS_KEY", token.fields["OSSAccessKeyId"])
    }
}

class StorageExceptionTest {

    @Test
    fun `StorageException carries message and cause`() {
        val cause = RuntimeException("network error")
        val ex = StorageException("upload failed", cause)
        assertEquals("upload failed", ex.message)
        assertEquals(cause, ex.cause)
    }

    @Test
    fun `ObjectNotFoundException formats message correctly`() {
        val ex = ObjectNotFoundException(bucket = "my-bucket", key = "path/to/file.jpg")
        assertNotNull(ex.message)
        assertTrue(ex.message!!.contains("my-bucket"))
        assertTrue(ex.message!!.contains("path/to/file.jpg"))
        assertEquals("my-bucket", ex.bucket)
        assertEquals("path/to/file.jpg", ex.key)
        assertNull(ex.cause)
    }

    @Test
    fun `ObjectNotFoundException is a StorageException`() {
        val ex: Any = ObjectNotFoundException(bucket = "b", key = "k")
        assertTrue(ex is StorageException)
    }
}

class ListObjectsRequestTest {

    @Test
    fun `default values are applied`() {
        val req = ListObjectsRequest(bucket = "bucket")
        assertNull(req.prefix)
        assertNull(req.delimiter)
        assertEquals(1000, req.maxKeys)
        assertNull(req.continuationToken)
    }
}
