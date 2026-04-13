package me.ahoo.costore.starter.oss

import me.ahoo.costore.core.CoStore
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "${CoStore.BRAND_PREFIX}oss")
class OssProperties {
    var endpoint: String = ""
    var accessKey: String = ""
    var secretKey: String = ""

    fun toCredentials(): OssCredentials = OssCredentials(
        endpoint = endpoint,
        accessKey = accessKey,
        secretKey = secretKey
    )
}
