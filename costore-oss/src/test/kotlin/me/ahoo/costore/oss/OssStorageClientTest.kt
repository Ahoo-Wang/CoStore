package me.ahoo.costore.oss

import com.aliyun.oss.OSSClient
import com.aliyun.oss.OSSException
import com.aliyun.oss.model.GetObjectRequest as OssGetObjectRequest
import com.aliyun.oss.model.ListObjectsV2Request as OssListObjectsV2Request
import com.aliyun.oss.model.ListObjectsV2Result
import com.aliyun.oss.model.OSSObject
import com.aliyun.oss.model.OSSObjectSummary
import com.aliyun.oss.model.ObjectMetadata
import com.aliyun.oss.model.PutObjectResult
import com.aliyun.oss.model.VoidResult
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import me.ahoo.costore.core.exception.ObjectNotFoundException
import me.ahoo.costore.core.exception.StorageException
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.HttpMethod
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.UploadPolicy
import java.io.ByteArrayInputStream
import java.net.URI
import java.net.URL
import java.time.Instant
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OssStorageClientTest {

    private val ossClient: OSSClient = mockk()
    private val client = OssStorageClient(ossClient, "ACCESS_KEY")

    // ── putObject ────────────────────────────────────────────────────────────

    @Test
    fun `putObject returns eTag on success`() {
        val result = mockk<PutObjectResult> { every { eTag } returns "abc123" }
        every { ossClient.putObject(any<String>(), any<String>(), any<java.io.InputStream>(), any<ObjectMetadata>()) } returns result

        val response = client.putObject(
            PutObjectRequest(
                bucket = "bucket",
                key = "key.txt",
                inputStream = ByteArrayInputStream("hello".toByteArray()),
                contentLength = 5,
                contentType = "text/plain",
            ),
        )

        assertEquals("abc123", response.eTag)
    }

    // ── getObject ────────────────────────────────────────────────────────────

    @Test
    fun `getObject returns StorageObject on success`() {
        val now = Date.from(Instant.now())
        val meta = ObjectMetadata().apply {
            contentType = "text/plain"
            contentLength = 5
            lastModified = now
        }
        val ossObject = mockk<OSSObject> {
            every { objectMetadata } returns meta
            every { objectContent } returns ByteArrayInputStream("hello".toByteArray())
        }
        every { ossClient.getObject(any<OssGetObjectRequest>()) } returns ossObject

        val result = client.getObject(GetObjectRequest(bucket = "bucket", key = "key.txt"))

        assertEquals("key.txt", result.key)
        assertEquals(5L, result.contentLength)
        assertEquals("text/plain", result.contentType)
    }

    @Test
    fun `getObject throws ObjectNotFoundException for NoSuchKey error`() {
        val ossEx = mockk<OSSException>(relaxed = true) {
            every { errorCode } returns "NoSuchKey"
        }
        every { ossClient.getObject(any<OssGetObjectRequest>()) } throws ossEx

        try {
            client.getObject(GetObjectRequest(bucket = "bucket", key = "missing.txt"))
            error("expected ObjectNotFoundException")
        } catch (e: ObjectNotFoundException) {
            assertEquals("bucket", e.bucket)
            assertEquals("missing.txt", e.key)
        }
    }

    @Test
    fun `getObject wraps other OSSException as StorageException`() {
        val ossEx = mockk<OSSException>(relaxed = true) {
            every { errorCode } returns "ServiceUnavailable"
        }
        every { ossClient.getObject(any<OssGetObjectRequest>()) } throws ossEx

        try {
            client.getObject(GetObjectRequest(bucket = "bucket", key = "key.txt"))
            error("expected StorageException")
        } catch (e: StorageException) {
            assertTrue(e !is ObjectNotFoundException)
        }
    }

    // ── deleteObject ─────────────────────────────────────────────────────────

    @Test
    fun `deleteObject delegates to OSSClient`() {
        every { ossClient.deleteObject(any<String>(), any<String>()) } returns mockk<VoidResult>()

        client.deleteObject(DeleteObjectRequest(bucket = "bucket", key = "key.txt"))

        verify(exactly = 1) { ossClient.deleteObject("bucket", "key.txt") }
    }

    // ── listObjects ──────────────────────────────────────────────────────────

    @Test
    fun `listObjects returns mapped objects`() {
        val now = Date.from(Instant.now())
        val summary = OSSObjectSummary().apply {
            key = "file.txt"
            size = 42L
            lastModified = now
            eTag = "etag"
        }
        val listResult = mockk<ListObjectsV2Result> {
            every { objectSummaries } returns listOf(summary)
            every { isTruncated } returns false
            every { nextContinuationToken } returns null
        }
        every { ossClient.listObjectsV2(any<OssListObjectsV2Request>()) } returns listResult

        val result = client.listObjects(ListObjectsRequest(bucket = "bucket", prefix = "files/"))

        assertEquals(1, result.objects.size)
        assertEquals("file.txt", result.objects[0].key)
        assertEquals(42L, result.objects[0].size)
    }

    // ── generateUploadToken ───────────────────────────────────────────────────

    @Test
    fun `generateUploadToken returns POST token with OSS form fields`() {
        every { ossClient.generatePostPolicy(any<Date>(), any()) } returns """{"expiration":"2099-01-01T00:00:00.000Z","conditions":[]}"""
        every { ossClient.calculatePostSignature(any<String>()) } returns "SIG"
        every { ossClient.endpoint } returns URI.create("https://oss-cn-hangzhou.aliyuncs.com")

        val policy = UploadPolicy(
            bucket = "my-bucket",
            keyPrefix = "uploads/",
            allowedContentTypes = listOf("image/jpeg"),
            maxContentLength = 5 * 1024 * 1024,
            expireSeconds = 600,
        )
        val token = client.generateUploadToken(policy)

        assertEquals(HttpMethod.POST, token.method)
        assertTrue(token.uploadUrl.startsWith("https://"))
        assertEquals("ACCESS_KEY", token.fields["OSSAccessKeyId"])
        assertNotNull(token.fields["policy"])
        assertEquals("SIG", token.fields["Signature"])
        assertEquals("image/jpeg", token.fields["Content-Type"])
        assertTrue(token.expiresAt.isAfter(Instant.now()))
    }

    @Test
    fun `generateUploadToken key uses keyPrefix`() {
        every { ossClient.generatePostPolicy(any<Date>(), any()) } returns "{}"
        every { ossClient.calculatePostSignature(any<String>()) } returns "SIG"
        every { ossClient.endpoint } returns URI.create("https://oss-cn-hangzhou.aliyuncs.com")

        val token = client.generateUploadToken(UploadPolicy(bucket = "b", keyPrefix = "docs/"))

        assertTrue(token.fields["key"]!!.startsWith("docs/"))
    }

    // ── generatePresignedDownloadUrl ─────────────────────────────────────────

    @Test
    fun `generatePresignedDownloadUrl returns URL string`() {
        every { ossClient.generatePresignedUrl(any<String>(), any<String>(), any<Date>()) } returns
            URL("https://bucket.oss-cn-hangzhou.aliyuncs.com/key.txt?Expires=123&OSSAccessKeyId=AK&Signature=SIG")

        val url = client.generatePresignedDownloadUrl("bucket", "key.txt", 3600)

        assertTrue(url.startsWith("https://"))
        assertTrue(url.contains("Signature"))
    }

    // ── close ────────────────────────────────────────────────────────────────

    @Test
    fun `close shuts down OSSClient`() {
        justRun { ossClient.shutdown() }

        client.close()

        verify(exactly = 1) { ossClient.shutdown() }
    }
}
