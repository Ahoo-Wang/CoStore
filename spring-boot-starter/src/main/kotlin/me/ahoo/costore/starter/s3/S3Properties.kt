package me.ahoo.costore.starter.s3

import me.ahoo.costore.core.CoStore
import me.ahoo.costore.core.provider.CommonStoreProviderCredentials
import me.ahoo.costore.core.provider.NullableEndpointCapable
import me.ahoo.costore.core.provider.NullableRegionCapable
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "${CoStore.BRAND_PREFIX}s3")
class S3Properties(
    override var accessKey: String = "",
    override var secretKey: String = "",
    override var region: String? = null,
    override var endpoint: String? = null,
) : NullableRegionCapable, CommonStoreProviderCredentials, NullableEndpointCapable {

    fun toCredentials(): S3Credentials = S3Credentials(
        region = region,
        accessKey = accessKey,
        secretKey = secretKey
    )
}
