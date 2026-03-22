package me.ahoo.costore.api

interface GetObjectOperations {
    fun getObject(request: GetObjectRequest): GetObjectResponse
}

interface GetObjectRequest

interface GetObjectResponse