package me.ahoo.costore.starter.oss

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "costore.oss")
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
