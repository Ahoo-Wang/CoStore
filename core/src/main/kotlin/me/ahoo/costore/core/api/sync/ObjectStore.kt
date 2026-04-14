package me.ahoo.costore.core.api.sync

import me.ahoo.costore.core.model.BatchPresignRequest
import me.ahoo.costore.core.model.BatchPresignResponse
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.DeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.PresignDeleteObjectRequest
import me.ahoo.costore.core.model.PresignDeleteObjectResponse
import me.ahoo.costore.core.model.PresignGetObjectRequest
import me.ahoo.costore.core.model.PresignGetObjectResponse
import me.ahoo.costore.core.model.PresignPutObjectRequest
import me.ahoo.costore.core.model.PresignPutObjectResponse
import me.ahoo.costore.core.model.PresignRequest
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse

/**
 * Synchronous object store interface for blocking I/O operations.
 *
 * This is the base interface for all object store implementations. It provides
 * blocking operations for managing objects in S3-compatible storage backends.
 *
 * Implementations are auto-closeable and should be used within a try-with-resources block
 * or explicitly closed after use.
 */
interface ObjectStore :
    GetObjectOperations,
    HeadObjectOperations,
    PutObjectOperations,
    DeleteObjectOperations,
    ListObjectsOperations,
    PresignObjectOperations,
    AutoCloseable

/**
 * Deletes an object from the store.
 *
 * @param request The delete object request containing bucket and key
 * @return The result of the delete operation
 */
interface DeleteObjectOperations {
    fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse
}

/**
 * Retrieves an object from the store.
 *
 * @param request The get object request containing bucket, key, and optional parameters
 * @return The object content and metadata
 * @throws ObjectNotFoundError if the object does not exist
 * @see StoredObject.content The returned InputStream must be closed by the caller to avoid resource leaks
 */
interface GetObjectOperations {
    fun getObject(request: GetObjectRequest): GetObjectResponse
}

/**
 * Retrieves object metadata without the content.
 *
 * @param request The head object request containing bucket and key
 * @return Object metadata (content length, type, etag, etc.)
 * @throws ObjectNotFoundError if the object does not exist
 */
interface HeadObjectOperations {
    fun headObject(request: HeadObjectRequest): HeadObjectResponse
}

/**
 * Lists objects in a bucket with optional prefix filtering.
 *
 * @param request The list objects request containing bucket and filter parameters
 * @return Paginated list of objects and common prefixes
 */
interface ListObjectsOperations {
    fun listObjects(request: ListObjectsRequest): ListObjectsResponse
}

/**
 * Stores an object in the bucket.
 *
 * @param request The put object request containing bucket, key, content, and metadata
 * @return The result of the put operation (etag, versionId, etc.)
 */
interface PutObjectOperations {
    fun putObject(request: PutObjectRequest): PutObjectResponse
}

/**
 * Generates pre-signed URLs for temporary object access.
 *
 * Pre-signed URLs allow temporary access to private objects without requiring
 * AWS credentials in the request.
 */
interface PresignObjectOperations {
    fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse
    fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse

    /**
     * Generates a pre-signed URL for deleting an object.
     *
     * @throws UnsupportedOperationException if the underlying storage does not support
     *         pre-signed delete URLs (e.g., OSS)
     */
    @Throws(UnsupportedOperationException::class)
    fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse

    fun presignObjects(request: BatchPresignRequest): BatchPresignResponse {
        val responses = request.requests.map { presignRequest ->
            when (presignRequest) {
                is PresignRequest.Get -> presignGetObject(presignRequest)
                is PresignRequest.Put -> presignPutObject(presignRequest)
                is PresignRequest.Delete -> presignDeleteObject(presignRequest)
            }
        }
        return BatchPresignResponse(responses)
    }
}
