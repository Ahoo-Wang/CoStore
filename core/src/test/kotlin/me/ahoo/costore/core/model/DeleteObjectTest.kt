package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class DeleteObjectTest {
    @Test
    fun `should create DeleteObjectRequest instance`() {
        val bucket = "test-bucket"
        val key = "test/key"
        val versionId = "version-123"

        val request = DeleteObjectRequest(
            bucket = bucket,
            key = key,
            versionId = versionId
        )

        with(request) {
            this.bucket.assert().isEqualTo(bucket)
            this.key.assert().isEqualTo(key)
            versionId.assert().isNotNull().isEqualTo(versionId)
        }
    }

    @Test
    fun `should create DeleteObjectResponse instance`() {
        val deleteMarker = true
        val versionId = "version-123"

        val response = DeleteObjectResponse(
            deleteMarker = deleteMarker,
            versionId = versionId
        )

        with(response) {
            this.deleteMarker.assert().isTrue()
            this.versionId.assert().isNotNull().isEqualTo(versionId)
        }
    }
}
