package me.ahoo.costore.core.api.sync

import me.ahoo.costore.core.model.PutObjectRequest
import me.ahoo.costore.core.model.PutObjectResponse

interface PutObjectOperations {
    fun putObject(request: PutObjectRequest): PutObjectResponse
}
