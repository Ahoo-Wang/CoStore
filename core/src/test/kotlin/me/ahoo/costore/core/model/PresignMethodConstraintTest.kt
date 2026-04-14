package me.ahoo.costore.core.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PresignMethodConstraintTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `valid presign methods should pass validation`() {
        val validMethods = listOf("GET", "PUT", "DELETE")
        for (method in validMethods) {
            val violations = validator.validate(MethodTest(method))
            violations.size.assert().isEqualTo(0)
        }
    }

    @Test
    fun `blank method should fail validation`() {
        val violations = validator.validate(MethodTest(""))
        violations.size.assert().isGreaterThan(0)
    }

    @ParameterizedTest
    @ValueSource(strings = ["GET ", " PUT", "get", "Post", "INVALID"])
    fun `invalid method should fail validation`(method: String) {
        val violations = validator.validate(MethodTest(method))
        violations.size.assert().isGreaterThan(0)
    }

    data class MethodTest(@get:PresignMethodConstraint val method: String)
}
