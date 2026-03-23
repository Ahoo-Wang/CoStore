package me.ahoo.costore.core.provider

interface ObjectStoreProvider<in CREDENTIALS : StoreProviderCredentials> {
    fun sync(credentials: CREDENTIALS): me.ahoo.costore.core.api.sync.ObjectStore
    fun reactive(credentials: CREDENTIALS): me.ahoo.costore.core.api.reactive.ReactiveObjectStore
    fun async(credentials: CREDENTIALS): me.ahoo.costore.core.api.async.AsyncObjectStore
    fun coroutines(credentials: CREDENTIALS): me.ahoo.costore.core.api.coroutines.CoroutinesObjectStore
}
