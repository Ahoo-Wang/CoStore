package me.ahoo.costore.core.provider

interface StoreProviderCredentials

interface CommonStoreProviderCredentials : StoreProviderCredentials {
    val accessKeyId: String
    val secretAccessKey: String
}
