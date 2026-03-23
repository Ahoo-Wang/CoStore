package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class BucketCapableTest {
    @Test
    fun `should create BucketCapable instance`() {
        val bucketName = "test-bucket"
        val instance =
            object : BucketCapable {
                override val bucket: BucketName = bucketName
            }
        instance.bucket.assert().isEqualTo(bucketName)
    }

    @Test
    fun `BucketName typealias should be String`() {
        val bucketName: BucketName = "my-bucket"
        bucketName.assert().isEqualTo("my-bucket")
    }
}
