package me.ahoo.costore.oss

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DefaultDeleteObjectRequest
import me.ahoo.costore.core.model.DefaultGetObjectRequest
import me.ahoo.costore.core.model.DefaultHeadObjectRequest
import me.ahoo.costore.core.model.DefaultListObjectsRequest
import me.ahoo.costore.core.model.DefaultPresignDeleteObjectRequest
import me.ahoo.costore.core.model.DefaultPresignGetObjectRequest
import me.ahoo.costore.core.model.DefaultPresignPutObjectRequest
import me.ahoo.costore.core.model.DefaultPutObjectRequest
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import java.time.Duration

@EnabledIfEnvironmentVariable(named = "OSS_ACCESS_KEY_ID", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_ACCESS_KEY_SECRET", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_ENDPOINT", matches = ".+")
@EnabledIfEnvironmentVariable(named = "OSS_BUCKET", matches = ".+")
class OssObjectStoreIT {
    private lateinit var client: OSS
    private lateinit var store: OssObjectStore
    private lateinit var bucket: BucketName

    @BeforeEach
    fun setup() {
        val endpoint = System.getenv("OSS_ENDPOINT")!!
        val accessKey = System.getenv("OSS_ACCESS_KEY_ID")!!
        val secretKey = System.getenv("OSS_ACCESS_KEY_SECRET")!!
        bucket = System.getenv("OSS_BUCKET") as BucketName
        client = OSSClientBuilder().build(endpoint, accessKey, secretKey)
        store = OssObjectStore(client)
    }

    @AfterEach
    fun cleanup() {
        store.close()
    }

    @Test
    fun `should head object`() {
        val key: ObjectKey = "costore/test/head-${System.currentTimeMillis()}"
        try {
            // Put first
            val putRequest = DefaultPutObjectRequest(
                bucket = bucket,
                key = key,
                content = "content".byteInputStream(),
                contentType = "text/plain"
            )
            store.putObject(putRequest)

            // Head
            val headRequest = DefaultHeadObjectRequest(
                bucket = bucket,
                key = key
            )
            val response = store.headObject(headRequest)

            with(response) {
                this.bucket.assert().isEqualTo(bucket)
                this.key.assert().isEqualTo(key)
                contentLength.assert().isEqualTo(7L)
                eTag.assert().isNotNull()
            }
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }

    @Test
    fun `should put and get object`() {
        val key: ObjectKey = "costore/test/put-get-${System.currentTimeMillis()}"
        val content = "Hello OSS!".repeat(100)
        try {
            val putRequest = DefaultPutObjectRequest(
                bucket = bucket,
                key = key,
                content = content.byteInputStream(),
                contentType = "text/plain"
            )
            val putResponse = store.putObject(putRequest)
            putResponse.eTag.assert().isNotNull()

            val getRequest = DefaultGetObjectRequest(
                bucket = bucket,
                key = key
            )
            val getResponse = store.getObject(getRequest)
            getResponse.metadata.bucket.assert().isEqualTo(bucket)
            getResponse.metadata.key.assert().isEqualTo(key)
            getResponse.metadata.eTag.assert().isNotNull()
            val readContent = getResponse.content.bufferedReader().readText()
            readContent.assert().isEqualTo(content)
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket = bucket, key = key))
        }
    }

    @Test
    fun `should delete object`() {
        val key: ObjectKey = "costore/test/delete-${System.currentTimeMillis()}"
        try {
            // Put first
            val putRequest = DefaultPutObjectRequest(
                bucket = bucket,
                key = key,
                content = "to be deleted".byteInputStream(),
                contentType = "text/plain"
            )
            store.putObject(putRequest)

            // Delete
            val deleteRequest = DefaultDeleteObjectRequest(bucket = bucket, key = key)
            val deleteResponse = store.deleteObject(deleteRequest)
            deleteResponse.deleteMarker.assert().isFalse()
        } finally {
            // Cleanup in case test failed before delete
            try {
                store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
            } catch (_: Exception) {
                // Ignore
            }
        }
    }

    @Test
    fun `should list objects`() {
        val prefix = "costore/test/list-${System.currentTimeMillis()}"
        val key1: ObjectKey = "$prefix-1"
        val key2: ObjectKey = "$prefix-2"

        try {
            // Put objects
            listOf(key1, key2).forEach { k ->
                store.putObject(
                    DefaultPutObjectRequest(
                        bucket = bucket,
                        key = k,
                        content = "content".byteInputStream(),
                        contentType = "text/plain"
                    )
                )
            }

            // List
            val listRequest = DefaultListObjectsRequest(
                bucket = bucket,
                prefix = "$prefix-",
                maxKeys = 100
            )
            val listResponse = store.listObjects(listRequest)
            listResponse.objects.assert().hasSize(2)
            listResponse.isTruncated.assert().isFalse()
        } finally {
            // Cleanup
            listOf(key1, key2).forEach { k ->
                try {
                    store.deleteObject(DefaultDeleteObjectRequest(bucket, k))
                } catch (_: Exception) {
                    // Ignore
                }
            }
        }
    }

    @Test
    fun `should presign get object`() {
        val key: ObjectKey = "costore/test/presign-get-${System.currentTimeMillis()}"
        try {
            // Put first
            store.putObject(
                DefaultPutObjectRequest(
                    bucket = bucket,
                    key = key,
                    content = "content".byteInputStream(),
                    contentType = "text/plain"
                )
            )

            val presignRequest = DefaultPresignGetObjectRequest(
                bucket = bucket,
                key = key,
                expiration = Duration.ofMinutes(15)
            )
            val response = store.presignGetObject(presignRequest)

            response.url.assert().isNotNull()
            response.expiration.assert().isNotNull()
            response.url.toString().assert().contains(bucket)
            response.url.toString().assert().contains(key)
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }

    @Test
    fun `should presign put object`() {
        val key: ObjectKey = "costore/test/presign-put-${System.currentTimeMillis()}"

        try {
            val presignRequest = DefaultPresignPutObjectRequest(
                bucket = bucket,
                key = key,
                expiration = Duration.ofMinutes(15),
                contentType = "text/plain"
            )
            val response = store.presignPutObject(presignRequest)

            response.url.assert().isNotNull()
            response.expiration.assert().isNotNull()
            response.url.toString().assert().contains(bucket)
            response.url.toString().assert().contains(key)
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }

    @Test
    fun `should presign delete object`() {
        val key: ObjectKey = "costore/test/presign-delete-${System.currentTimeMillis()}"

        try {
            // Put first
            store.putObject(
                DefaultPutObjectRequest(
                    bucket = bucket,
                    key = key,
                    content = "content".byteInputStream(),
                    contentType = "text/plain"
                )
            )

            val presignRequest = DefaultPresignDeleteObjectRequest(
                bucket = bucket,
                key = key,
                expiration = Duration.ofMinutes(15)
            )
            val response = store.presignDeleteObject(presignRequest)

            response.url.assert().isNotNull()
            response.expiration.assert().isNotNull()
            response.url.toString().assert().contains(bucket)
            response.url.toString().assert().contains(key)
        } finally {
            store.deleteObject(DefaultDeleteObjectRequest(bucket, key))
        }
    }
}
