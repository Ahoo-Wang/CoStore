package me.ahoo.costore.core.api.sync

import me.ahoo.costore.core.model.DeleteObjectRequest
import me.ahoo.costore.core.model.DeleteObjectResponse

interface DeleteObjectOperations {
    fun deleteObject(request: DeleteObjectRequest): DeleteObjectResponse
}
