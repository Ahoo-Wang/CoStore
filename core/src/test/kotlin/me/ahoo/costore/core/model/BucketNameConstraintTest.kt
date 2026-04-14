package me.ahoo.costore.core.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
            "bucket123",
            "a1b",
            "abc-def.ghi-jkl.mno"
        )
        for (bucket in validBuckets) {
            val violations = validator.validate(BucketTest(bucket))
            violations.size.assert().isEqualTo(0)
        }
    }

    @Test
    fun `blank bucket should fail validation`() {
        val violations = validator.validate(BucketTest(""))
        violations.size.assert().isGreaterThan(0)
    }

    @Test
    fun `bucket shorter than 3 characters should fail validation`() {
        val violations = validator.validate(BucketTest("ab"))
        violations.size.assert().isGreaterThan(0)
    }

    @Test
    fun `bucket longer than 63 characters should fail validation`() {
        val violations = validator.validate(BucketTest("a".repeat(64)))
        violations.size.assert().isGreaterThan(0)
    }

    data class BucketTest(@get:BucketNameConstraint val bucket: String)
}
