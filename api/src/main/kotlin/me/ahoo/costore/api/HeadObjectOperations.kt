package me.ahoo.costore.api

interface HeadObjectOperations {
    fun headObject(request: HeadObjectRequest): HeadObjectResponse
}

interface HeadObjectRequest

interface HeadObjectResponse