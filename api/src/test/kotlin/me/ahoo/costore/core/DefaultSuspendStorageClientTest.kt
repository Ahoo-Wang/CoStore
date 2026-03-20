package me.ahoo.costore.core

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import me.ahoo.costore.core.exception.ObjectNotFoundException
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.HttpMethod
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.costore.core.model.StorageObject
import me.ahoo.costore.core.model.StorageObjectSummary
import me.ahoo.costore.core.model.UploadPolicy
import me.ahoo.costore.core.model.UploadToken
import me.ahoo.test.asserts.assert
import me.ahoo.test.asserts.assertThrownBy
import java.io.ByteArrayInputStream
import java.time.Instant
import kotlin.test.Test

class DefaultSuspendStorageClientTest {

    private val delegate: StorageClient = mockk()
    private val client: SuspendStorageClient = delegate.asSuspend()

    // ── putObject ────────────────────────────────────────────────────────────

    @Test
    fun `putObject delegates to sync client`() = runBlocking {
        val request = PutObjectRequest(
            bucket = "bucket",
            key = "key.txt",
            inputStream = ByteArrayInputStream("data".toByteArray()),
            contentLength = 4,
        )
        val expected = PutObjectResponse(eTag = "abc123")
        every { delegate.putObject(request) } returns expected

        val result = client.putObject(request)

        result.eTag.assert().isEqualTo("abc123")
        verify(exactly = 1) { delegate.putObject(request) }
    }

    // ── getObject ────────────────────────────────────────────────────────────

    @Test
    fun `getObject delegates to sync client`() = runBlocking {
        val request = GetObjectRequest(bucket = "bucket", key = "key.txt")
        val expected = StorageObject(
            key = "key.txt",
            content = ByteArrayInputStream("data".toByteArray()),
            contentLength = 4,
            contentType = "text/plain",
            metadata = emptyMap(),
            lastModified = Instant.now(),
        )
        every { delegate.getObject(request) } returns expected

        val result = client.getObject(request)

        result.key.assert().isEqualTo("key.txt")
        result.contentLength.assert().isEqualTo(4L)
        result.contentType.assert().isEqualTo("text/plain")
        verify(exactly = 1) { delegate.getObject(request) }
    }

    @Test
    fun `getObject propagates ObjectNotFoundException`() {
        val request = GetObjectRequest(bucket = "bucket", key = "missing.txt")
        every { delegate.getObject(request) } throws ObjectNotFoundException(bucket = "bucket", key = "missing.txt")

        assertThrownBy<ObjectNotFoundException> {
            runBlocking { client.getObject(request) }
        }.hasFieldOrPropertyWithValue("bucket", "bucket")
            .hasFieldOrPropertyWithValue("key", "missing.txt")
    }

    // ── deleteObject ─────────────────────────────────────────────────────────

    @Test
    fun `deleteObject delegates to sync client`() = runBlocking {
        val request = DeleteObjectRequest(bucket = "bucket", key = "key.txt")
        justRun { delegate.deleteObject(request) }

        client.deleteObject(request)

        verify(exactly = 1) { delegate.deleteObject(request) }
    }

    // ── listObjects ──────────────────────────────────────────────────────────

    @Test
    fun `listObjects delegates to sync client`() = runBlocking {
        val request = ListObjectsRequest(bucket = "bucket", prefix = "files/")
        val expected = ListObjectsResponse(
            objects = listOf(
                StorageObjectSummary(key = "files/a.txt", size = 10L, lastModified = Instant.now()),
            ),
            isTruncated = false,
        )
        every { delegate.listObjects(request) } returns expected

        val result = client.listObjects(request)

        result.objects.assert().hasSize(1)
        result.objects[0].key.assert().isEqualTo("files/a.txt")
        verify(exactly = 1) { delegate.listObjects(request) }
    }

    // ── generateUploadToken ───────────────────────────────────────────────────

    @Test
    fun `generateUploadToken delegates to sync client`() = runBlocking {
        val policy = UploadPolicy(bucket = "bucket", keyPrefix = "uploads/", expireSeconds = 600)
        val expected = UploadToken(
            uploadUrl = "https://bucket.s3.amazonaws.com/uploads/key",
            method = HttpMethod.PUT,
            expiresAt = Instant.now().plusSeconds(600),
        )
        every { delegate.generateUploadToken(policy) } returns expected

        val result = client.generateUploadToken(policy)

        result.method.assert().isEqualTo(HttpMethod.PUT)
        result.uploadUrl.assert().startsWith("https://")
        verify(exactly = 1) { delegate.generateUploadToken(policy) }
    }

    // ── generatePresignedDownloadUrl ──────────────────────────────────────────

    @Test
    fun `generatePresignedDownloadUrl delegates to sync client`() = runBlocking {
        every { delegate.generatePresignedDownloadUrl("bucket", "key.txt", 3600) } returns
            "https://bucket.s3.amazonaws.com/key.txt?X-Amz-Signature=sig"

        val url = client.generatePresignedDownloadUrl("bucket", "key.txt", 3600)

        url.assert().startsWith("https://").contains("X-Amz-Signature")
        verify(exactly = 1) { delegate.generatePresignedDownloadUrl("bucket", "key.txt", 3600) }
    }

    // ── close ────────────────────────────────────────────────────────────────

    @Test
    fun `close delegates to sync client`() {
        justRun { delegate.close() }

        client.close()

        verify(exactly = 1) { delegate.close() }
    }

    // ── asSuspend extension ───────────────────────────────────────────────────

    @Test
    fun `asSuspend wraps StorageClient in DefaultSuspendStorageClient`() {
        val wrapped = delegate.asSuspend()
        wrapped.assert().isInstanceOf(DefaultSuspendStorageClient::class.java)
    }
}
