package me.ahoo.costore.oss

import me.ahoo.costore.core.api.reactive.ReactiveObjectStore
import me.ahoo.costore.core.api.reactive.asReactive
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
import reactor.core.publisher.Mono

class ReactiveObjectStoreIT : AbstractObjectStoreIT() {

    private lateinit var reactiveStore: ReactiveObjectStore

    override fun initStore(syncStore: OssObjectStore) {
        reactiveStore = syncStore.asReactive()
    }

    override fun closeStore() {
        Mono.from(reactiveStore.close()).block()
    }

    override fun doPutObject(key: ObjectKey, content: String): PutObjectResponse {
        return reactiveStore.putObject(
            PutObjectRequest(
                bucket = bucket,
                key = key,
                content = content.byteInputStream(),
                contentLength = content.toByteArray().size.toLong(),
                contentType = "text/plain"
            )
        ).block()!!
    }

    override fun doGetObject(key: ObjectKey): GetObjectResponse {
        return reactiveStore.getObject(
            GetObjectRequest(bucket, key)
        ).block()!!
    }

    override fun doHeadObject(key: ObjectKey): HeadObjectResponse {
        return reactiveStore.headObject(
            HeadObjectRequest(bucket, key)
        ).block()!!
    }

    override fun doDeleteObject(key: ObjectKey): DeleteObjectResponse {
        return reactiveStore.deleteObject(
            DeleteObjectRequest(bucket, key)
        ).block()!!
    }

    @Test
    fun `should head object`() = headObjectTest()

    @Test
    fun `should put and get object`() = putAndGetObjectTest()

    @Test
    fun `should delete object`() = deleteObjectTest()
}
