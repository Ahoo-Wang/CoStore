package me.ahoo.costore.core.api.sync

import me.ahoo.costore.core.model.GetObjectRequest
import me.ahoo.costore.core.model.GetObjectResponse

interface GetObjectOperations {
    fun getObject(request: GetObjectRequest): GetObjectResponse
}
