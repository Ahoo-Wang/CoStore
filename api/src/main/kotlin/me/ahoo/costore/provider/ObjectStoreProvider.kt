package me.ahoo.costore.provider

import me.ahoo.costore.api.ObjectStore

interface ObjectStoreProvider<in CREDENTIALS : StoreProviderCredentials> {
    fun create(credentials: CREDENTIALS): ObjectStore
}
