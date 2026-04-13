package me.ahoo.costore.starter.oss

import me.ahoo.costore.core.CoStore
import me.ahoo.costore.core.provider.CommonStoreProviderCredentials
import me.ahoo.costore.core.provider.EndpointCapable
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "${CoStore.BRAND_PREFIX}oss")
data class OssProperties(
    override var endpoint: String = "",
    override var accessKeyId: String = "",
    override var secretAccessKey: String = "",
) : CommonStoreProviderCredentials, EndpointCapable {

    fun toCredentials(): OssCredentials = OssCredentials(
        endpoint = endpoint,
        accessKeyId = accessKeyId,
        secretAccessKey = secretAccessKey
    )
}
