package me.ahoo.costore.core.api

import io.mockk.mockk
import me.ahoo.costore.core.api.async.AsyncObjectStore
import me.ahoo.costore.core.api.async.asAsync
import me.ahoo.costore.core.api.coroutines.CoroutinesObjectStore
import me.ahoo.costore.core.api.coroutines.asCoroutines
import me.ahoo.costore.core.api.reactive.ReactiveObjectStore
import me.ahoo.costore.core.api.reactive.asReactive
import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.util.concurrent.ForkJoinPool

class ObjectStoreExtensionsTest {
    @Test
    fun `asAsync should return AsyncObjectStore`() {
        val store = mockk<ObjectStore>()
        val asyncStore = store.asAsync()
        asyncStore.assert().isInstanceOf(AsyncObjectStore::class.java)
    }

    @Test
    fun `asAsync with custom executor should return AsyncObjectStore`() {
        val store = mockk<ObjectStore>()
        val executor = ForkJoinPool.commonPool()
        val asyncStore = store.asAsync(executor)
        asyncStore.assert().isInstanceOf(AsyncObjectStore::class.java)
    }

    @Test
    fun `asReactive should return ReactiveObjectStore`() {
        val store = mockk<ObjectStore>()
        val reactiveStore = store.asReactive()
        reactiveStore.assert().isInstanceOf(ReactiveObjectStore::class.java)
    }

    @Test
    fun `asCoroutines should return CoroutinesObjectStore`() {
        val store = mockk<ObjectStore>()
        val coroutinesStore = store.asCoroutines()
        coroutinesStore.assert().isInstanceOf(CoroutinesObjectStore::class.java)
    }
}
