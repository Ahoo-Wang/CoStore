package me.ahoo.costore.core.api.sync

import me.ahoo.costore.core.model.ListObjectsRequest
import me.ahoo.costore.core.model.ListObjectsResponse

interface ListObjectsOperations {
    fun listObjects(request: ListObjectsRequest): ListObjectsResponse
}
