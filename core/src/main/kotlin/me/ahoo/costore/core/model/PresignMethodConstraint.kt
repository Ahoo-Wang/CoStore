package me.ahoo.costore.core.model

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates that presign method is one of the allowed values: GET, PUT, DELETE.
 */
@Constraint(validatedBy = [])
@NotBlank(message = PresignMethodConstraint.BLANK_MESSAGE)
@Pattern(
    regexp = PresignMethodConstraint.METHOD_PATTERN,
    message = PresignMethodConstraint.PATTERN_MESSAGE
)
annotation class PresignMethodConstraint(
    val message: String = "",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    companion object {
        const val METHOD_PATTERN = "^(GET|PUT|DELETE)$"
        const val BLANK_MESSAGE = "Presign method must not be blank"
        const val PATTERN_MESSAGE = "Presign method must be one of: GET, PUT, DELETE"
    }
}
