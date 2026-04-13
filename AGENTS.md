# Agent Guidelines for CoStore

## Project Overview

CoStore is a Kotlin multi-module project providing object storage abstractions (S3, OSS compatible).
Uses Gradle with Kotlin DSL, JVM toolchain 17.

## Build Commands

```bash
# Full build (no tests)
./gradlew assemble

# Full build with tests
./gradlew build

# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :core:test
./gradlew :s3:test
./gradlew :oss:test

# Run a single test class
./gradlew :core:test --tests "me.ahoo.costore.core.SomeTest"

# Run a single test method
./gradlew :core:test --tests "me.ahoo.costore.core.SomeTest.someMethod"

# Run detekt (linting)
./gradlew detekt

# Run detekt with auto-correct
./gradlew detektAll

# Build with code coverage
./gradlew koverReport

# Generate aggregated JaCoCo coverage report
./gradlew :code-coverage-report:codeCoverageReport

# Publish to local Maven
./gradlew publishToMavenLocal

# Clean
./gradlew clean
```

## Code Style

### Formatting
- Kotlin "official" code style (`kotlin.code.style=official`)
- Max line length: 300 characters
- 4 spaces indentation (Kotlin default)
- No wildcard imports except: `java.util.*`, `org.assertj.core.api.Assertions.*`

### Naming Conventions
- Classes: PascalCase (e.g., `ObjectStore`, `CoStoreError`)
- Interfaces: PascalCase, often end with -able or similar suffixes (e.g., `BucketCapable`, `ObjectKeyCapable`)
- Type aliases: PascalCase (e.g., `BucketName = String`)
- Packages: lowercase with dots (e.g., `me.ahoo.costore.core.model`)
- Generic type parameters: single uppercase letter or descriptive (e.g., `CREDENTIALS`)

### Architecture Patterns
- Interfaces for operations grouped by concern (sync, reactive, async, coroutines)
- Composition over inheritance
- Error classes extend `CoStoreError` which extends `RuntimeException`
- Request/Response model pairs (e.g., `PutObjectRequest` / `PutObjectResponse`)
- Provider pattern for credential-based store creation
- `ObjectStore` is the base contract; adapters are exposed via `asAsync`, `asReactive`, and `asCoroutines` extension functions
- `s3` and `oss` implementations are currently scaffolding with `TODO("Not yet implemented")` sections in `S3ObjectStore` and `OssObjectStore`

### Imports Organization
```kotlin
import java.io.InputStream                    // standard library
import me.ahoo.costore.core.model.BucketCapable // project internal
```

### Key Type Aliases
```kotlin
typealias BucketName = String
typealias ObjectKey = String
```

### Module Structure
```
:core       - Core interfaces and models
:s3         - AWS S3 implementation
:oss        - Aliyun OSS implementation
:bom        - Bill of materials
:code-coverage-report - Aggregated coverage report
```

## Testing

- Framework: JUnit Jupiter (via `org.junit:junit-bom`)
- Assertions: FluentAssert (`me.ahoo.test:fluent-assert-core`, `me.ahoo.test.asserts.assert`)
- Mocking: MockK
- Test retry plugin: 2 retries in CI, fail on pass after retry
- Use `useJUnitPlatform()` for all tests
- Test logging: `TestExceptionFormat.FULL`
- Current tests are in `:core`; `:s3` and `:oss` currently do not contain test sources

### Example Test Structure
```kotlin
import me.ahoo.test.asserts.assert

class MyTest {
    @Test
    fun `should do something`() {
        // given
        val input = "test"
        // when
        val result = doSomething(input)
        // then
        result.assert().isEqualTo("expected")
    }

    @Test
    fun `should handle list`() {
        listOf(1, 2, 3).assert()
            .hasSize(3)
            .contains(2)
            .doesNotContain(4)
    }
}
```

## Linting (detekt)

- Config: `config/detekt/detekt.yml`
- Auto-correct enabled
- Formatting rules via `detekt-formatting` plugin
- Disabled rules: LongParameterList, NestedBlockDepth, TooManyFunctions, ReturnCount, etc.

## Compiler Options

```kotlin
-Xjsr305=strict        // Strict nullability checks
-Xjvm-default=all-compatibility  // JVM default method compatibility
-javaParameters        // Java-style parameter names in reflection
```

## Error Handling

- Base error: `CoStoreError(message, cause)`
- Specific errors extend CoStoreError (e.g., `ObjectNotFoundError`)
- Errors implement relevant capability interfaces (e.g., `BucketCapable`, `ObjectKeyCapable`)

## Git Conventions

- Conventional commit messages recommended
- Sign commits when required for publishing
- No force pushes to main/dev branches

## Available Skills

- `.agents/skills/fluent-assert/` - FluentAssert assertions library for Kotlin tests
