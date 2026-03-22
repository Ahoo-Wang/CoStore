package me.ahoo.costore.api

interface DeleteObjectOperations {
    fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse
}

interface DeleteObjectRequest : ObjectRequest

interface DeleteObjectResponse : ObjectResponse