package me.ahoo.costore.core.model

typealias BucketName = String

interface BucketCapable {
    val bucket: BucketName
}
