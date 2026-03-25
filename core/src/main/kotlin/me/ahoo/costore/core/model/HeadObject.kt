package me.ahoo.costore.core.model

interface HeadObjectRequest :
    BucketCapable,
    ObjectKeyCapable

typealias HeadObjectResponse = StoredObjectMetadata
