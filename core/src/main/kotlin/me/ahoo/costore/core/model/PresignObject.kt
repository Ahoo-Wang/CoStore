package me.ahoo.costore.core.model

import java.time.Duration

interface PresignGetObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val expiration: Duration
}

interface PresignedUrlCapable {
    val presignedUrl: String
}

interface PresignGetObjectResponse : PresignedUrlCapable

interface PresignPutObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val expiration: Duration
    val contentType: String?
}

interface PresignPutObjectResponse : PresignedUrlCapable

interface PresignDeleteObjectRequest :
    BucketCapable,
    ObjectKeyCapable {
    val expiration: Duration
}

interface PresignDeleteObjectResponse : PresignedUrlCapable
