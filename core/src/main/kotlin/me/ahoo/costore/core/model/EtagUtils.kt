package me.ahoo.costore.core.model

/**
 * Normalizes an ETag value by ensuring it is wrapped in double quotes.
 *
 * Some S3-compatible services return ETags without quotes, while the S3 API
 * specification requires quotes. This function ensures consistent formatting.
 *
 * @receiver The ETag string, possibly null
 * @return The normalized ETag with surrounding quotes, or null if input was null
 */
fun String?.normalizeEtag(): String? = this?.let {
    if (it.startsWith("\"")) it else "\"$it\""
}
