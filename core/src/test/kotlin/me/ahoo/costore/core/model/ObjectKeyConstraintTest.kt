package me.ahoo.costore.core.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ObjectKeyConstraintTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `valid object keys should pass validation`() {
        val validKeys = listOf(
            "valid/key",
            "valid.key",
            "valid_key",
            "key/with/multiple/segments",
            "file.txt",
            "path/to/file.pdf"
        )
        for (key in validKeys) {
            val violations = validator.validate(KeyTest(key))
            violations.size.assert().isEqualTo(0)
        }
    }

    @Test
    fun `blank key should fail validation`() {
        val violations = validator.validate(KeyTest(""))
        violations.size.assert().isGreaterThan(0)
    }

    @ParameterizedTest
    @ValueSource(strings = ["key\nname", "key\rname", "key\tname"])
    fun `key with control characters should fail validation`(key: String) {
        val violations = validator.validate(KeyTest(key))
        violations.size.assert().isGreaterThan(0)
    }

    data class KeyTest(@get:ObjectKeyConstraint val key: String)
}
