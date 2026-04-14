package me.ahoo.costore.starter.oss

import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.oss.provider.OssObjectStoreProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations.of
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class OssAutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
        .withConfiguration(
            of(
                OssAutoConfiguration::class.java
            )
        )

    @Test
    fun `should configure OssObjectStoreProvider when properties present`() {
        contextRunner
            .withPropertyValues(
                "costore.oss.endpoint=https://oss-cn-hangzhou.aliyuncs.com",
                "costore.oss.access-key-id=test-access-key",
                "costore.oss.secret-access-key=test-secret-key"
            )
            .run { context ->
                assertThat(context)
                    .hasSingleBean(OssObjectStoreProvider::class.java)
                    .hasSingleBean(ObjectStore::class.java)
            }
    }

    @Test
    fun `should not configure when endpoint is missing`() {
        contextRunner
            .withPropertyValues(
                "costore.oss.access-key-id=test-access-key",
                "costore.oss.secret-access-key=test-secret-key"
            )
            .run { context ->
                assertThat(context)
                    .doesNotHaveBean(OssObjectStoreProvider::class.java)
                    .doesNotHaveBean(ObjectStore::class.java)
            }
    }

    @Test
    fun `should not configure when properties are empty`() {
        contextRunner.run { context ->
            assertThat(context)
                .doesNotHaveBean(OssObjectStoreProvider::class.java)
                .doesNotHaveBean(ObjectStore::class.java)
        }
    }
}
