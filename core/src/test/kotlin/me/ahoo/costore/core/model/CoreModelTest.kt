package me.ahoo.costore.core.model

import me.ahoo.costore.core.exception.ObjectNotFoundException
import me.ahoo.costore.core.exception.StorageException
import me.ahoo.test.asserts.assert
import me.ahoo.test.asserts.assertThrownBy
import java.time.Instant
import kotlin.test.Test

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
        policy.bucket.assert().isEqualTo("my-bucket")
        policy.keyPrefix.assert().isEqualTo("uploads/")
        policy.maxContentLength.assert().isEqualTo(5 * 1024 * 1024)
        policy.allowedContentTypes.assert().isEqualTo(listOf("image/jpeg", "image/png"))
        policy.expireSeconds.assert().isEqualTo(600)
    }

    @Test
    fun `valid policy with only required bucket`() {
        val policy = UploadPolicy(bucket = "bucket")
        policy.keyPrefix.assert().isNull()
        policy.maxContentLength.assert().isNull()
        policy.allowedContentTypes.assert().isNull()
        policy.expireSeconds.assert().isEqualTo(3600)
    }

    @Test
    fun `expiresAt is in the future`() {
        val before = Instant.now()
        val policy = UploadPolicy(bucket = "bucket", expireSeconds = 60)
        val expiresAt = policy.expiresAt()
        expiresAt.assert().isAfter(before)
        expiresAt.assert().isBefore(before.plusSeconds(120))
    }

    @Test
    fun `blank bucket throws IllegalArgumentException`() {
        assertThrownBy<IllegalArgumentException> {
            UploadPolicy(bucket = "  ")
        }
    }

    @Test
    fun `zero expireSeconds throws IllegalArgumentException`() {
        assertThrownBy<IllegalArgumentException> {
            UploadPolicy(bucket = "bucket", expireSeconds = 0)
        }
    }

    @Test
    fun `negative expireSeconds throws IllegalArgumentException`() {
        assertThrownBy<IllegalArgumentException> {
            UploadPolicy(bucket = "bucket", expireSeconds = -1)
        }
    }

    @Test
    fun `zero maxContentLength throws IllegalArgumentException`() {
        assertThrownBy<IllegalArgumentException> {
            UploadPolicy(bucket = "bucket", maxContentLength = 0)
        }
    }

    @Test
    fun `negative maxContentLength throws IllegalArgumentException`() {
        assertThrownBy<IllegalArgumentException> {
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
        token.method.assert().isEqualTo(HttpMethod.PUT)
        token.fields.assert().isEmpty()
        token.headers["Content-Type"].assert().isEqualTo("image/jpeg")
        token.expiresAt.assert().isEqualTo(expiresAt)
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
        token.method.assert().isEqualTo(HttpMethod.POST)
        token.headers.assert().isEmpty()
        token.fields["OSSAccessKeyId"].assert().isEqualTo("ACCESS_KEY")
    }
}

class StorageExceptionTest {

    @Test
    fun `StorageException carries message and cause`() {
        val cause = RuntimeException("network error")
        val ex = StorageException("upload failed", cause)
        ex.message.assert().isEqualTo("upload failed")
        ex.cause.assert().isEqualTo(cause)
    }

    @Test
    fun `ObjectNotFoundException formats message correctly`() {
        val ex = ObjectNotFoundException(bucket = "my-bucket", key = "path/to/file.jpg")
        ex.message.assert().isNotNull().contains("my-bucket").contains("path/to/file.jpg")
        ex.bucket.assert().isEqualTo("my-bucket")
        ex.key.assert().isEqualTo("path/to/file.jpg")
        ex.cause.assert().isNull()
    }

    @Test
    fun `ObjectNotFoundException is a StorageException`() {
        val ex: Any = ObjectNotFoundException(bucket = "b", key = "k")
        ex.assert().isInstanceOf(StorageException::class.java)
    }
}

class ListObjectsRequestTest {

    @Test
    fun `default values are applied`() {
        val req = ListObjectsRequest(bucket = "bucket")
        req.prefix.assert().isNull()
        req.delimiter.assert().isNull()
        req.maxKeys.assert().isEqualTo(1000)
        req.continuationToken.assert().isNull()
    }
}
