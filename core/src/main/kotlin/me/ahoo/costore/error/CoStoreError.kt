package me.ahoo.costore.error

open class CoStoreError(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
