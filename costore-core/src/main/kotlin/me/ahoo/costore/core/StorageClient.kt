package me.ahoo.costore.core

/**
 * Vendor-agnostic cloud storage client interface — **synchronous (blocking) API**.
 *
 * Combines [ObjectOperations] (put, get, delete, list) with [CredentialsOperations]
 * (upload tokens, presigned URLs) into a single unified client.
 *
 * All operations block the calling thread until the underlying provider responds.
 * For non-blocking, Kotlin-coroutine-friendly usage see [SuspendStorageClient]
 * and the [asSuspend] extension.
 *
 * Supporting providers include Alibaba Cloud OSS and AWS S3.
 */
interface StorageClient : ObjectOperations, CredentialsOperations, AutoCloseable
