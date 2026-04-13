package me.ahoo.costore.starter.s3

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "costore.s3")
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
