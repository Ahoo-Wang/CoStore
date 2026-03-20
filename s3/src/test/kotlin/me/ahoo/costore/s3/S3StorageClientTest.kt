package me.ahoo.costore.s3

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import me.ahoo.costore.core.exception.ObjectNotFoundException
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.HttpMethod
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.UploadPolicy
import me.ahoo.test.asserts.assert
import me.ahoo.test.asserts.assertThrownBy
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest as S3DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse
import software.amazon.awssdk.services.s3.model.GetObjectRequest as S3GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response
import software.amazon.awssdk.services.s3.model.NoSuchKeyException
import software.amazon.awssdk.services.s3.model.PutObjectRequest as S3PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse as S3PutObjectResponse
import software.amazon.awssdk.services.s3.model.S3Object
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.ByteArrayInputStream
import java.net.URL
import java.time.Instant
import kotlin.test.Test

class S3StorageClientTest {

    private val s3Client: S3Client = mockk()
    private val s3Presigner: S3Presigner = mockk()
    private val client = S3StorageClient(s3Client, s3Presigner)

    // ── putObject ────────────────────────────────────────────────────────────

    @Test
    fun `putObject returns eTag on success`() {
        val response = mockk<S3PutObjectResponse> {
            every { eTag() } returns "\"abc123\""
            every { versionId() } returns null
        }
        every { s3Client.putObject(any<S3PutObjectRequest>(), any<RequestBody>()) } returns response

        val result = client.putObject(
            PutObjectRequest(
                bucket = "bucket",
                key = "key.txt",
                inputStream = ByteArrayInputStream("hello".toByteArray()),
                contentLength = 5,
                contentType = "text/plain",
            ),
        )

        result.eTag.assert().isEqualTo("\"abc123\"")
    }

    // ── getObject ────────────────────────────────────────────────────────────

    @Test
    fun `getObject returns StorageObject on success`() {
        val now = Instant.now()
        val objectResponse = mockk<GetObjectResponse> {
            every { contentLength() } returns 10L
            every { contentType() } returns "text/plain"
            every { metadata() } returns mapOf("author" to "test")
            every { lastModified() } returns now
        }
        val responseStream = mockk<ResponseInputStream<GetObjectResponse>> {
            every { response() } returns objectResponse
        }
        every { s3Client.getObject(any<S3GetObjectRequest>()) } returns responseStream

        val result = client.getObject(GetObjectRequest(bucket = "bucket", key = "key.txt"))

        result.key.assert().isEqualTo("key.txt")
        result.contentLength.assert().isEqualTo(10L)
        result.contentType.assert().isEqualTo("text/plain")
        result.metadata["author"].assert().isEqualTo("test")
    }

    @Test
    fun `getObject throws ObjectNotFoundException when key missing`() {
        every { s3Client.getObject(any<S3GetObjectRequest>()) } throws
            NoSuchKeyException.builder().message("NoSuchKey").build()

        assertThrownBy<ObjectNotFoundException> {
            client.getObject(GetObjectRequest(bucket = "bucket", key = "missing.txt"))
        }.hasFieldOrPropertyWithValue("bucket", "bucket")
            .hasFieldOrPropertyWithValue("key", "missing.txt")
    }

    // ── deleteObject ─────────────────────────────────────────────────────────

    @Test
    fun `deleteObject invokes SDK delete`() {
        every { s3Client.deleteObject(any<java.util.function.Consumer<S3DeleteObjectRequest.Builder>>()) } returns
            DeleteObjectResponse.builder().build()

        client.deleteObject(DeleteObjectRequest(bucket = "bucket", key = "key.txt"))

        verify(exactly = 1) {
            s3Client.deleteObject(any<java.util.function.Consumer<S3DeleteObjectRequest.Builder>>())
        }
    }

    // ── listObjects ──────────────────────────────────────────────────────────

    @Test
    fun `listObjects returns mapped response`() {
        val now = Instant.now()
        val s3Object = mockk<S3Object> {
            every { key() } returns "file.txt"
            every { size() } returns 100L
            every { lastModified() } returns now
            every { eTag() } returns "\"etag\""
        }
        val listResponse = mockk<ListObjectsV2Response> {
            every { contents() } returns listOf(s3Object)
            every { isTruncated } returns false
            every { nextContinuationToken() } returns null
        }
        every { s3Client.listObjectsV2(any<ListObjectsV2Request>()) } returns listResponse

        val result = client.listObjects(ListObjectsRequest(bucket = "bucket", prefix = "files/"))

        result.objects.assert().hasSize(1)
        result.objects[0].key.assert().isEqualTo("file.txt")
        result.objects[0].size.assert().isEqualTo(100L)
    }

    // ── generateUploadToken ───────────────────────────────────────────────────

    @Test
    fun `generateUploadToken returns PUT token with correct URL`() {
        val presignedPut = mockk<PresignedPutObjectRequest> {
            every { url() } returns URL("https://bucket.s3.amazonaws.com/key?X-Amz-Signature=sig")
        }
        every { s3Presigner.presignPutObject(any<PutObjectPresignRequest>()) } returns presignedPut

        val policy = UploadPolicy(
            bucket = "bucket",
            keyPrefix = "uploads/",
            allowedContentTypes = listOf("image/jpeg"),
            maxContentLength = 1024 * 1024,
            expireSeconds = 600,
        )
        val token = client.generateUploadToken(policy)

        token.method.assert().isEqualTo(HttpMethod.PUT)
        token.uploadUrl.assert().startsWith("https://")
        token.headers["Content-Type"].assert().isEqualTo("image/jpeg")
        token.expiresAt.assert().isNotNull().isAfter(Instant.now())
    }

    @Test
    fun `generateUploadToken key uses keyPrefix`() {
        val presignedPut = mockk<PresignedPutObjectRequest> {
            every { url() } returns URL("https://bucket.s3.amazonaws.com/uploads/uuid")
        }
        every { s3Presigner.presignPutObject(any<PutObjectPresignRequest>()) } returns presignedPut

        val policy = UploadPolicy(bucket = "bucket", keyPrefix = "uploads/")
        val token = client.generateUploadToken(policy)

        token.uploadUrl.assert().contains("uploads/")
    }

    // ── generatePresignedDownloadUrl ─────────────────────────────────────────

    @Test
    fun `generatePresignedDownloadUrl returns URL string`() {
        val presignedGet = mockk<PresignedGetObjectRequest> {
            every { url() } returns URL("https://bucket.s3.amazonaws.com/key?X-Amz-Signature=sig")
        }
        every { s3Presigner.presignGetObject(any<GetObjectPresignRequest>()) } returns presignedGet

        val url = client.generatePresignedDownloadUrl("bucket", "key.txt", 3600)

        url.assert().startsWith("https://").contains("X-Amz-Signature")
    }

    // ── close ────────────────────────────────────────────────────────────────

    @Test
    fun `close shuts down both SDK clients`() {
        justRun { s3Client.close() }
        justRun { s3Presigner.close() }

        client.close()

        verify(exactly = 1) { s3Client.close() }
        verify(exactly = 1) { s3Presigner.close() }
    }
}
