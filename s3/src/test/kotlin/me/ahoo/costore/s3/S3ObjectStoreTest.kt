package me.ahoo.costore.s3

import io.mockk.mockk
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PresignRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

class S3ObjectStoreTest {
    private lateinit var client: S3Client
    private lateinit var presigner: S3Presigner
    private lateinit var store: S3ObjectStore

    @BeforeEach
    fun setup() {
        client = mockk(relaxed = true)
        presigner = mockk(relaxed = true)
        store = S3ObjectStore(client, presigner)
    }

    @Test
    fun `should head object`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val request = HeadObjectRequest(
            bucket = bucket,
            key = key
        )

        val response = store.headObject(request)

        with(response) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
        }
    }

    @Test
    fun `should put object`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val content = "test content".byteInputStream()
        val contentLength = "test content".toByteArray().size.toLong()
        val contentType = "text/plain"
        val metadata = mapOf("meta1" to "value1")
        val request = PutObjectRequest(
            bucket = bucket,
            key = key,
            content = content,
            contentLength = contentLength,
            contentType = contentType,
            metadata = metadata
        )

        val response = store.putObject(request)

        response.eTag.assert().isNotNull()
    }

    @Test
    fun `should delete object`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val request = DeleteObjectRequest(
            bucket = bucket,
            key = key
        )

        val response = store.deleteObject(request)

        response.deleteMarker.assert().isFalse()
    }

    @Test
    fun `should list objects`() {
        val bucket: BucketName = "test-bucket"
        val request = ListObjectsRequest(
            bucket = bucket,
            prefix = "prefix/",
            delimiter = "/",
            maxKeys = 100
        )

        val response = store.listObjects(request)

        with(response) {
            objects.assert().isEmpty()
            isTruncated.assert().isFalse()
        }
    }

    @Test
    fun `should presign get object`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val request = PresignRequest.Get(
            bucket = bucket,
            key = key,
            expiration = Duration.ofMinutes(15)
        )

        val response = store.presignGetObject(request)

        response.url.assert().isNotNull()
        response.expiration.assert().isNotNull()
    }

    @Test
    fun `should presign put object`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val request = PresignRequest.Put(
            bucket = bucket,
            key = key,
            expiration = Duration.ofMinutes(15),
            contentType = "text/plain"
        )

        val response = store.presignPutObject(request)

        response.url.assert().isNotNull()
        response.expiration.assert().isNotNull()
    }

    @Test
    fun `should presign delete object`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val request = PresignRequest.Delete(
            bucket = bucket,
            key = key,
            expiration = Duration.ofMinutes(15)
        )

        val response = store.presignDeleteObject(request)

        response.url.assert().isNotNull()
        response.expiration.assert().isNotNull()
    }
}

private typealias S3Client = software.amazon.awssdk.services.s3.S3Client
private typealias S3Presigner = software.amazon.awssdk.services.s3.presigner.S3Presigner
