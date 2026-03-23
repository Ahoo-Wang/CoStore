package me.ahoo.costore.core.api.sync

interface ObjectStore :
    GetObjectOperations,
    HeadObjectOperations,
    PutObjectOperations,
    DeleteObjectOperations,
    ListObjectsOperations,
    PresignObjectOperations,
    AutoCloseable
