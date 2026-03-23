package me.ahoo.costore.core.provider

import me.ahoo.costore.core.api.async.AsyncObjectStore
import me.ahoo.costore.core.api.async.asAsync
import me.ahoo.costore.core.api.coroutines.CoroutinesObjectStore
import me.ahoo.costore.core.api.coroutines.asCoroutines
import me.ahoo.costore.core.api.reactive.ReactiveObjectStore
import me.ahoo.costore.core.api.reactive.asReactive
import me.ahoo.costore.core.api.sync.ObjectStore

interface ObjectStoreProvider<in CREDENTIALS : StoreProviderCredentials> {
    fun sync(credentials: CREDENTIALS): ObjectStore

    fun async(credentials: CREDENTIALS): AsyncObjectStore

    fun reactive(credentials: CREDENTIALS): ReactiveObjectStore

    fun coroutines(credentials: CREDENTIALS): CoroutinesObjectStore
}

abstract class AbstractObjectStoreProvider<in CREDENTIALS : StoreProviderCredentials> :
    ObjectStoreProvider<CREDENTIALS> {
    override fun async(credentials: CREDENTIALS): AsyncObjectStore = sync(credentials).asAsync()

    override fun reactive(credentials: CREDENTIALS): ReactiveObjectStore = sync(credentials).asReactive()

    override fun coroutines(credentials: CREDENTIALS): CoroutinesObjectStore = sync(credentials).asCoroutines()
}
