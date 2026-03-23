package me.ahoo.costore.core.api.sync

import me.ahoo.costore.core.model.PresignDeleteObjectRequest
import me.ahoo.costore.core.model.PresignDeleteObjectResponse
import me.ahoo.costore.core.model.PresignGetObjectRequest
import me.ahoo.costore.core.model.PresignGetObjectResponse
import me.ahoo.costore.core.model.PresignPutObjectRequest
import me.ahoo.costore.core.model.PresignPutObjectResponse

interface PresignObjectOperations {
    fun presignGetObject(request: PresignGetObjectRequest): PresignGetObjectResponse
    fun presignPutObject(request: PresignPutObjectRequest): PresignPutObjectResponse
    fun presignDeleteObject(request: PresignDeleteObjectRequest): PresignDeleteObjectResponse
}
