package me.ahoo.costore.core.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListObjectsTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `should create ListObjectsRequest instance`() {
        val bucket = "test-bucket"
        val prefix = "prefix/"
        val delimiter = "/"
        val marker = "marker-key"
        val maxKeys = 100

        val request = ListObjectsRequest(
            bucket = bucket,
            prefix = prefix,
            delimiter = delimiter,
            marker = marker,
            maxKeys = maxKeys
        )

        with(request) {
            this.bucket.assert().isEqualTo(bucket)
            this.prefix.assert().isNotNull().isEqualTo(prefix)
            delimiter.assert().isNotNull().isEqualTo(delimiter)
            marker.assert().isNotNull().isEqualTo(marker)
            maxKeys.assert().isEqualTo(maxKeys)
        }
    }

    @Test
    fun `should create ListObjectsResponse instance`() {
        val objects =
            listOf(
                StoredObjectMetadata(
                    bucket = "test-bucket",
                    key = "key1",
                    contentLength = 1024L,
                    contentType = "application/json"
                ),
                StoredObjectMetadata(
                    bucket = "test-bucket",
                    key = "key2",
                    contentLength = 2048L,
                    contentType = "text/plain"
                ),
            )
        val commonPrefixes = listOf("prefix1/", "prefix2/")
        val isTruncated = true
        val nextMarker = "next-marker"

        val response = ListObjectsResponse(
            objects = objects,
            commonPrefixes = commonPrefixes,
            isTruncated = isTruncated,
            nextMarker = nextMarker
        )

        with(response) {
            objects.assert().hasSize(2)
            commonPrefixes.assert().hasSize(2).containsExactly("prefix1/", "prefix2/")
            this.isTruncated.assert().isTrue()
            nextMarker.assert().isNotNull().isEqualTo(nextMarker)
        }
    }

    @Test
    fun `should fail validation for maxKeys less than 1`() {
        val request = ListObjectsRequest(bucket = "bucket", maxKeys = 0)
        val violations = validator.validate(request)
        violations.size.assert().isGreaterThan(0)
    }

    @Test
    fun `should fail validation for maxKeys greater than 1000`() {
        val request = ListObjectsRequest(bucket = "bucket", maxKeys = 1001)
        val violations = validator.validate(request)
        violations.size.assert().isGreaterThan(0)
    }
}
