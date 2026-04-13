package me.ahoo.costore.starter.s3

import me.ahoo.costore.core.provider.StoreProviderCredentials

data class S3Credentials(
    val region: String,
    val accessKey: String,
    val secretKey: String
) : StoreProviderCredentials
