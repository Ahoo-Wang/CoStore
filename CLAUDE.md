# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

CoStore is a Kotlin multi-module library providing object storage abstractions for S3 and OSS-compatible backends. Version 0.0.1, JVM 17.

## Build Commands

```bash
./gradlew assemble          # Build without tests
./gradlew build            # Full build with tests
./gradlew test             # Run all tests
./gradlew :core:test        # Test specific module (:s3, :oss, :core)
./gradlew :core:test --tests "me.ahoo.costore.core.SomeTest"   # Single test class
./gradlew :core:test --tests "me.ahoo.costore.core.SomeTest.method"  # Single test method
./gradlew detekt           # Lint
./gradlew detektAll         # Lint with auto-correct
./gradlew koverReport       # Code coverage
./gradlew publishToMavenLocal
```

## Architecture

### Interface Hierarchy (per adapter)

The core contract is `ObjectStore` (sync). Each operation style is a separate interface hierarchy:

- **Sync**: `ObjectStore` - blocking operations
- **Async**: `AsyncObjectStore` - `CompletableFuture`-based operations
- **Reactive**: `ReactiveObjectStore` - Reactor `Mono`-based operations
- **Coroutines**: `CoroutinesObjectStore` - suspend function-based operations

Each hierarchy mirrors the same operations (get, put, delete, list, head, presign*). Implementations expose variants via `asAsync()`, `asReactive()`, and `asCoroutines()` extension functions on the sync base.

### Request/Response Model Pairs

Operations use typed request/response pairs:
- `GetObjectRequest` → `GetObjectResponse`
- `PutObjectRequest` → `PutObjectResponse`
- `DeleteObjectRequest` → `DeleteObjectResponse`
- etc.

All requests implement `ObjectRequest`, responses implement `ObjectResponse`.

### Provider Pattern for Credentials

`ObjectStoreProvider<CREDENTIALS>` creates store instances from credentials, exposing all four variants:
```kotlin
fun sync(credentials: CREDENTIALS): ObjectStore
fun async(credentials: CREDENTIALS): AsyncObjectStore
fun reactive(credentials: CREDENTIALS): ReactiveObjectStore
fun coroutines(credentials: CREDENTIALS): CoroutinesObjectStore
```

### Error Model

Errors extend `CoStoreError(message, cause)` which extends `RuntimeException`. Errors implement capability interfaces like `BucketCapable` and `ObjectKeyCapable` to carry context.

## Module Structure

```
:core       - Interfaces, models, provider abstractions
:s3         - AWS S3 implementation (scaffolding - TODO sections)
:oss        - Aliyun OSS implementation (scaffolding - TODO sections)
:bom        - Bill of materials
:code-coverage-report - Aggregated coverage
```

## Detailed Documentation

See `AGENTS.md` for detailed information on:
- Code style and formatting rules
- Naming conventions
- Testing patterns (FluentAssert, MockK)
- Linting configuration (detekt)
- Git conventions
