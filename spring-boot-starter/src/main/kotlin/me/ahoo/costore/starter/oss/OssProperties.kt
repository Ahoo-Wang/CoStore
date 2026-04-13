package me.ahoo.costore.starter.oss

import me.ahoo.costore.core.CoStore
import me.ahoo.costore.core.provider.CommonStoreProviderCredentials
import me.ahoo.costore.core.provider.EndpointCapable
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "${CoStore.BRAND_PREFIX}oss")
data class OssProperties(
    override var endpoint: String = "",
    override var accessKey: String = "",
    override var secretKey: String = "",
) : CommonStoreProviderCredentials, EndpointCapable {

    fun toCredentials(): OssCredentials = OssCredentials(
        endpoint = endpoint,
        accessKey = accessKey,
        secretKey = secretKey
    )
}
