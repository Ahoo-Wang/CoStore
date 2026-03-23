package me.ahoo.costore.api

interface ListObjectsOperations {
    fun listObjects(request: ListObjectsRequest): ListObjectsResponse
}

interface ListObjectsRequest

interface ListObjectsResponse
