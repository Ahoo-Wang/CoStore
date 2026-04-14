package me.ahoo.costore.core.model

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import kotlin.reflect.KClass

/**
 * Validates that version ID is not blank.
 */
@Constraint(validatedBy = [])
@NotBlank(message = VersionIdConstraint.BLANK_MESSAGE)
annotation class VersionIdConstraint(
    val message: String = "",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    companion object {
        const val BLANK_MESSAGE = "Version ID must not be blank"
    }
}
