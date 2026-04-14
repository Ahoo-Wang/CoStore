package me.ahoo.costore.core.model

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.PositiveOrZero
import kotlin.reflect.KClass

/**
 * Validates that content length is a non-negative value.
 */
@Constraint(validatedBy = [])
@PositiveOrZero(message = ContentLengthConstraint.POSITIVE_OR_ZERO_MESSAGE)
annotation class ContentLengthConstraint(
    val message: String = "",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    companion object {
        const val POSITIVE_OR_ZERO_MESSAGE = "Content length must be non-negative"
    }
}
