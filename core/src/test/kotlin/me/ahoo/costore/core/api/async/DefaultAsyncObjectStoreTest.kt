package me.ahoo.costore.core.api.async

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.ahoo.costore.core.api.sync.ObjectStore
import me.ahoo.costore.core.model.BatchPresignRequest
import me.ahoo.costore.core.model.BatchPresignResponse
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DefaultAsyncObjectStoreTest {
    private lateinit var delegate: ObjectStore
    private lateinit var executor: Executor
    private lateinit var store: DefaultAsyncObjectStore

    @BeforeEach
    fun setup() {
        delegate = mockk()
        executor = Executors.newCachedThreadPool()
        store = DefaultAsyncObjectStore(delegate, executor)
    }

    @Test
    fun `should delegate getObject`() {
        val bucket = "test-bucket" as BucketName
        val key = "test-key" as ObjectKey
        val request = mockk<GetObjectRequest>()
        val response = mockk<GetObjectResponse>()
        every { request.bucket } returns bucket
        every { request.key } returns key
        every { delegate.getObject(request) } returns response

        val future = store.getObject(request)
        future.join()

        verify { delegate.getObject(request) }
    }

    @Test
    fun `should delegate putObject`() {
        val request = mockk<PutObjectRequest>()
        val response = mockk<PutObjectResponse>()
        every { delegate.putObject(request) } returns response

        val future = store.putObject(request)
        future.join()

        verify { delegate.putObject(request) }
    }

    @Test
    fun `should delegate deleteObject`() {
        val request = mockk<DeleteObjectRequest>()
        val response = mockk<DeleteObjectResponse>()
        every { delegate.deleteObject(request) } returns response

        val future = store.deleteObject(request)
        future.join()

        verify { delegate.deleteObject(request) }
    }

    @Test
    fun `should delegate listObjects`() {
        val request = mockk<ListObjectsRequest>()
        val response = mockk<ListObjectsResponse>()
        every { delegate.listObjects(request) } returns response

        val future = store.listObjects(request)
        future.join()

        verify { delegate.listObjects(request) }
    }

    @Test
    fun `should delegate headObject`() {
        val request = mockk<HeadObjectRequest>()
        val response = mockk<HeadObjectResponse>()
        every { delegate.headObject(request) } returns response

        val future = store.headObject(request)
        future.join()

        verify { delegate.headObject(request) }
    }

    @Test
    fun `should delegate presignGetObject`() {
        val request = mockk<PresignGetObjectRequest>()
        val response = mockk<PresignGetObjectResponse>()
        every { delegate.presignGetObject(request) } returns response

        val future = store.presignGetObject(request)
        future.join()

        verify { delegate.presignGetObject(request) }
    }

    @Test
    fun `should delegate presignPutObject`() {
        val request = mockk<PresignPutObjectRequest>()
        val response = mockk<PresignPutObjectResponse>()
        every { delegate.presignPutObject(request) } returns response

        val future = store.presignPutObject(request)
        future.join()

        verify { delegate.presignPutObject(request) }
    }

    @Test
    fun `should delegate presignDeleteObject`() {
        val request = mockk<PresignDeleteObjectRequest>()
        val response = mockk<PresignDeleteObjectResponse>()
        every { delegate.presignDeleteObject(request) } returns response

        val future = store.presignDeleteObject(request)
        future.join()

        verify { delegate.presignDeleteObject(request) }
    }

    @Test
    fun `should delegate close`() {
        every { delegate.close() } returns Unit

        val future = store.close()
        future.join()

        verify { delegate.close() }
    }

    @Test
    fun `should delegate presignObjects`() {
        val request = mockk<BatchPresignRequest>()
        val response = mockk<BatchPresignResponse>()
        every { delegate.presignObjects(request) } returns response

        val future = store.presignObjects(request)
        future.join()

        verify { delegate.presignObjects(request) }
    }
}
