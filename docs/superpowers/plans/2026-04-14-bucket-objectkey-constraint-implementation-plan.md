# BucketNameConstraint & ObjectKeyConstraint Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace manual `ObjectStoreValidation` validation with declarative Jakarta Bean Validation annotations via custom constraint annotations `@BucketNameConstraint` and `@ObjectKeyConstraint`.

**Architecture:** Create two custom composing constraint annotations (`BucketNameConstraint`, `ObjectKeyConstraint`) that use standard Jakarta Bean Validation annotations. Apply these to `BucketCapable.bucket` and `ObjectKeyCapable.key` properties. Remove manual `init` block validation from all implementing classes.

**Tech Stack:** Kotlin, Jakarta Bean Validation (`jakarta.validation.constraints.*`), existing `jakarta.validation.api` dependency already present as `compileOnly` in `core/build.gradle.kts`.

---

## File Structure

**Create:**
- `core/src/main/kotlin/me/ahoo/costore/core/model/BucketNameConstraint.kt` — bucket name constraint annotation
- `core/src/main/kotlin/me/ahoo/costore/core/model/ObjectKeyConstraint.kt` — object key constraint annotation
- `core/src/test/kotlin/me/ahoo/costore/core/model/BucketNameConstraintTest.kt` — bucket constraint tests
- `core/src/test/kotlin/me/ahoo/costore/core/model/ObjectKeyConstraintTest.kt` — object key constraint tests

**Modify:**
- `core/src/main/kotlin/me/ahoo/costore/core/model/BucketCapable.kt:15-16` — replace `@get:NotBlank` with `@get:BucketNameConstraint`
- `core/src/main/kotlin/me/ahoo/costore/core/model/ObjectKeyCapable.kt:14-15` — replace `@get:NotBlank` with `@get:ObjectKeyConstraint`
- `core/src/main/kotlin/me/ahoo/costore/core/model/GetObject.kt:9-12` — remove `init` block
- `core/src/main/kotlin/me/ahoo/costore/core/model/PutObject.kt:18-21` — remove `init` block
- `core/src/main/kotlin/me/ahoo/costore/core/model/DeleteObject.kt:8-11` — remove `init` block
- `core/src/main/kotlin/me/ahoo/costore/core/model/HeadObject.kt:7-10` — remove `init` block
- `core/src/main/kotlin/me/ahoo/costore/core/model/PresignRequest.kt:56-59,70-73,83-86` — remove `init` blocks
- `core/src/main/kotlin/me/ahoo/costore/core/model/ListObjects.kt:10-14` — remove `init` block (contains validateBucketName)

**Delete:**
- `core/src/main/kotlin/me/ahoo/costore/core/model/Validation.kt` — replaced by annotation-based validation
- `core/src/test/kotlin/me/ahoo/costore/core/model/ObjectStoreValidationTest.kt` — tests for removed class

---

## Task 1: Create BucketNameConstraint Annotation

**Files:**
- Create: `core/src/main/kotlin/me/ahoo/costore/core/model/BucketNameConstraint.kt`

- [ ] **Step 1: Write BucketNameConstraint.kt**

```kotlin
package me.ahoo.costore.core.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlin.annotation.AnnotationTarget.*

/**
 * Validates that a bucket name conforms to AWS S3 bucket naming rules.
 *
 * @see <a href="https://docs.aws.amazon.com/AmazonS3/latest/userguide/bucketnamingrules.html">S3 Bucket Naming Rules</a>
 */
@Target(PROPERTY_GETTER, FIELD)
@Retention(RUNTIME)
@NotBlank
@Size(min = 3, max = 63)
@Pattern(regexp = "^[a-z0-9][a-z0-9.-]{2,62}$", message = "Bucket name must conform to S3 naming rules")
annotation class BucketNameConstraint
```

- [ ] **Step 2: Verify file compiles**

Run: `./gradlew :core:compileKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add core/src/main/kotlin/me/ahoo/costore/core/model/BucketNameConstraint.kt
git commit -m "feat(core): add BucketNameConstraint annotation for S3-compliant bucket name validation"
```

---

## Task 2: Create ObjectKeyConstraint Annotation

**Files:**
- Create: `core/src/main/kotlin/me/ahoo/costore/core/model/ObjectKeyConstraint.kt`

- [ ] **Step 1: Write ObjectKeyConstraint.kt**

```kotlin
package me.ahoo.costore.core.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import kotlin.annotation.AnnotationTarget.*

/**
 * Validates that an object key does not contain control characters.
 *
 * S3 object keys can contain any UTF-8 characters except control characters.
 */
@Target(PROPERTY_GETTER, FIELD)
@Retention(RUNTIME)
@NotBlank
@Pattern(regexp = "^[^\\n\\r\\t]+$", message = "Object key must not contain control characters")
annotation class ObjectKeyConstraint
```

- [ ] **Step 2: Verify file compiles**

Run: `./gradlew :core:compileKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add core/src/main/kotlin/me/ahoo/costore/core/model/ObjectKeyConstraint.kt
git commit -m "feat(core): add ObjectKeyConstraint annotation for control character validation"
```

---

## Task 3: Update BucketCapable Interface

**Files:**
- Modify: `core/src/main/kotlin/me/ahoo/costore/core/model/BucketCapable.kt`

- [ ] **Step 1: Replace @get:NotBlank with @get:BucketNameConstraint**

```kotlin
interface BucketCapable {
    @get:BucketNameConstraint
    val bucket: BucketName
}
```

- [ ] **Step 2: Remove import for NotBlank (if no longer used)**

Check if `jakarta.validation.constraints.NotBlank` is still needed in the file.

- [ ] **Step 3: Verify file compiles**

Run: `./gradlew :core:compileKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add core/src/main/kotlin/me/ahoo/costore/core/model/BucketCapable.kt
git commit -m "refactor(core): use @BucketNameConstraint instead of @NotBlank on BucketCapable.bucket"
```

---

## Task 4: Update ObjectKeyCapable Interface

**Files:**
- Modify: `core/src/main/kotlin/me/ahoo/costore/core/model/ObjectKeyCapable.kt`

- [ ] **Step 1: Replace @get:NotBlank with @get:ObjectKeyConstraint**

```kotlin
interface ObjectKeyCapable {
    @get:ObjectKeyConstraint
    val key: ObjectKey
}
```

- [ ] **Step 2: Remove import for NotBlank (if no longer used)**

Check if `jakarta.validation.constraints.NotBlank` is still needed in the file.

- [ ] **Step 3: Verify file compiles**

Run: `./gradlew :core:compileKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add core/src/main/kotlin/me/ahoo/costore/core/model/ObjectKeyCapable.kt
git commit -m "refactor(core): use @ObjectKeyConstraint instead of @NotBlank on ObjectKeyCapable.key"
```

---

## Task 5: Remove Init Blocks from Request Classes

**Files:**
- Modify: `core/src/main/kotlin/me/ahoo/costore/core/model/GetObject.kt:9-12`
- Modify: `core/src/main/kotlin/me/ahoo/costore/core/model/PutObject.kt:18-21`
- Modify: `core/src/main/kotlin/me/ahoo/costore/core/model/DeleteObject.kt:8-11`
- Modify: `core/src/main/kotlin/me/ahoo/costore/core/model/HeadObject.kt:7-10`
- Modify: `core/src/main/kotlin/me/ahoo/costore/core/model/ListObjects.kt:10-14`

- [ ] **Step 1: Remove init block from GetObjectRequest**

Change:
```kotlin
) : BucketCapable, ObjectKeyCapable, NullableContentTypeCapable, NullableVersionIdCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}
```
To:
```kotlin
) : BucketCapable, ObjectKeyCapable, NullableContentTypeCapable, NullableVersionIdCapable
```

- [ ] **Step 2: Remove init block from PutObjectRequest**

Change:
```kotlin
) : BucketCapable, ContentLengthCapable, NullableContentTypeCapable, ObjectKeyCapable, ContentCapable, UserMetadataCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}
```
To:
```kotlin
) : BucketCapable, ContentLengthCapable, NullableContentTypeCapable, ObjectKeyCapable, ContentCapable, UserMetadataCapable
```

- [ ] **Step 3: Remove init block from DeleteObjectRequest**

Change:
```kotlin
) : BucketCapable, ObjectKeyCapable, NullableVersionIdCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}
```
To:
```kotlin
) : BucketCapable, ObjectKeyCapable, NullableVersionIdCapable
```

- [ ] **Step 4: Remove init block from HeadObjectRequest**

Change:
```kotlin
) : BucketCapable, ObjectKeyCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}
```
To:
```kotlin
) : BucketCapable, ObjectKeyCapable
```

- [ ] **Step 5: Remove ObjectStoreValidation calls from ListObjectsRequest init block**

Change:
```kotlin
) : BucketCapable {
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        require(maxKeys > 0) { "maxKeys must be positive, but was $maxKeys" }
        require(maxKeys <= 1000) { "maxKeys must not exceed 1000, but was $maxKeys" }
    }
}
```
To:
```kotlin
) : BucketCapable {
    init {
        require(maxKeys > 0) { "maxKeys must be positive, but was $maxKeys" }
        require(maxKeys <= 1000) { "maxKeys must not exceed 1000, but was $maxKeys" }
    }
}
```
Note: The `maxKeys` validation (`require(maxKeys > 0)` and `require(maxKeys <= 1000)`) remains because it is not covered by `@BucketNameConstraint` or `@ObjectKeyConstraint`.

- [ ] **Step 6: Verify all files compile**

Run: `./gradlew :core:compileKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add core/src/main/kotlin/me/ahoo/costore/core/model/GetObject.kt
git add core/src/main/kotlin/me/ahoo/costore/core/model/PutObject.kt
git add core/src/main/kotlin/me/ahoo/costore/core/model/DeleteObject.kt
git add core/src/main/kotlin/me/ahoo/costore/core/model/HeadObject.kt
git add core/src/main/kotlin/me/ahoo/costore/core/model/ListObjects.kt
git commit -m "refactor(core): remove ObjectStoreValidation init blocks from request classes"
```

---

## Task 6: Remove Init Blocks from PresignRequest Classes

**Files:**
- Modify: `core/src/main/kotlin/me/ahoo/costore/core/model/PresignRequest.kt:50-60,62-74,76-87`

- [ ] **Step 1: Remove init block from PresignRequest.Get**

Change:
```kotlin
data class Get(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration,
) : PresignRequest {
    override val method: PresignMethod = PresignMethods.GET
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}
```
To:
```kotlin
data class Get(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration,
) : PresignRequest {
    override val method: PresignMethod = PresignMethods.GET
}
```

- [ ] **Step 2: Remove init block from PresignRequest.Put**

Change:
```kotlin
data class Put(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration,
    override val contentType: String? = null,
    override val metadata: Map<String, String> = emptyMap(),
) : PresignRequest, NullableContentTypeCapable, UserMetadataCapable {
    override val method: PresignMethod = PresignMethods.PUT
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}
```
To:
```kotlin
data class Put(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration,
    override val contentType: String? = null,
    override val metadata: Map<String, String> = emptyMap(),
) : PresignRequest, NullableContentTypeCapable, UserMetadataCapable {
    override val method: PresignMethod = PresignMethods.PUT
}
```

- [ ] **Step 3: Remove init block from PresignRequest.Delete**

Change:
```kotlin
data class Delete(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration,
    override val versionId: String? = null,
) : PresignRequest, NullableVersionIdCapable {
    override val method: PresignMethod = PresignMethods.DELETE
    init {
        ObjectStoreValidation.validateBucketName(bucket)
        ObjectStoreValidation.validateObjectKey(key)
    }
}
```
To:
```kotlin
data class Delete(
    override val bucket: BucketName,
    override val key: ObjectKey,
    override val expiration: Duration,
    override val versionId: String? = null,
) : PresignRequest, NullableVersionIdCapable {
    override val method: PresignMethod = PresignMethods.DELETE
}
```

- [ ] **Step 4: Verify file compiles**

Run: `./gradlew :core:compileKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add core/src/main/kotlin/me/ahoo/costore/core/model/PresignRequest.kt
git commit -m "refactor(core): remove ObjectStoreValidation init blocks from PresignRequest classes"
```

---

## Task 7: Delete Validation.kt and ObjectStoreValidationTest.kt

**Files:**
- Delete: `core/src/main/kotlin/me/ahoo/costore/core/model/Validation.kt`
- Delete: `core/src/test/kotlin/me/ahoo/costore/core/model/ObjectStoreValidationTest.kt`

- [ ] **Step 1: Delete Validation.kt**

Run: `git rm core/src/main/kotlin/me/ahoo/costore/core/model/Validation.kt`

- [ ] **Step 2: Delete ObjectStoreValidationTest.kt**

Run: `git rm core/src/test/kotlin/me/ahoo/costore/core/model/ObjectStoreValidationTest.kt`

- [ ] **Step 3: Verify project compiles**

Run: `./gradlew :core:compileKotlin :core:compileTestKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Verify tests still pass**

Run: `./gradlew :core:test`
Expected: BUILD SUCCESSFUL (all tests pass, excluding deleted test)

- [ ] **Step 5: Commit**

```bash
git rm core/src/main/kotlin/me/ahoo/costore/core/model/Validation.kt
git rm core/src/test/kotlin/me/ahoo/costore/core/model/ObjectStoreValidationTest.kt
git commit -m "refactor(core): remove ObjectStoreValidation - replaced by annotation-based validation"
```

---

## Task 8: Add Constraint Annotation Tests

**Files:**
- Create: `core/src/test/kotlin/me/ahoo/costore/core/model/BucketNameConstraintTest.kt`
- Create: `core/src/test/kotlin/me/ahoo/costore/core/model/ObjectKeyConstraintTest.kt`

**Note:** The existing test `ObjectStoreValidationTest.kt` is being deleted per the spec. The new tests cover validation behavior at the annotation level using Hibernate Validator's `validator` API.

- [ ] **Step 1: Write BucketNameConstraintTest.kt**

```kotlin
package me.ahoo.costore.core.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import me.ahoo.test.asserts.assert

class BucketNameConstraintTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `valid bucket names should pass validation`() {
        val validBuckets = listOf(
            "valid-bucket",
            "valid.bucket",
            "valid_bucket",
            "bucket123",
            "a1b",
            "abc-def.ghi-jkl.mno"
        )
        for (bucket in validBuckets) {
            val violations = validator.validate(BucketTest(bucket))
            assert(violations).isEmpty("Bucket '$bucket' should be valid but had violations: ${violations.map { it.message }}")
        }
    }

    @Test
    fun `blank bucket should fail validation`() {
        val violations = validator.validate(BucketTest(""))
        assert(violations.size).isGreaterThan(0)
        assert(violations.first().message).contains("blank")
    }

    @ParameterizedTest
    @ValueSource(strings = ["bucket\nname", "bucket\rname", "bucket\tname"])
    fun `bucket with control characters should fail validation`(bucket: String) {
        val violations = validator.validate(BucketTest(bucket))
        assert(violations.size).isGreaterThan(0)
    }

    @Test
    fun `bucket shorter than 3 characters should fail validation`() {
        val violations = validator.validate(BucketTest("ab"))
        assert(violations.size).isGreaterThan(0)
    }

    @Test
    fun `bucket longer than 63 characters should fail validation`() {
        val violations = validator.validate(BucketTest("a".repeat(64)))
        assert(violations.size).isGreaterThan(0)
    }

    data class BucketTest(@get:BucketNameConstraint val bucket: String)
}
```

- [ ] **Step 2: Write ObjectKeyConstraintTest.kt**

```kotlin
package me.ahoo.costore.core.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import me.ahoo.test.asserts.assert

class ObjectKeyConstraintTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `valid object keys should pass validation`() {
        val validKeys = listOf(
            "valid/key",
            "valid.key",
            "valid_key",
            "key/with/multiple/segments",
            "file.txt",
            "path/to/file.pdf"
        )
        for (key in validKeys) {
            val violations = validator.validate(KeyTest(key))
            assert(violations).isEmpty("Key '$key' should be valid but had violations: ${violations.map { it.message }}")
        }
    }

    @Test
    fun `blank key should fail validation`() {
        val violations = validator.validate(KeyTest(""))
        assert(violations.size).isGreaterThan(0)
        assert(violations.first().message).contains("blank")
    }

    @ParameterizedTest
    @ValueSource(strings = ["key\nname", "key\rname", "key\tname"])
    fun `key with control characters should fail validation`(key: String) {
        val violations = validator.validate(KeyTest(key))
        assert(violations.size).isGreaterThan(0)
        assert(violations.first().message).contains("control characters")
    }

    data class KeyTest(@get:ObjectKeyConstraint val key: String)
}
```

- [ ] **Step 3: Verify tests compile**

Run: `./gradlew :core:compileTestKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Run the new tests**

Run: `./gradlew :core:test --tests "me.ahoo.costore.core.model.BucketNameConstraintTest" --tests "me.ahoo.costore.core.model.ObjectKeyConstraintTest"`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add core/src/test/kotlin/me/ahoo/costore/core/model/BucketNameConstraintTest.kt
git add core/src/test/kotlin/me/ahoo/costore/core/model/ObjectKeyConstraintTest.kt
git commit -m "test(core): add BucketNameConstraint and ObjectKeyConstraint validation tests"
```

---

## Task 9: Final Verification

- [ ] **Step 1: Run full build**

Run: `./gradlew build`
Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Run detekt linting**

Run: `./gradlew detekt`
Expected: BUILD SUCCESSFUL (no detekt violations)

- [ ] **Step 3: Final commit if needed**

If any changes were made in final verification, commit them.

---

## Summary

| Task | Description | Files Created/Modified/Deleted |
|------|-------------|-------------------------------|
| 1 | Create BucketNameConstraint annotation | Created |
| 2 | Create ObjectKeyConstraint annotation | Created |
| 3 | Update BucketCapable interface | Modified |
| 4 | Update ObjectKeyCapable interface | Modified |
| 5 | Remove init blocks from GetObject, PutObject, DeleteObject, HeadObject, ListObjects | Modified |
| 6 | Remove init blocks from PresignRequest classes | Modified |
| 7 | Delete Validation.kt and ObjectStoreValidationTest.kt | Deleted |
| 8 | Add constraint annotation tests | Created |
| 9 | Final verification | — |
