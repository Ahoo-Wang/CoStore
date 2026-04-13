package me.ahoo.costore.oss

import me.ahoo.costore.core.api.async.AsyncObjectStore
import me.ahoo.costore.core.api.async.asAsync
import me.ahoo.costore.core.model.DefaultDeleteObjectRequest
import me.ahoo.costore.core.model.DefaultGetObjectRequest
import me.ahoo.costore.core.model.DefaultHeadObjectRequest
import me.ahoo.costore.core.model.DefaultPutObjectRequest
import me.ahoo.costore.core.model.DeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PutObjectResponse
import org.junit.jupiter.api.Test

class AsyncObjectStoreIT : AbstractObjectStoreIT() {

    private lateinit var asyncStore: AsyncObjectStore

    override fun initStore(syncStore: OssObjectStore) {
        asyncStore = syncStore.asAsync()
    }

    override fun closeStore() {
        asyncStore.close().toCompletableFuture().join()
    }

    override fun doPutObject(key: ObjectKey, content: String): PutObjectResponse {
        return asyncStore.putObject(
            DefaultPutObjectRequest(
                bucket = bucket,
                key = key,
                content = content.byteInputStream(),
                contentType = "text/plain"
            )
        ).toCompletableFuture().join()
    }

    override fun doGetObject(key: ObjectKey): GetObjectResponse {
        return asyncStore.getObject(
            DefaultGetObjectRequest(bucket, key)
        ).toCompletableFuture().join()
    }

    override fun doHeadObject(key: ObjectKey): HeadObjectResponse {
        return asyncStore.headObject(
            DefaultHeadObjectRequest(bucket, key)
        ).toCompletableFuture().join()
    }

    override fun doDeleteObject(key: ObjectKey): DeleteObjectResponse {
        return asyncStore.deleteObject(
            DefaultDeleteObjectRequest(bucket, key)
        ).toCompletableFuture().join()
    }

    @Test
    fun `should head object`() = headObjectTest()

    @Test
    fun `should put and get object`() = putAndGetObjectTest()

    @Test
    fun `should delete object`() = deleteObjectTest()
}
