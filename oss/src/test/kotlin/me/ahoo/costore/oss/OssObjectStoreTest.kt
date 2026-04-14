package me.ahoo.costore.oss

import com.aliyun.oss.OSS
import io.mockk.mockk
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PresignRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.test.asserts.assert
import me.ahoo.test.asserts.assertThrownBy
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
        val request = PutObjectRequest(
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
    fun `should presign delete object throw`() {
        val bucket: BucketName = "test-bucket"
        val key: ObjectKey = "test-key"
        val request = PresignRequest.Delete(
            bucket = bucket,
            key = key,
            expiration = Duration.ofMinutes(15)
        )

        assertThrownBy<UnsupportedOperationException> {
            store.presignDeleteObject(request)
        }.hasMessage("OSS does not support presigned DELETE URLs")
    }
}
