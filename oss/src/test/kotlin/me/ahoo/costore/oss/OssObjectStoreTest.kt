package me.ahoo.costore.oss

import com.aliyun.oss.OSS
import io.mockk.mockk
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DefaultDeleteObjectRequest
import me.ahoo.costore.core.model.DefaultHeadObjectRequest
import me.ahoo.costore.core.model.DefaultListObjectsRequest
import me.ahoo.costore.core.model.DefaultPresignDeleteObjectRequest
import me.ahoo.costore.core.model.DefaultPresignGetObjectRequest
import me.ahoo.costore.core.model.DefaultPresignPutObjectRequest
import me.ahoo.costore.core.model.DefaultPutObjectRequest
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

class OssObjectStoreTest {
    private lateinit var client: OSS
    private lateinit var store: OssObjectStore

    @BeforeEach
    fun setup() {
        client = mockk(relaxed = true)
        store = OssObjectStore(client)
    }

    @Test
    fun `should head object`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val request = DefaultHeadObjectRequest(
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
        val request = DefaultPutObjectRequest(
            bucket = bucket,
            key = key,
            content = content,
            contentType = "text/plain"
        )

        val response = store.putObject(request)

        response.eTag.assert().isNotNull()
    }

    @Test
    fun `should delete object`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val request = DefaultDeleteObjectRequest(
            bucket = bucket,
            key = key
        )

        val response = store.deleteObject(request)

        response.deleteMarker.assert().isFalse()
    }

    @Test
    fun `should list objects`() {
        val bucket: BucketName = "test-bucket"
        val request = DefaultListObjectsRequest(
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
        val request = DefaultPresignGetObjectRequest(
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
        val request = DefaultPresignPutObjectRequest(
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
        val request = DefaultPresignDeleteObjectRequest(
            bucket = bucket,
            key = key,
            expiration = Duration.ofMinutes(15)
        )

        val response = store.presignDeleteObject(request)

        response.url.assert().isNotNull()
        response.expiration.assert().isNotNull()
    }
}
