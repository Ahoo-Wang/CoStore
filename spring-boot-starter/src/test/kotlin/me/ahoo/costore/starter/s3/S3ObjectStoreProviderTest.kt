package me.ahoo.costore.starter.s3

import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.s3.provider.S3Credentials
import me.ahoo.costore.s3.provider.S3ObjectStoreProvider
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
    fun `should create ObjectStore from S3Credentials`() {
        val credentials = S3Credentials(
            region = "us-east-1",
            accessKeyId = "test-access-key",
            secretAccessKey = "test-secret-key"
        )

        val store = provider.sync(credentials)

        store.assert().isInstanceOf(ObjectStore::class.java)
    }

    @Test
    fun `should create ObjectStore that can be closed`() {
        val credentials = S3Credentials(
            region = "us-west-2",
            accessKeyId = "my-access-key",
            secretAccessKey = "my-secret-key"
        )

        val store = provider.sync(credentials)

        store.close()
    }
}
