package me.ahoo.costore.starter.oss

import me.ahoo.costore.core.provider.StoreProviderCredentials

data class OssCredentials(
    val endpoint: String,
    val accessKey: String,
    val secretKey: String
) : StoreProviderCredentials
