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

        request.bucket.assert().isEqualTo(bucket)
        request.prefix.assert().isEqualTo(prefix)
        request.delimiter.assert().isEqualTo(delimiter)
        request.marker.assert().isEqualTo(marker)
        request.maxKeys.assert().isEqualTo(maxKeys)
    }

    @Test
    fun `should create ListObjectsResponse instance`() {
        val objects =
            listOf(
                object : StoredObjectMetadata {
                    override val bucket: BucketName = "test-bucket"
                    override val key: ObjectKey = "key1"
                    override val contentLength: Long = 1024L
                    override val contentType: String? = "application/json"
                    override val lastModified: java.time.Instant? = null
                    override val eTag: String? = null
                    override val metadata: Map<String, String> = emptyMap()
                    override val storageClass: String? = "STANDARD"
                },
                object : StoredObjectMetadata {
                    override val bucket: BucketName = "test-bucket"
                    override val key: ObjectKey = "key2"
                    override val contentLength: Long = 2048L
                    override val contentType: String? = "text/plain"
                    override val lastModified: java.time.Instant? = null
                    override val eTag: String? = null
                    override val metadata: Map<String, String> = emptyMap()
                    override val storageClass: String? = "STANDARD"
                },
            )
        val commonPrefixes = listOf("prefix1/", "prefix2/")
        val isTruncated = true
        val nextMarker = "next-marker"

        val response =
            object : ListObjectsResponse {
                override val objects: List<StoredObjectMetadata> = objects
                override val commonPrefixes: List<String> = commonPrefixes
                override val isTruncated: Boolean = isTruncated
                override val nextMarker: String? = nextMarker
            }

        response.objects.assert().hasSize(2)
        response.commonPrefixes.assert().hasSize(2)
        response.isTruncated.assert().isTrue()
        response.nextMarker.assert().isEqualTo(nextMarker)
    }
}
