package me.ahoo.costore.core.provider

import me.ahoo.costore.core.api.async.AsyncObjectStore
import me.ahoo.costore.core.api.async.asAsync
import me.ahoo.costore.core.api.coroutines.CoroutinesObjectStore
import me.ahoo.costore.core.api.coroutines.asCoroutines
import me.ahoo.costore.core.api.reactive.ReactiveObjectStore
import me.ahoo.costore.core.api.reactive.asReactive
import me.ahoo.costore.core.api.sync.ObjectStore

/**
 * Factory interface for creating object store instances from credentials.
 *
 * Implementations provide all four programming model variants (sync, async, reactive, coroutines)
 * for a specific storage backend (e.g., S3, OSS).
 *
 * @param CREDENTIALS The credentials type required by this provider
 */
interface ObjectStoreProvider<in CREDENTIALS : StoreProviderCredentials> {
    /** Creates a synchronous [ObjectStore] instance. */
    fun sync(credentials: CREDENTIALS): ObjectStore

    /** Creates an asynchronous [AsyncObjectStore] instance. */
    fun async(credentials: CREDENTIALS): AsyncObjectStore

    /** Creates a reactive [ReactiveObjectStore] instance. */
    fun reactive(credentials: CREDENTIALS): ReactiveObjectStore

    /** Creates a coroutines-based [CoroutinesObjectStore] instance. */
    fun coroutines(credentials: CREDENTIALS): CoroutinesObjectStore
}

/**
 * Abstract base implementation of [ObjectStoreProvider] that provides default
 * implementations for async, reactive, and coroutines variants by delegating
 * to the synchronous implementation.
 */
abstract class AbstractObjectStoreProvider<in CREDENTIALS : StoreProviderCredentials> :
    ObjectStoreProvider<CREDENTIALS> {
    override fun async(credentials: CREDENTIALS): AsyncObjectStore = sync(credentials).asAsync()

    override fun reactive(credentials: CREDENTIALS): ReactiveObjectStore = sync(credentials).asReactive()

    override fun coroutines(credentials: CREDENTIALS): CoroutinesObjectStore = sync(credentials).asCoroutines()
}
