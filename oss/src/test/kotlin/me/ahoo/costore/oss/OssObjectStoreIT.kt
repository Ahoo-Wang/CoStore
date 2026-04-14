package me.ahoo.costore.oss

import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.DeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PresignRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.time.Duration

class OssObjectStoreIT : AbstractObjectStoreIT() {

    private lateinit var store: OssObjectStore

    override fun initStore(syncStore: OssObjectStore) {
        store = syncStore
    }

    override fun closeStore() {
        store.close()
    }

    override fun doPutObject(key: ObjectKey, content: String): PutObjectResponse {
        return store.putObject(
            PutObjectRequest(
                bucket = bucket,
                key = key,
                content = content.byteInputStream(),
                contentLength = content.toByteArray().size.toLong(),
                contentType = "text/plain"
            )
        )
    }

    override fun doGetObject(key: ObjectKey): GetObjectResponse {
        return store.getObject(GetObjectRequest(bucket, key))
    }

    override fun doHeadObject(key: ObjectKey): HeadObjectResponse {
        return store.headObject(HeadObjectRequest(bucket, key))
    }

    override fun doDeleteObject(key: ObjectKey): DeleteObjectResponse {
        return store.deleteObject(DeleteObjectRequest(bucket, key))
    }

    @Test
    fun `should head object`() = headObjectTest()

    @Test
    fun `should put and get object`() = putAndGetObjectTest()

    @Test
    fun `should delete object`() = deleteObjectTest()

    @Test
    fun `should list objects`() {
        val prefix = "costore/test/list-${System.currentTimeMillis()}"
        val key1: ObjectKey = "$prefix-1"
        val key2: ObjectKey = "$prefix-2"

        try {
            listOf(key1, key2).forEach { k ->
                store.putObject(
                    PutObjectRequest(
                        bucket = bucket,
                        key = k,
                        content = "content".byteInputStream(),
                        contentLength = "content".length.toLong(),
                        contentType = "text/plain"
                    )
                )
            }

            val listResponse = store.listObjects(
                me.ahoo.costore.core.model.ListObjectsRequest(
                    bucket = bucket,
                    prefix = "$prefix-",
                    maxKeys = 100
                )
            )
            listResponse.objects.assert().hasSize(2)
            listResponse.isTruncated.assert().isFalse()
        } finally {
            listOf(key1, key2).forEach { k ->
                try {
                    doDeleteObject(k)
                } catch (_: Exception) {
                    // Ignore
                }
            }
        }
    }

    @Test
    fun `should presign get object`() {
        val key = newKey("presign-get")
        try {
            doPutObject(key, "content")

            val presignRequest = PresignRequest.Get(
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
            doDeleteObject(key)
        }
    }

    @Test
    fun `should presign put object`() {
        val key = newKey("presign-put")

        try {
            val presignRequest = PresignRequest.Put(
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
            doDeleteObject(key)
        }
    }

    @Test
    fun `should presign delete object`() {
        val key = newKey("presign-delete")
        try {
            doPutObject(key, "content")

            val presignRequest = PresignRequest.Delete(
                bucket = bucket,
                key = key,
                expiration = Duration.ofMinutes(15)
            )
            // OSS does not support presigned DELETE URLs
            org.junit.jupiter.api.assertThrows<UnsupportedOperationException> {
                store.presignDeleteObject(presignRequest)
            }
        } finally {
            doDeleteObject(key)
        }
    }
}
