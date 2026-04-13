# CoStore

A Kotlin multi-module library providing unified object storage abstractions for S3 and OSS-compatible backends.

**Version:** 0.0.1 | **Target:** JVM 17

---

## Features

- **Unified API** for object storage operations across S3 and OSS providers
- **Four programming models** — Sync, Async (`CompletableFuture`), Reactive (`Mono`), and Coroutines
- **Provider pattern** for flexible credential management
- **Request/Response pairs** for type-safe operations
- **Spring Boot starter** for quick integration

---

## Module Structure

| Module | Description |
|--------|-------------|
| `:core` | Interfaces, models, provider abstractions, error types |
| `:s3` | AWS S3 implementation using AWS SDK v2 |
| `:oss` | Aliyun OSS implementation using Aliyun OSS SDK |
| `:bom` | Bill of Materials for dependency management |
| `:spring-boot-starter` | Spring Boot auto-configuration |

---

## Programming Models

CoStore provides four parallel interface hierarchies. Choose the one that fits your application's architecture:

### Sync (Blocking)

```kotlin
val store: ObjectStore = s3Provider.sync(credentials)

val response = store.getObject(GetObjectRequest(bucket, key))
response.content.use { inputStream ->
    // read bytes
}

store.putObject(PutObjectRequest(bucket, key, contentStream, contentType))
store.deleteObject(DeleteObjectRequest(bucket, key))
```

### Async (`CompletableFuture`)

```kotlin
val store: AsyncObjectStore = s3Provider.async(credentials)

val future: CompletableFuture<GetObjectResponse> = store.getObject(GetObjectRequest(bucket, key))
```

### Reactive (`Mono`)

```kotlin
val store: ReactiveObjectStore = s3Provider.reactive(credentials)

val mono: Mono<GetObjectResponse> = store.getObject(GetObjectRequest(bucket, key))
```

### Coroutines (Suspend Functions)

```kotlin
val store: CoroutinesObjectStore = s3Provider.coroutines(credentials)

val response = store.getObject(GetObjectRequest(bucket, key))
```

### Adapters

Convert between models using extension functions:

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
| **Get** | `getObject(request)` | Download an object |
| **Put** | `putObject(request)` | Upload an object |
| **Delete** | `deleteObject(request)` | Delete an object |
| **List** | `listObjects(request)` | List objects with prefix/delimiter |
| **Head** | `headObject(request)` | Get metadata without content |
| **Presign Get** | `presignGetObject(request)` | Generate a pre-signed GET URL |
| **Presign Put** | `presignPutObject(request)` | Generate a pre-signed PUT URL |
| **Presign Delete** | `presignDeleteObject(request)` | Generate a pre-signed DELETE URL |

---

## Providers

### S3 Provider

```kotlin
val credentials = S3Credentials(
    accessKeyId = "your-access-key-id",
    secretAccessKey = "your-secret-access-key",
    region = "us-east-1"         // optional
    endpoint = "https://..."     // optional, for S3-compatible stores
)

val provider = S3ObjectStoreProvider()
val store = provider.sync(credentials)
```

### OSS Provider

```kotlin
val credentials = OssCredentials(
    endpoint = "https://oss-cn-hangzhou.aliyuncs.com",  // required
    accessKeyId = "your-access-key-id",
    secretAccessKey = "your-secret-access-key"
)

val provider = OssObjectStoreProvider()
val store = provider.sync(credentials)
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

---

## Request / Response Models

```kotlin
// Get
val getRequest = GetObjectRequest(bucket, key)
val getResponse: GetObjectResponse = store.getObject(getRequest)
// getResponse.content: InputStream
// getResponse.metadata: StoredObjectMetadata

// Put
val putRequest = PutObjectRequest(
    bucket = bucket,
    key = key,
    content = inputStream,
    contentType = "application/json"
)
val putResponse = store.putObject(putRequest)
// putResponse.eTag, putResponse.versionId

// Delete
val deleteRequest = DeleteObjectRequest(bucket, key)
val deleteResponse = store.deleteObject(deleteRequest)
// deleteResponse.deleteMarker, deleteResponse.versionId

// List
val listRequest = ListObjectsRequest(bucket, prefix = "prefix/")
val listResponse = store.listObjects(listRequest)
// listResponse.objects: List<StoredObject>
// listResponse.commonPrefixes: List<String>

// Head
val headRequest = HeadObjectRequest(bucket, key)
val headResponse = store.headObject(headRequest)
// headResponse.contentLength, headResponse.contentType, headResponse.lastModified
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

## Build

```bash
./gradlew assemble          # Build without tests
./gradlew build            # Full build with tests
./gradlew test             # Run all tests
./gradlew :core:test       # Test specific module
./gradlew detekt           # Lint
./gradlew koverReport      # Code coverage
```

---

## License

Apache License 2.0
