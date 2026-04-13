package me.ahoo.costore.starter.s3

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class S3PropertiesTest {
    @Test
    fun `should create credentials from properties`() {
        val properties = S3Properties().apply {
            region = "us-east-1"
            accessKey = "test-access-key"
            secretKey = "test-secret-key"
        }

        val credentials = properties.toCredentials()

        with(credentials) {
            region.assert().isEqualTo("us-east-1")
            accessKey.assert().isEqualTo("test-access-key")
            secretKey.assert().isEqualTo("test-secret-key")
        }
    }
}
