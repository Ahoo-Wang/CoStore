package me.ahoo.costore.core

object CoStore {
    const val BRAND = "costore"
    const val BRAND_PREFIX = "$BRAND."
    val VERSION = BRAND.javaClass.`package`.implementationVersion.orEmpty()
}
