package me.ahoo.costore.api

interface PresignObjectOperations {
    fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse
    fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse
    fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse
}

interface PresignGetObjectRequest {
}

interface PresignGetObjectResponse {
}

interface PresignPutObjectRequest {
}

interface PresignPutObjectResponse {
}

interface PresignDeleteObjectRequest {
}
interface PresignDeleteObjectResponse {
}