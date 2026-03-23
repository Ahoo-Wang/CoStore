---
name: fluent-assert
description: Fluent assertions for Kotlin unit tests. Use when writing or modifying test files. Provides AssertJ wrapper with Kotlin extensions for primitives, collections, exceptions, and time types.
---

# FluentAssert Skill

## Import

```kotlin
import me.ahoo.test.asserts.assert
```

## Pattern

```kotlin
value.assert().assertionMethod()
value.assert().assertionMethod(expected)
value.assert().assertionMethod().assertionMethod()
```

## Usage

### Primitives & Strings

```kotlin
42.assert().isEqualTo(42).isBetween(0, 100)
"hello".assert().startsWith("hel").endsWith("llo").contains("ell")
```

### Collections

```kotlin
listOf(1, 2, 3).assert().hasSize(3).contains(2).doesNotContain(4)
mapOf("key" to "value").assert().containsKey("key").containsValue("value")
arrayOf("a", "b").assert().hasSize(2).contains("a")
optionalOf("x").assert().isPresent().contains("x")
```

### Exception Testing

```kotlin
assertThrownBy<IllegalArgumentException> {
    throw IllegalArgumentException("invalid")
}.assert().hasMessage("invalid")
```

### Time

```kotlin
Instant.now().assert().isBefore(Instant.now().plusSeconds(1))
LocalDate.now().assert().isToday()
```

## Common Assertions

| Type | Assertions |
|------|-----------|
| `Boolean?` | `isTrue()`, `isFalse()`, `isNull()` |
| `Int?` / `Long?` | `isEqualTo()`, `isGreaterThan()`, `isLessThan()`, `isBetween()` |
| `String?` | `startsWith()`, `endsWith()`, `contains()`, `hasLength()`, `matches()` |
| `Collection<T>?` | `hasSize()`, `contains()`, `doesNotContain()`, `allMatch()` |
| `List<T>?` | `hasSize()`, `contains()`, `element(index)` |
| `Map<K,V>?` | `hasSize()`, `containsKey()`, `containsValue()`, `containsEntry()` |
| `Optional<T>?` | `isPresent()`, `isEmpty()`, `contains()` |
| `Path?` / `File?` | `exists()`, `isReadable()`, `isRegularFile()` |
| `Throwable?` | `hasMessage()`, `isInstanceOf()` |

## Gotchas

- `assertThrownBy` requires reified type: `assertThrownBy<IllegalArgumentException>`
- Chaining: each `.assert()` returns assert object for further chaining
- Nullable types: `null.assert().isNull()` works safely

## Detailed API

For complete API reference including all assertion methods, see [references/API.md](references/API.md).

## Resources

- GitHub: https://github.com/Ahoo-Wang/FluentAssert
- Version: 0.2.2
