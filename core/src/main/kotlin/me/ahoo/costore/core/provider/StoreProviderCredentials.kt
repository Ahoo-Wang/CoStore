package me.ahoo.costore.core.provider

/**
 * Marker interface for credentials used by [ObjectStoreProvider].
 *
 * Each provider implementation defines its own credentials type that extends this interface.
 */
interface StoreProviderCredentials

/**
 * Common credentials interface for S3-compatible services that use access key authentication.
 *
 * Both AWS S3 and Aliyun OSS use this pattern of access key ID and secret access key.
 */
interface CommonStoreProviderCredentials : StoreProviderCredentials {
    /** The access key ID used for authentication. */
    val accessKeyId: String

    /** The secret access key used for authentication. */
    val secretAccessKey: String
}
