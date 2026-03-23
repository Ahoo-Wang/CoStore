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

        request.bucket.assert().isEqualTo(bucket)
        request.key.assert().isEqualTo(key)
        request.versionId.assert().isEqualTo(versionId)
    }

    @Test
    fun `should create DeleteObjectResponse instance`() {
        val deleteMarker = true
        val versionId = "version-123"

        val response =
            object : DeleteObjectResponse {
                override val deleteMarker: Boolean = deleteMarker
                override val versionId: String? = versionId
            }

        response.deleteMarker.assert().isTrue()
        response.versionId.assert().isEqualTo(versionId)
    }
}
