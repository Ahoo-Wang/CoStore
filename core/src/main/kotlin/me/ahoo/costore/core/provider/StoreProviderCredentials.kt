package me.ahoo.costore.core.provider

interface StoreProviderCredentials

interface CommonStoreProviderCredentials : StoreProviderCredentials {
    val accessKey: String
    val secretKey: String
}
