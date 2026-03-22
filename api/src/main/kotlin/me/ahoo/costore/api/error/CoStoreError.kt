package me.ahoo.costore.api.error

open class CoStoreError(message: String, cause: Throwable? = null) : RuntimeException(message, cause)