package me.ahoo.costore.starter.s3

import me.ahoo.costore.core.provider.StoreProviderCredentials
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class S3CredentialsTest {
    @Test
    fun `should implement StoreProviderCredentials`() {
        val credentials = S3Credentials(
            region = "us-east-1",
            accessKey = "test-access-key",
            secretKey = "test-secret-key"
        )

        credentials.assert().isInstanceOf(StoreProviderCredentials::class.java)
    }

    @Test
    fun `should store all properties`() {
        val credentials = S3Credentials(
            region = "us-west-2",
            accessKey = "my-access-key",
            secretKey = "my-secret-key"
        )

        with(credentials) {
            region.assert().isEqualTo("us-west-2")
            accessKey.assert().isEqualTo("my-access-key")
            secretKey.assert().isEqualTo("my-secret-key")
        }
    }
}
