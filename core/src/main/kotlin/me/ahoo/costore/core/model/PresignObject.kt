package me.ahoo.costore.core.model

import java.time.Duration

interface PresignGetObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val expiration: Duration
}

interface PresignGetObjectResponse {
    val presignedUrl: String
}

interface PresignPutObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val expiration: Duration
    val contentType: String?
}

interface PresignPutObjectResponse {
    val presignedUrl: String
}

interface PresignDeleteObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val expiration: Duration
}

interface PresignDeleteObjectResponse {
    val presignedUrl: String
}
