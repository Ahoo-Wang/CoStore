package me.ahoo.costore.core.api.sync

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.ahoo.costore.core.model.BatchPresignRequest
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.DeleteObjectResponse
import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.GetObjectResponse
import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.HeadObjectResponse
import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.PresignDeleteObjectRequest
import me.ahoo.costore.core.model.PresignDeleteObjectResponse
import me.ahoo.costore.core.model.PresignGetObjectRequest
import me.ahoo.costore.core.model.PresignGetObjectResponse
import me.ahoo.costore.core.model.PresignPutObjectRequest
import me.ahoo.costore.core.model.PresignPutObjectResponse
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import me.ahoo.test.asserts.assert
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.Duration
import java.time.Instant

/**
 * Test implementation of ObjectStore that uses the real default presignObjects implementation.
 * All individual methods are mocked, but presignObjects uses the default dispatch logic.
 */
class MockObjectStore : ObjectStore {
    val getObjectMock = mockk<(GetObjectRequest) -> GetObjectResponse>()
    val headObjectMock = mockk<(HeadObjectRequest) -> HeadObjectResponse>()
    val putObjectMock = mockk<(PutObjectRequest) -> PutObjectResponse>()
    val deleteObjectMock = mockk<(DeleteObjectRequest) -> DeleteObjectResponse>()
    val listObjectsMock = mockk<(ListObjectsRequest) -> ListObjectsResponse>()
    val presignGetObjectMock = mockk<(PresignGetObjectRequest) -> PresignGetObjectResponse>()
    val presignPutObjectMock = mockk<(PresignPutObjectRequest) -> PresignPutObjectResponse>()
    val presignDeleteObjectMock = mockk<(PresignDeleteObjectRequest) -> PresignDeleteObjectResponse>()

    override fun getObject(request: GetObjectRequest): GetObjectResponse = getObjectMock.invoke(request)
    override fun headObject(request: HeadObjectRequest): HeadObjectResponse = headObjectMock.invoke(request)
    override fun putObject(request: PutObjectRequest): PutObjectResponse = putObjectMock.invoke(request)
    override fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse = deleteObjectMock.invoke(request)
    override fun listObjects(request: ListObjectsRequest): ListObjectsResponse = listObjectsMock.invoke(request)
    override fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse =
        presignGetObjectMock.invoke(request)
    override fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse =
        presignPutObjectMock.invoke(request)
    override fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse =
        presignDeleteObjectMock.invoke(request)

    override fun close() = Unit
}

class ObjectStorePresignObjectsTest {

    @Test
    fun `should dispatch Get requests to presignGetObject`() {
        val store = MockObjectStore()
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val expiration = Duration.ofMinutes(15)

        val getRequest = PresignGetObjectRequest(bucket = bucket, key = key, expiration = expiration)
        val getResponse = PresignGetObjectResponse(
            url = URL("https://example.com/get"),
            expiration = Instant.now()
        )
        every { store.presignGetObjectMock.invoke(any()) } returns getResponse

        val batchRequest = BatchPresignRequest(requests = listOf(getRequest))
        store.presignObjects(batchRequest)

        verify { store.presignGetObjectMock.invoke(getRequest) }
    }

    @Test
    fun `should dispatch Put requests to presignPutObject`() {
        val store = MockObjectStore()
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val expiration = Duration.ofMinutes(15)

        val putRequest = PresignPutObjectRequest(
            bucket = bucket,
            key = key,
            expiration = expiration,
            contentType = "text/plain"
        )
        val putResponse = PresignPutObjectResponse(
            url = URL("https://example.com/put"),
            expiration = Instant.now()
        )
        every { store.presignPutObjectMock.invoke(any()) } returns putResponse

        val batchRequest = BatchPresignRequest(requests = listOf(putRequest))
        store.presignObjects(batchRequest)

        verify { store.presignPutObjectMock.invoke(putRequest) }
    }

    @Test
    fun `should dispatch Delete requests to presignDeleteObject`() {
        val store = MockObjectStore()
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val expiration = Duration.ofMinutes(15)

        val deleteRequest = PresignDeleteObjectRequest(
            bucket = bucket,
            key = key,
            expiration = expiration
        )
        val deleteResponse = PresignDeleteObjectResponse(
            url = URL("https://example.com/delete"),
            expiration = Instant.now(),
            headers = emptyMap()
        )
        every { store.presignDeleteObjectMock.invoke(any()) } returns deleteResponse

        val batchRequest = BatchPresignRequest(requests = listOf(deleteRequest))
        store.presignObjects(batchRequest)

        verify { store.presignDeleteObjectMock.invoke(deleteRequest) }
    }

    @Test
    fun `should dispatch mixed requests correctly`() {
        val store = MockObjectStore()
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val expiration = Duration.ofMinutes(15)

        val getRequest = PresignGetObjectRequest(bucket = bucket, key = key, expiration = expiration)
        val putRequest = PresignPutObjectRequest(
            bucket = bucket,
            key = key,
            expiration = expiration,
            contentType = "text/plain"
        )
        val deleteRequest = PresignDeleteObjectRequest(
            bucket = bucket,
            key = key,
            expiration = expiration
        )

        val getResponse = PresignGetObjectResponse(
            url = URL("https://example.com/get"),
            expiration = Instant.now()
        )
        val putResponse = PresignPutObjectResponse(
            url = URL("https://example.com/put"),
            expiration = Instant.now()
        )
        val deleteResponse = PresignDeleteObjectResponse(
            url = URL("https://example.com/delete"),
            expiration = Instant.now(),
            headers = emptyMap()
        )

        every { store.presignGetObjectMock.invoke(any()) } returns getResponse
        every { store.presignPutObjectMock.invoke(any()) } returns putResponse
        every { store.presignDeleteObjectMock.invoke(any()) } returns deleteResponse

        val batchRequest = BatchPresignRequest(
            requests = listOf(getRequest, putRequest, deleteRequest)
        )
        val response = store.presignObjects(batchRequest)

        verify { store.presignGetObjectMock.invoke(getRequest) }
        verify { store.presignPutObjectMock.invoke(putRequest) }
        verify { store.presignDeleteObjectMock.invoke(deleteRequest) }
        response.responses.assert().hasSize(3)
    }
}
