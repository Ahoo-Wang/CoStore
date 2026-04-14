package me.ahoo.costore.oss

import kotlinx.coroutines.runBlocking
import me.ahoo.costore.core.api.coroutines.CoroutinesObjectStore
import me.ahoo.costore.core.api.coroutines.asCoroutines
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.DeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import org.junit.jupiter.api.Test

class CoroutinesObjectStoreIT : AbstractObjectStoreIT() {

    private lateinit var coroutinesStore: CoroutinesObjectStore

    override fun initStore(syncStore: OssObjectStore) {
        coroutinesStore = syncStore.asCoroutines()
    }

    override fun closeStore() {
        runBlocking {
            coroutinesStore.close()
        }
    }

    override fun doPutObject(key: ObjectKey, content: String): PutObjectResponse {
        return runBlocking {
            coroutinesStore.putObject(
                PutObjectRequest(
                    bucket = bucket,
                    key = key,
                    content = content.byteInputStream(),
                    contentLength = content.toByteArray().size.toLong(),
                    contentType = "text/plain"
                )
            )
        }
    }

    override fun doGetObject(key: ObjectKey): GetObjectResponse {
        return runBlocking {
            coroutinesStore.getObject(
                GetObjectRequest(bucket, key)
            )
        }
    }

    override fun doHeadObject(key: ObjectKey): HeadObjectResponse {
        return runBlocking {
            coroutinesStore.headObject(
                HeadObjectRequest(bucket, key)
            )
        }
    }

    override fun doDeleteObject(key: ObjectKey): DeleteObjectResponse {
        return runBlocking {
            coroutinesStore.deleteObject(
                DeleteObjectRequest(bucket, key)
            )
        }
    }

    @Test
    fun `should head object`() = headObjectTest()

    @Test
    fun `should put and get object`() = putAndGetObjectTest()

    @Test
    fun `should delete object`() = deleteObjectTest()
}
