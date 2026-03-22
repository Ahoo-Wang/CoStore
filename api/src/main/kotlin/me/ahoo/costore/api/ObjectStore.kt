package me.ahoo.costore.api

interface ObjectStore : GetObjectOperations, PutObjectOperations, DeleteObjectOperations, ListObjectsOperations,
    PresignObjectOperations, AutoCloseable