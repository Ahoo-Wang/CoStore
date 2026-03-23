package me.ahoo.costore.core.api.sync

import me.ahoo.costore.core.model.HeadObjectRequest
import me.ahoo.costore.core.model.HeadObjectResponse

interface HeadObjectOperations {
    fun headObject(request: HeadObjectRequest): HeadObjectResponse
}
