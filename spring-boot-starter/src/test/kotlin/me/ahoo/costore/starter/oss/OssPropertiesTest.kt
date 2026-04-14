package me.ahoo.costore.starter.oss

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class OssPropertiesTest {
    @Test
    fun `should create credentials from properties`() {
        val properties = OssProperties().apply {
            endpoint = "https://oss-cn-hangzhou.aliyuncs.com"
            accessKeyId = "test-access-key"
            secretAccessKey = "test-secret-key"
        }

        val credentials = properties.toCredentials()

        with(credentials) {
            endpoint.assert().isEqualTo("https://oss-cn-hangzhou.aliyuncs.com")
            accessKeyId.assert().isEqualTo("test-access-key")
            secretAccessKey.assert().isEqualTo("test-secret-key")
        }
    }
}
