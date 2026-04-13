package me.ahoo.costore.core

/**
 * CoStore library branding and version information.
 *
 * Provides project-wide constants for brand identification and version reporting.
 */
object CoStore {
    /** Brand name identifier for the library. */
    const val BRAND = "costore"

    /** Brand prefix used for namespacing (e.g., in configuration keys). */
    const val BRAND_PREFIX = "$BRAND."

    /** Current library version, derived from the package manifest. */
    val VERSION = BRAND.javaClass.`package`.implementationVersion.orEmpty()
}
