package me.ahoo.costore.core

import me.ahoo.costore.core.model.UploadPolicy
import me.ahoo.costore.core.model.UploadToken

/**
 * Synchronous credential and presigned-URL generation for cloud storage.
 *
 * All methods block the calling thread until the provider responds.
 *
 * @see SuspendCredentialsOperations the coroutine-friendly equivalent
 * @see StorageClient combines this with [ObjectOperations] into a single client interface
 */
interface CredentialsOperations {

    /**
     * Generates a temporary upload token governed by the given [UploadPolicy].
     *
     * The returned [UploadToken] contains everything a client needs to upload a file
     * directly to cloud storage without exposing permanent credentials. The token is
     * vendor-agnostic: it carries an upload URL, the HTTP method to use, any required
     * form fields (for POST-based providers), any required headers (for PUT-based
     * providers), and an expiry timestamp.
     *
     * @param policy the policy that constrains the allowed bucket, key prefix,
     *               content types, max content length, and token lifetime
     * @return [UploadToken] ready for use by the calling client
     */
    fun generateUploadToken(policy: UploadPolicy): UploadToken

    /**
     * Generates a presigned URL that allows downloading the specified object
     * without requiring cloud-provider credentials.
     *
     * @param bucket        the bucket containing the object
     * @param key           the object key
     * @param expireSeconds how many seconds the URL remains valid (default 3600)
     * @return a presigned download URL string
     */
    fun generatePresignedDownloadUrl(bucket: String, key: String, expireSeconds: Long = 3600): String
}

/**
 * Coroutine-friendly credential and presigned-URL generation for cloud storage.
 *
 * All methods are declared as `suspend fun` and are safe to call from any coroutine
 * without blocking the calling thread.
 *
 * @see CredentialsOperations the synchronous (blocking) equivalent
 * @see SuspendStorageClient combines this with [SuspendObjectOperations] into a single client interface
 */
interface SuspendCredentialsOperations {

    /**
     * Generates a temporary upload token governed by the given [UploadPolicy].
     *
     * @param policy the policy that constrains the allowed bucket, key prefix,
     *               content types, max content length, and token lifetime
     * @return [UploadToken] ready for use by the calling client
     */
    suspend fun generateUploadToken(policy: UploadPolicy): UploadToken

    /**
     * Generates a presigned URL that allows downloading the specified object
     * without requiring cloud-provider credentials.
     *
     * @param bucket        the bucket containing the object
     * @param key           the object key
     * @param expireSeconds how many seconds the URL remains valid (default 3600)
     * @return a presigned download URL string
     */
    suspend fun generatePresignedDownloadUrl(bucket: String, key: String, expireSeconds: Long = 3600): String
}
