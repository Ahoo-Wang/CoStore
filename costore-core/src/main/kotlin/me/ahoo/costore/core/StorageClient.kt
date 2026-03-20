package me.ahoo.costore.core

import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.costore.core.model.StorageObject
import me.ahoo.costore.core.model.UploadPolicy
import me.ahoo.costore.core.model.UploadToken

/**
 * Vendor-agnostic cloud storage client interface.
 *
 * Provides a unified API for common cloud storage operations (put, get, delete, list)
 * as well as generation of temporary upload tokens with policy restrictions,
 * supporting providers such as Alibaba Cloud OSS and AWS S3.
 */
interface StorageClient : AutoCloseable {

    /**
     * Uploads an object to the specified bucket.
     *
     * @param request the put-object request containing bucket, key, and content
     * @return [PutObjectResponse] containing the resulting ETag and optional version ID
     */
    fun putObject(request: PutObjectRequest): PutObjectResponse

    /**
     * Downloads an object from the specified bucket.
     *
     * @param request the get-object request containing bucket and key
     * @return [StorageObject] with content stream and metadata
     * @throws me.ahoo.costore.core.exception.ObjectNotFoundException if the object does not exist
     */
    fun getObject(request: GetObjectRequest): StorageObject

    /**
     * Deletes an object from the specified bucket.
     *
     * @param request the delete-object request containing bucket and key
     */
    fun deleteObject(request: DeleteObjectRequest)

    /**
     * Lists objects in a bucket, with optional prefix filtering and pagination.
     *
     * @param request the list-objects request
     * @return [ListObjectsResponse] with the page of results
     */
    fun listObjects(request: ListObjectsRequest): ListObjectsResponse

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
     * @param bucket     the bucket containing the object
     * @param key        the object key
     * @param expireSeconds how many seconds the URL remains valid (default 3600)
     * @return a presigned download URL string
     */
    fun generatePresignedDownloadUrl(bucket: String, key: String, expireSeconds: Long = 3600): String
}
