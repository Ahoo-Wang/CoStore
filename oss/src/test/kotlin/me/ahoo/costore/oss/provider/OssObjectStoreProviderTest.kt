package me.ahoo.costore.oss.provider

import kotlinx.coroutines.runBlocking
import me.ahoo.costore.core.api.async.AsyncObjectStore
import me.ahoo.costore.core.api.coroutines.CoroutinesObjectStore
import me.ahoo.costore.core.api.reactive.ReactiveObjectStore
import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

class OssObjectStoreProviderTest {

    private lateinit var provider: OssObjectStoreProvider

    @BeforeEach
    fun setup() {
        provider = OssObjectStoreProvider()
    }

    @Test
    fun `should create sync ObjectStore`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-hangzhou.aliyuncs.com",
            accessKey = "test-access-key",
            secretKey = "test-secret-key"
        )

        val store = provider.sync(credentials)

        store.assert().isInstanceOf(ObjectStore::class.java)
        store.close()
    }

    @Test
    fun `should create async ObjectStore`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-hangzhou.aliyuncs.com",
            accessKey = "test-access-key",
            secretKey = "test-secret-key"
        )

        val store = provider.async(credentials)

        store.assert().isInstanceOf(AsyncObjectStore::class.java)
        store.close().toCompletableFuture().join()
    }

    @Test
    fun `should create reactive ObjectStore`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-hangzhou.aliyuncs.com",
            accessKey = "test-access-key",
            secretKey = "test-secret-key"
        )

        val store = provider.reactive(credentials)

        store.assert().isInstanceOf(ReactiveObjectStore::class.java)
        Mono.from(store.close()).block()
    }

    @Test
    fun `should create coroutines ObjectStore`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-hangzhou.aliyuncs.com",
            accessKey = "test-access-key",
            secretKey = "test-secret-key"
        )

        val store = provider.coroutines(credentials)

        store.assert().isInstanceOf(CoroutinesObjectStore::class.java)
        runBlocking { store.close() }
    }

    @Test
    fun `should create ObjectStore that can be closed`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-beijing.aliyuncs.com",
            accessKey = "my-access-key",
            secretKey = "my-secret-key"
        )

        val store = provider.sync(credentials)

        store.close()
    }
}
