package me.ahoo.costore.starter.s3

import me.ahoo.costore.core.CoStore
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "${CoStore.BRAND_PREFIX}s3")
class S3Properties {
    var region: String = ""
    var accessKey: String = ""
    var secretKey: String = ""

    fun toCredentials(): S3Credentials = S3Credentials(
        region = region,
        accessKey = accessKey,
        secretKey = secretKey
    )
}
