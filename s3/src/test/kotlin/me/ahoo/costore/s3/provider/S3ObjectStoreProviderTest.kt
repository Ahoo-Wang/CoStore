package me.ahoo.costore.s3.provider

import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class S3ObjectStoreProviderTest {

    private lateinit var provider: S3ObjectStoreProvider

    @BeforeEach
    fun setup() {
        provider = S3ObjectStoreProvider()
    }

    @Test
    fun `should create sync ObjectStore from S3Credentials`() {
        val credentials = S3Credentials(
            accessKey = "test-access-key",
            secretKey = "test-secret-key",
            region = "us-east-1"
        )

        val store = provider.sync(credentials)

        store.assert().isInstanceOf(ObjectStore::class.java)
    }

    @Test
    fun `should create ObjectStore that can be closed`() {
        val credentials = S3Credentials(
            accessKey = "my-access-key",
            secretKey = "my-secret-key",
            region = "us-west-2"
        )

        val store = provider.sync(credentials)

        store.close()
    }
}
