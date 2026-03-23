package me.ahoo.costore.core.api.sync

interface ObjectStore :
    GetObjectOperations,
    PutObjectOperations,
    DeleteObjectOperations,
    ListObjectsOperations,
    PresignObjectOperations,
    AutoCloseable
