package me.ahoo.costore.starter.oss

import me.ahoo.costore.core.CoStore
import me.ahoo.costore.oss.provider.OssObjectStoreProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(OssObjectStoreProvider::class)
@ConditionalOnProperty(prefix = "${CoStore.BRAND_PREFIX}oss", name = ["endpoint"])
@EnableConfigurationProperties(OssProperties::class)
class OssAutoConfiguration(
    private val ossProperties: OssProperties
) {
    @Bean
    @ConditionalOnMissingBean
    fun ossObjectStoreProvider(): OssObjectStoreProvider = OssObjectStoreProvider()

    @Bean
    @ConditionalOnMissingBean
    fun ossObjectStore(provider: OssObjectStoreProvider) = provider.sync(ossProperties.toCredentials())
}
