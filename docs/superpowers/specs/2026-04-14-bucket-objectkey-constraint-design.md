# BucketNameConstraint & ObjectKeyConstraint Design

## Overview

Replace manual validation in `ObjectStoreValidation` with Jakarta Bean Validation annotations via custom constraint annotations `@BucketNameConstraint` and `@ObjectKeyConstraint`.

## Goals

- Declarative validation using standard Jakarta Bean Validation
- AWS S3-compliant bucket name validation
- Control character validation for object keys
- Remove manual `require()`-based validation in `init` blocks

## New Annotations

### BucketNameConstraint

```kotlin
@Target(PROPERTY_GETTER, FIELD)
@Retention(RUNTIME)
@NotBlank
@Size(min = 3, max = 63)
@Pattern(regexp = "^[a-z0-9][a-z0-9.-]{2,62}$", message = "Bucket name must conform to S3 naming rules")
annotation class BucketNameConstraint
```

Validates:
- Not blank
- Length 3-63 characters
- Starts with lowercase letter or digit
- Contains only lowercase letters, digits, `.`, `-`

### ObjectKeyConstraint

```kotlin
@Target(PROPERTY_GETTER, FIELD)
@Retention(RUNTIME)
@NotBlank
@Pattern(regexp = "^[^\\n\\r\\t]+$", message = "Object key must not contain control characters")
annotation class ObjectKeyConstraint
```

Validates:
- Not blank
- No control characters (`\n`, `\r`, `\t`)

## Files to Create

| File | Purpose |
|------|---------|
| `core/src/main/kotlin/me/ahoo/costore/core/model/BucketNameConstraint.kt` | Bucket name validation annotation |
| `core/src/main/kotlin/me/ahoo/costore/core/model/ObjectKeyConstraint.kt` | Object key validation annotation |

## Files to Modify

| File | Change |
|------|--------|
| `core/src/main/kotlin/me/ahoo/costore/core/model/BucketCapable.kt` | Replace `@get:NotBlank` with `@get:BucketNameConstraint` |
| `core/src/main/kotlin/me/ahoo/costore/core/model/ObjectKeyCapable.kt` | Replace `@get:NotBlank` with `@get:ObjectKeyConstraint` |

## Files to Delete

| File | Reason |
|------|--------|
| `core/src/main/kotlin/me/ahoo/costore/core/model/Validation.kt` | Replaced by annotation-based validation |
| `core/src/test/kotlin/me/ahoo/costore/core/model/ObjectStoreValidationTest.kt` | Tests for removed `ObjectStoreValidation` class |

## Affected Classes

All classes implementing `BucketCapable` or `ObjectKeyCapable` automatically get validation via annotations on the interface:

- `GetObjectRequest`, `PutObjectRequest`, `DeleteObjectRequest`, `HeadObjectRequest`
- `PresignRequest.Get`, `PresignRequest.Put`, `PresignRequest.Delete`
- `StoredObjectMetadata`

### Init Block Cleanup

Remove `init` blocks containing `ObjectStoreValidation.validateBucketName()` / `ObjectStoreValidation.validateObjectKey()` calls from affected classes, as annotation-based validation triggers during object construction.

## Validation Messages

- Bucket: `"Bucket name must conform to S3 naming rules"`
- ObjectKey: `"Object key must not contain control characters"`

## S3 Naming References

- [AWS S3 Bucket Naming Rules](https://docs.aws.amazon.com/AmazonS3/latest/userguide/bucketnamingrules.html)

## Test Considerations

Add unit tests for the new constraint annotations using a validation framework (e.g., Hibernate Validator test utilities).
