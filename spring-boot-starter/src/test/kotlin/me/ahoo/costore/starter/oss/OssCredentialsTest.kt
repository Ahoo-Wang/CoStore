package me.ahoo.costore.starter.oss

import me.ahoo.costore.core.provider.StoreProviderCredentials
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class OssCredentialsTest {
    @Test
    fun `should implement StoreProviderCredentials`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-hangzhou.aliyuncs.com",
            accessKey = "test-access-key",
            secretKey = "test-secret-key"
        )

        credentials.assert().isInstanceOf(StoreProviderCredentials::class.java)
    }

    @Test
    fun `should store all properties`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-beijing.aliyuncs.com",
            accessKey = "my-access-key",
            secretKey = "my-secret-key"
        )

        with(credentials) {
            endpoint.assert().isEqualTo("https://oss-cn-beijing.aliyuncs.com")
            accessKey.assert().isEqualTo("my-access-key")
            secretKey.assert().isEqualTo("my-secret-key")
        }
    }
}
