package me.ahoo.costore.core.model

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import kotlin.reflect.KClass

/**
 * Validates that content type is a valid MIME type format.
 */
@Constraint(validatedBy = [])
@NotBlank(message = ContentTypeConstraint.BLANK_MESSAGE)
@Pattern(
    regexp = ContentTypeConstraint.MIME_TYPE_PATTERN,
    message = ContentTypeConstraint.PATTERN_MESSAGE
)
annotation class ContentTypeConstraint(
    val message: String = "",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    companion object {
        // Basic MIME type pattern: type/subtype
        const val MIME_TYPE_PATTERN = "^[^/]+/[^/]+$"
        const val BLANK_MESSAGE = "Content type must not be blank"
        const val PATTERN_MESSAGE = "Content type must be a valid MIME type format"
    }
}
