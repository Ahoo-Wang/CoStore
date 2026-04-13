package me.ahoo.costore.core.model

import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test

class ListObjectsTest {
    @Test
    fun `should create ListObjectsRequest instance`() {
        val bucket = "test-bucket"
        val prefix = "prefix/"
        val delimiter = "/"
        val marker = "marker-key"
        val maxKeys = 100

        val request =
            object : ListObjectsRequest {
                override val bucket: BucketName = bucket
                override val prefix: String? = prefix
                override val delimiter: String? = delimiter
                override val marker: String? = marker
                override val maxKeys: Int = maxKeys
            }

        with(request) {
            bucket.assert().isEqualTo(bucket)
            prefix.assert().isNotNull().isEqualTo(prefix)
            delimiter.assert().isNotNull().isEqualTo(delimiter)
            marker.assert().isNotNull().isEqualTo(marker)
            maxKeys.assert().isEqualTo(maxKeys)
        }
    }

    @Test
    fun `should create ListObjectsResponse instance`() {
        val objects =
            listOf(
                DefaultStoredObjectMetadata(
                    bucket = "test-bucket",
                    key = "key1",
                    contentLength = 1024L,
                    contentType = "application/json"
                ),
                DefaultStoredObjectMetadata(
                    bucket = "test-bucket",
                    key = "key2",
                    contentLength = 2048L,
                    contentType = "text/plain"
                ),
            )
        val commonPrefixes = listOf("prefix1/", "prefix2/")
        val isTruncated = true
        val nextMarker = "next-marker"

        val response = DefaultListObjectsResponse(
            objects = objects,
            commonPrefixes = commonPrefixes,
            isTruncated = isTruncated,
            nextMarker = nextMarker
        )

        with(response) {
            objects.assert().hasSize(2)
            commonPrefixes.assert().hasSize(2).containsExactly("prefix1/", "prefix2/")
            isTruncated.assert().isTrue()
            nextMarker.assert().isNotNull().isEqualTo(nextMarker)
        }
    }
}
