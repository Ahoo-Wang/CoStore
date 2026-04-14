package me.ahoo.costore.core.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

/**
 * Validates that a bucket name conforms to AWS S3 bucket naming rules.
 *
 * @see <a href="https://docs.aws.amazon.com/AmazonS3/latest/userguide/bucketnamingrules.html">S3 Bucket Naming Rules</a>
 */
@Target(PROPERTY_GETTER, FIELD)
@Retention(RUNTIME)
@NotBlank
@Size(min = 3, max = 63)
@Pattern(regexp = "^[a-z0-9][a-z0-9.-]{2,62}$", message = "Bucket name must conform to S3 naming rules")
annotation class BucketNameConstraint
