package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class DeleteObjectTest {
    @Test
    fun `should create DeleteObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val versionId = "version-123"

        val request =
            object : DeleteObjectRequest {
                override val bucket: BucketName = bucket
                override val key: ObjectKey = key
                override val versionId: String? = versionId
            }

        with(request) {
            bucket.assert().isEqualTo(bucket)
            key.assert().isEqualTo(key)
            this.versionId.assert().isNotNull().isEqualTo(versionId)
        }
    }

    @Test
    fun `should create DeleteObjectResponse instance`() {
        val deleteMarker = true
        val versionId = "version-123"

        val response = DefaultDeleteObjectResponse(
            deleteMarker = deleteMarker,
            versionId = versionId
        )

        with(response) {
            deleteMarker.assert().isTrue()
            this.versionId.assert().isNotNull().isEqualTo(versionId)
        }
    }
}
