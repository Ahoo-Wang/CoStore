package me.ahoo.costore.core.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

/**
 * Validates that an object key does not contain control characters.
 *
 * S3 object keys can contain any UTF-8 characters except control characters.
 */
@Target(PROPERTY_GETTER, FIELD)
@Retention(RUNTIME)
@NotBlank
@Pattern(regexp = "^[^\\n\\r\\t]+$", message = "Object key must not contain control characters")
annotation class ObjectKeyConstraint
