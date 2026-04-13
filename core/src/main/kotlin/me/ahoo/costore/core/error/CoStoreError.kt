package me.ahoo.costore.core.error

import me.ahoo.costore.core.model.BucketCapable
import me.ahoo.costore.core.model.BucketName
import me.ahoo.costore.core.model.ObjectKey
import me.ahoo.costore.core.model.ObjectKeyCapable

open class CoStoreError(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class ObjectNotFoundError(
    override val bucket: BucketName,
    override val key: ObjectKey,
    message: String = "Object not found: bucket='$bucket', key='$key'",
) :
    BucketCapable,
    ObjectKeyCapable, CoStoreError(message)
