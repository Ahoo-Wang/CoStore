package me.ahoo.costore.core.api.reactive

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.ahoo.costore.core.api.sync.ObjectStore
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
import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultReactiveObjectStoreTest {
    private lateinit var delegate: ObjectStore
    private lateinit var store: DefaultReactiveObjectStore

    @BeforeEach
    fun setup() {
        delegate = mockk()
        store = DefaultReactiveObjectStore(delegate)
    }

    @Test
    fun `should delegate getObject`() {
        val request = mockk<GetObjectRequest>()
        val response = mockk<GetObjectResponse>()
        every { delegate.getObject(request) } returns response

        store.getObject(request).block()

        verify { delegate.getObject(request) }
    }

    @Test
    fun `should delegate putObject`() {
        val request = mockk<PutObjectRequest>()
        val response = mockk<PutObjectResponse>()
        every { delegate.putObject(request) } returns response

        store.putObject(request).block()

        verify { delegate.putObject(request) }
    }

    @Test
    fun `should delegate deleteObject`() {
        val request = mockk<DeleteObjectRequest>()
        val response = mockk<DeleteObjectResponse>()
        every { delegate.deleteObject(request) } returns response

        store.deleteObject(request).block()

        verify { delegate.deleteObject(request) }
    }

    @Test
    fun `should delegate listObjects`() {
        val request = mockk<ListObjectsRequest>()
        val response = mockk<ListObjectsResponse>()
        every { delegate.listObjects(request) } returns response

        store.listObjects(request).block()

        verify { delegate.listObjects(request) }
    }

    @Test
    fun `should delegate headObject`() {
        val request = mockk<HeadObjectRequest>()
        val response = mockk<HeadObjectResponse>()
        every { delegate.headObject(request) } returns response

        store.headObject(request).block()

        verify { delegate.headObject(request) }
    }

    @Test
    fun `should delegate presignGetObject`() {
        val request = mockk<PresignGetObjectRequest>()
        val response = mockk<PresignGetObjectResponse>()
        every { delegate.presignGetObject(request) } returns response

        store.presignGetObject(request).block()

        verify { delegate.presignGetObject(request) }
    }

    @Test
    fun `should delegate presignPutObject`() {
        val request = mockk<PresignPutObjectRequest>()
        val response = mockk<PresignPutObjectResponse>()
        every { delegate.presignPutObject(request) } returns response

        store.presignPutObject(request).block()

        verify { delegate.presignPutObject(request) }
    }

    @Test
    fun `should delegate presignDeleteObject`() {
        val request = mockk<PresignDeleteObjectRequest>()
        val response = mockk<PresignDeleteObjectResponse>()
        every { delegate.presignDeleteObject(request) } returns response

        store.presignDeleteObject(request).block()

        verify { delegate.presignDeleteObject(request) }
    }

    @Test
    fun `should delegate close`() {
        every { delegate.close() } returns Unit

        store.close().block()

        verify { delegate.close() }
    }

    @Test
    fun `should delegate presignObjects`() {
        val request = mockk<BatchPresignRequest>()
        val response = mockk<BatchPresignResponse>()
        every { delegate.presignObjects(request) } returns response

        store.presignObjects(request).block()

        verify { delegate.presignObjects(request) }
    }
}
