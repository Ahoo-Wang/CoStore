package me.ahoo.costore.starter.oss

import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.oss.provider.OssCredentials
import me.ahoo.costore.oss.provider.OssObjectStoreProvider
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OssObjectStoreProviderTest {
    private lateinit var provider: OssObjectStoreProvider

    @BeforeEach
    fun setup() {
        provider = OssObjectStoreProvider()
    }

    @Test
    fun `should create ObjectStore from OssCredentials`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-hangzhou.aliyuncs.com",
            accessKeyId = "test-access-key",
            secretAccessKey = "test-secret-key"
        )

        val store = provider.sync(credentials)

        store.assert().isInstanceOf(ObjectStore::class.java)
    }

    @Test
    fun `should create ObjectStore that can be closed`() {
        val credentials = OssCredentials(
            endpoint = "https://oss-cn-beijing.aliyuncs.com",
            accessKeyId = "my-access-key",
            secretAccessKey = "my-secret-key"
        )

        val store = provider.sync(credentials)

        store.close()
    }
}
