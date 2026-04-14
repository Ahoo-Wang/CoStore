package me.ahoo.costore.core.model

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlin.reflect.KClass

/**
 * Validates that a bucket name conforms to AWS S3 bucket naming rules.
 *
 * @see <a href="https://docs.aws.amazon.com/AmazonS3/latest/userguide/bucketnamingrules.html">S3 Bucket Naming Rules</a>
 */
@Constraint(validatedBy = [])
@NotBlank(message = BucketNameConstraint.BLANK_MESSAGE)
@Size(min = 3, max = 63, message = BucketNameConstraint.SIZE_MESSAGE)
@Pattern(
    regexp = BucketNameConstraint.BUCKET_NAME_PATTERN,
    message = BucketNameConstraint.PATTERN_MESSAGE
)
annotation class BucketNameConstraint(
    val message: String = "",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    companion object {
        const val BUCKET_NAME_PATTERN = "^[a-z0-9][a-z0-9.-]{2,62}$"
        const val BLANK_MESSAGE = "Bucket name must not be blank"
        const val SIZE_MESSAGE = "Bucket name must be between 3 and 63 characters"
        const val PATTERN_MESSAGE = "Bucket name must conform to S3 naming rules"
    }
}
