package me.ahoo.costore.core.model

import java.time.Instant

/**
 * Policy that constrains a temporary upload token.
 *
 * An [UploadPolicy] is passed to [me.ahoo.costore.core.StorageClient.generateUploadToken]
 * to produce an [UploadToken] that embeds these restrictions, so that the receiving
 * client can upload files directly to cloud storage without exposing permanent credentials.
 *
 * @property bucket              destination bucket name (required, must not be blank)
 * @property keyPrefix           object key prefix; when set, the uploaded object key must
 *                               start with this value. A UUID suffix is appended automatically
 *                               by the implementation when the full key is generated.
 * @property maxContentLength    maximum allowed file size in bytes; null means unrestricted
 * @property allowedContentTypes list of permitted MIME types (e.g. ["image/jpeg","image/png"]);
 *                               null means any content type is allowed
 * @property expireSeconds       token lifetime in seconds; must be a positive value
 *                               (default 3600 = 1 hour)
 */
data class UploadPolicy(
    val bucket: String,
    val keyPrefix: String? = null,
    val maxContentLength: Long? = null,
    val allowedContentTypes: List<String>? = null,
    val expireSeconds: Long = 3600,
) {
    init {
        require(bucket.isNotBlank()) { "bucket must not be blank" }
        require(expireSeconds > 0) { "expireSeconds must be positive, got $expireSeconds" }
        require(maxContentLength == null || maxContentLength > 0) {
            "maxContentLength must be positive when set, got $maxContentLength"
        }
    }

    /** The absolute expiry instant derived from the current wall clock and [expireSeconds]. */
    fun expiresAt(): Instant = Instant.now().plusSeconds(expireSeconds)
}
