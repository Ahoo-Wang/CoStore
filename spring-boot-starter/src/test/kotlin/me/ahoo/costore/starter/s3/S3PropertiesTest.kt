package me.ahoo.costore.starter.s3

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class S3PropertiesTest {
    @Test
    fun `should create credentials from properties`() {
        val properties = S3Properties().apply {
            region = "us-east-1"
            accessKeyId = "test-access-key"
            secretAccessKey = "test-secret-key"
        }

        val credentials = properties.toCredentials()

        with(credentials) {
            region.assert().isEqualTo("us-east-1")
            accessKeyId.assert().isEqualTo("test-access-key")
            secretAccessKey.assert().isEqualTo("test-secret-key")
        }
    }
}
