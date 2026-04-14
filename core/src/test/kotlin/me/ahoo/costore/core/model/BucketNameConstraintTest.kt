package me.ahoo.costore.core.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BucketNameConstraintTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `valid bucket names should pass validation`() {
        val validBuckets = listOf(
            "valid-bucket",
            "valid.bucket",
            "valid_bucket",
            "bucket123",
            "a1b",
            "abc-def.ghi-jkl.mno"
        )
        for (bucket in validBuckets) {
            val violations = validator.validate(BucketTest(bucket))
            assertThat(violations).isEmpty()
        }
    }

    @Test
    fun `blank bucket should fail validation`() {
        val violations = validator.validate(BucketTest(""))
        assertThat(violations).isNotEmpty()
        assertThat(violations.first().message).contains("blank")
    }

    @ParameterizedTest
    @ValueSource(strings = ["bucket\nname", "bucket\rname", "bucket\tname"])
    fun `bucket with control characters should fail validation`(bucket: String) {
        val violations = validator.validate(BucketTest(bucket))
        assertThat(violations).isNotEmpty()
    }

    @Test
    fun `bucket shorter than 3 characters should fail validation`() {
        val violations = validator.validate(BucketTest("ab"))
        assertThat(violations).isNotEmpty()
    }

    @Test
    fun `bucket longer than 63 characters should fail validation`() {
        val violations = validator.validate(BucketTest("a".repeat(64)))
        assertThat(violations).isNotEmpty()
    }

    data class BucketTest(@get:BucketNameConstraint val bucket: String)
}