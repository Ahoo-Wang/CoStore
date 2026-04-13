package me.ahoo.costore.starter.s3

import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.s3.provider.S3ObjectStoreProvider
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations.of
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class S3AutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
        .withConfiguration(
            of(
                S3AutoConfiguration::class.java
            )
        )

    @Test
    fun `should configure S3ObjectStoreProvider when properties present`() {
        contextRunner
            .withPropertyValues(
                "costore.s3.region=us-east-1",
                "costore.s3.access-key-id=test-access-key",
                "costore.s3.secret-access-key=test-secret-key"
            )
            .run { context ->
                context.assertThat()
                    .hasSingleBean(S3ObjectStoreProvider::class.java)
                    .hasSingleBean(ObjectStore::class.java)
            }
    }

    @Test
    fun `should not configure when region is missing`() {
        contextRunner
            .withPropertyValues(
                "costore.s3.access-key-id=test-access-key",
                "costore.s3.secret-access-key=test-secret-key"
            )
            .run { context ->
                context.assertThat()
                    .doesNotHaveBean(S3ObjectStoreProvider::class.java)
                    .doesNotHaveBean(ObjectStore::class.java)
            }
    }

    @Test
    fun `should not configure when properties are empty`() {
        contextRunner.run { context ->
            context.assertThat()
                .doesNotHaveBean(S3ObjectStoreProvider::class.java)
                .doesNotHaveBean(ObjectStore::class.java)
        }
    }
}
