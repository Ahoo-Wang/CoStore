package me.ahoo.costore.starter.s3

import me.ahoo.costore.core.CoStore
import me.ahoo.costore.s3.provider.S3ObjectStoreProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(S3ObjectStoreProvider::class)
@ConditionalOnProperty(prefix = "${CoStore.BRAND_PREFIX}s3", name = ["region"])
@EnableConfigurationProperties(S3Properties::class)
class S3AutoConfiguration(
    private val s3Properties: S3Properties
) {
    @Bean
    @ConditionalOnMissingBean
    fun s3ObjectStoreProvider(): S3ObjectStoreProvider = S3ObjectStoreProvider()

    @Bean
    @ConditionalOnMissingBean
    fun s3ObjectStore(provider: S3ObjectStoreProvider) = provider.sync(s3Properties.toCredentials())
}
