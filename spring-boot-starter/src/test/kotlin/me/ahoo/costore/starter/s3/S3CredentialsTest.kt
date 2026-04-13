package me.ahoo.costore.starter.s3

import me.ahoo.costore.core.provider.StoreProviderCredentials
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class S3CredentialsTest {
    @Test
    fun `should implement StoreProviderCredentials`() {
        val credentials = S3Credentials(
            region = "us-east-1",
            accessKeyId = "test-access-key",
            secretAccessKey = "test-secret-key"
        )

        credentials.assert().isInstanceOf(StoreProviderCredentials::class.java)
    }

    @Test
    fun `should store all properties`() {
        val credentials = S3Credentials(
            region = "us-west-2",
            accessKeyId = "my-access-key",
            secretAccessKey = "my-secret-key"
        )

        with(credentials) {
            region.assert().isEqualTo("us-west-2")
            accessKeyId.assert().isEqualTo("my-access-key")
            secretAccessKey.assert().isEqualTo("my-secret-key")
        }
    }
}
