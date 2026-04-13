package me.ahoo.costore.s3

import io.mockk.mockk
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PresignDeleteObjectRequest
import me.ahoo.costore.core.model.PresignGetObjectRequest
import me.ahoo.costore.core.model.PresignPutObjectRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.io.InputStream
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
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val request = object : HeadObjectRequest {
            override val bucket: BucketName = bucket
            override val key: ObjectKey = key
        }

        val response = store.headObject(request)

        with(response) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
        }
    }

    @Test
    fun `should put object`() {
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val content = "test content".byteInputStream()
        val request = object : PutObjectRequest {
            override val bucket: BucketName = bucket
            override val key: ObjectKey = key
            override val content: InputStream = content
            override val contentType: String? = "text/plain"
            override val metadata: Map<String, String> = emptyMap()
        }

        val response = store.putObject(request)

        response.eTag.assert().isNotNull()
    }

    @Test
    fun `should delete object`() {
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val request = object : DeleteObjectRequest {
            override val bucket: BucketName = bucket
            override val key: ObjectKey = key
            override val versionId: String? = null
        }

        val response = store.deleteObject(request)

        response.deleteMarker.assert().isFalse()
    }

    @Test
    fun `should list objects`() {
        val bucket = "test-bucket" as BucketName
        val request = object : ListObjectsRequest {
            override val bucket: BucketName = bucket
            override val prefix: String? = "prefix/"
            override val delimiter: String? = "/"
            override val marker: String? = null
            override val maxKeys: Int = 100
        }

        val response = store.listObjects(request)

        with(response) {
            objects.assert().isEmpty()
            isTruncated.assert().isFalse()
        }
    }

    @Test
    fun `should presign get object`() {
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val request = object : PresignGetObjectRequest {
            override val bucket: BucketName = bucket
            override val key: ObjectKey = key
            override val expiration: Duration = Duration.ofMinutes(15)
        }

        val response = store.presignGetObject(request)

        response.url.assert().isNotNull()
    }

    @Test
    fun `should presign put object`() {
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val request = object : PresignPutObjectRequest {
            override val bucket: BucketName = bucket
            override val key: ObjectKey = key
            override val expiration: Duration = Duration.ofMinutes(15)
            override val contentType: String? = "text/plain"
        }

        val response = store.presignPutObject(request)

        response.url.assert().isNotNull()
    }

    @Test
    fun `should presign delete object`() {
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val request = object : PresignDeleteObjectRequest {
            override val bucket: BucketName = bucket
            override val key: ObjectKey = key
            override val expiration: Duration = Duration.ofMinutes(15)
            override val versionId: String? = null
        }

        val response = store.presignDeleteObject(request)

        response.url.assert().isNotNull()
    }
}
