<h1>
  <img src="docs/logos/grid-horizontal.svg" alt="CoStore Logo" width="280" height="80" align="middle">
</h1>

A Kotlin multi-module library providing unified object storage abstractions for S3 and OSS-compatible backends. JVM 17.

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://github.com/Ahoo-Wang/CoStore/blob/main/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/me.ahoo.costore/costore-core)](https://central.sonatype.com/artifact/me.ahoo.costore/costore-core)
[![codecov](https://codecov.io/gh/Ahoo-Wang/CoStore/branch/main/graph/badge.svg?token=uloJrLoQir)](https://codecov.io/gh/Ahoo-Wang/CoStore)

---

## Features

- **Unified API** for S3 and Aliyun OSS providers
- **Four programming models** — Sync, Async (`CompletableFuture`), Reactive (`Mono`), Coroutines
- **Provider pattern** for flexible credential management
- **Type-safe request/response** model pairs
- **Spring Boot auto-configuration** for quick integration

---

## Modules

| Module | Description |
|--------|-------------|
| `:core` | Interfaces, models, provider abstractions, error types |
| `:s3` | AWS S3 implementation (AWS SDK v2) |
| `:oss` | Aliyun OSS implementation (Aliyun OSS SDK) |
| `:bom` | Bill of Materials for dependency management |
| `:spring-boot-starter` | Spring Boot auto-configuration |

---

## Quick Start

### Gradle

```kotlin
dependencies {
    implementation("me.ahoo.costore:costore-s3")
    implementation("me.ahoo.costore:costore-oss")
}
```

### Create a Store

**S3:**

```kotlin
val credentials = S3Credentials(
    accessKeyId = "your-access-key-id",
    secretAccessKey = "your-secret-access-key",
    region = "us-east-1"
)

val provider = S3ObjectStoreProvider()
val store: ObjectStore = provider.sync(credentials)
```

**OSS:**

```kotlin
val credentials = OssCredentials(
    endpoint = "https://oss-cn-hangzhou.aliyuncs.com",
    accessKeyId = "your-access-key-id",
    secretAccessKey = "your-secret-access-key"
)

val provider = OssObjectStoreProvider()
val store: ObjectStore = provider.sync(credentials)
```

---

## Programming Models

### Sync (Blocking)

```kotlin
val response = store.getObject(GetObjectRequest(bucket, key))
response.use { obj ->  // StoredObject implements Closeable - MUST close!
    obj.content.readBytes()
}

store.putObject(PutObjectRequest(
    bucket = bucket,
    key = key,
    content = contentStream,
    contentLength = contentSize,  // Required for presigned URLs
    contentType = "application/json"
))
store.deleteObject(DeleteObjectRequest(bucket, key))
```

### Async (`CompletableFuture`)

```kotlin
val asyncStore: AsyncObjectStore = provider.async(credentials)
val future: CompletableFuture<GetObjectResponse> = asyncStore.getObject(GetObjectRequest(bucket, key))
```

### Reactive (`Mono`)

```kotlin
val reactiveStore: ReactiveObjectStore = provider.reactive(credentials)
val mono: Mono<GetObjectResponse> = reactiveStore.getObject(GetObjectRequest(bucket, key))
```

### Coroutines (Suspend Functions)

```kotlin
val coroutinesStore: CoroutinesObjectStore = provider.coroutines(credentials)
val response = coroutinesStore.getObject(GetObjectRequest(bucket, key))
```

### Adapters

Convert between models on an existing store:

```kotlin
val syncStore: ObjectStore = s3Provider.sync(credentials)

val asyncStore: AsyncObjectStore = syncStore.asAsync()
val reactiveStore: ReactiveObjectStore = syncStore.asReactive()
val coroutinesStore: CoroutinesObjectStore = syncStore.asCoroutines()
```

---

## Operations

| Operation | Method | Description |
|-----------|--------|-------------|
| **Get** | `getObject(request)` | Download an object (content + metadata) |
| **Put** | `putObject(request)` | Upload an object |
| **Delete** | `deleteObject(request)` | Delete an object |
| **List** | `listObjects(request)` | List objects with prefix/delimiter |
| **Head** | `headObject(request)` | Get metadata without content |
| **Presign Get** | `presignGetObject(request)` | Generate a pre-signed GET URL |
| **Presign Put** | `presignPutObject(request)` | Generate a pre-signed PUT URL |
| **Presign Delete** | `presignDeleteObject(request)` | Generate a pre-signed DELETE URL (S3 only) |

---

## Request / Response Models

```kotlin
// Get
val getResponse: GetObjectResponse = store.getObject(GetObjectRequest(bucket, key))
getResponse.use { obj ->  // StoredObject implements Closeable - MUST close!
    obj.content.readBytes()
}
// getResponse.metadata: StoredObjectMetadata (bucket, key, contentLength, contentType, eTag, lastModified, metadata)

// Put
val putResponse = store.putObject(PutObjectRequest(
    bucket = bucket,
    key = key,
    content = inputStream,
    contentLength = contentSize,  // Required - needed for presigned URLs and streaming
    contentType = "application/json",
    metadata = mapOf("x-custom" to "value")
))
// putResponse.eTag, putResponse.versionId

// Delete
val deleteResponse = store.deleteObject(DeleteObjectRequest(bucket, key))
// deleteResponse.deleteMarker, deleteResponse.versionId

// List
val listResponse = store.listObjects(ListObjectsRequest(bucket, prefix = "prefix/"))
// listResponse.objects: List<StoredObjectMetadata>
// listResponse.commonPrefixes: List<String>
// listResponse.isTruncated, listResponse.nextMarker

// Head
val headResponse = store.headObject(HeadObjectRequest(bucket, key))
// headResponse.contentLength, headResponse.contentType, headResponse.lastModified, etc.

// Presign
val presignResponse = store.presignGetObject(PresignGetObjectRequest(bucket, key, Duration.ofMinutes(15)))
// presignResponse.url, presignResponse.expiration, presignResponse.headers
```

### Validation

Request models validate inputs in their `init` blocks:
- `bucket` - must not be blank or contain `\n`, `\r`, `\t`
- `key` - must not be blank or contain `\n`, `\r`, `\t`
- `maxKeys` (ListObjectsRequest) - must be 1-1000

---

## Pre-signed URLs

Pre-signed URLs grant temporary access to private objects without credentials.

```kotlin
// S3 presigned PUT (includes Content-Type header)
val presignedPut = store.presignPutObject(PresignPutObjectRequest(
    bucket = bucket,
    key = key,
    expiration = Duration.ofMinutes(15),
    contentType = "application/json",
    metadata = mapOf("x-custom" to "value")
))
// presignedPut.url, presignedPut.headers["Content-Type"]

// S3 presigned DELETE
val presignedDelete = store.presignDeleteObject(PresignDeleteObjectRequest(bucket, key, Duration.ofMinutes(15)))

// Note: OSS does not support presigned DELETE URLs
```

---

## Error Handling

```kotlin
try {
    store.getObject(GetObjectRequest(bucket, "nonexistent-key"))
} catch (e: ObjectNotFoundError) {
    println("Bucket: ${e.bucket}, Key: ${e.key}")
} catch (e: CoStoreError) {
    println("Error: ${e.message}")
}
```

---

## Spring Boot Integration

### Gradle

```kotlin
dependencies {
    implementation("me.ahoo.costore:spring-boot-starter")
}
```

### Configuration

**S3:**

```yaml
costore:
  s3:
    access-key-id: ${AWS_ACCESS_KEY_ID}
    secret-access-key: ${AWS_SECRET_ACCESS_KEY}
    region: us-east-1
    endpoint: https://...  # optional, for S3-compatible stores
```

**OSS:**

```yaml
costore:
  oss:
    endpoint: https://oss-cn-hangzhou.aliyuncs.com
    access-key-id: ${OSS_ACCESS_KEY_ID}
    secret-access-key: ${OSS_SECRET_ACCESS_KEY}
```

### Usage

```kotlin
@Configuration
class StorageConfig {
    @Bean
    fun objectStore(store: ObjectStore): ObjectStore = store
}
```

Auto-configuration is activated automatically when the respective Spring Boot starter is on the classpath and the required properties are set (`costore.s3.region` for S3, `costore.oss.endpoint` for OSS).

---

## Build

```bash
./gradlew assemble          # Build without tests
./gradlew build            # Full build with tests
./gradlew test             # Run all tests
./gradlew :core:test       # Test specific module (:core, :s3, :oss)
./gradlew detekt           # Lint
./gradlew :core:koverHtmlReportJvm  # Coverage report for core
./gradlew :code-coverage-report:codeCoverageReport  # Aggregated coverage
./gradlew publishToMavenLocal
```

---

## License

Apache License 2.0
