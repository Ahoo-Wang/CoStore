package me.ahoo.costore.core.model

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates that an object key does not contain control characters.
 *
 * S3 object keys can contain any UTF-8 characters except control characters.
 */
@Constraint(validatedBy = [])
@NotBlank(message = ObjectKeyConstraint.BLANK_MESSAGE)
@Pattern(
    regexp = ObjectKeyConstraint.KEY_PATTERN,
    message = ObjectKeyConstraint.PATTERN_MESSAGE
)
annotation class ObjectKeyConstraint(
    val message: String = "",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    companion object {
        const val KEY_PATTERN = "^[^\\n\\r\\t]+$"
        const val BLANK_MESSAGE = "Object key must not be blank"
        const val PATTERN_MESSAGE = "Object key must not contain control characters"
    }
}
