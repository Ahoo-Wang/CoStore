# FluentAssert API Reference

## Extension Functions

| Type | Extension |
|------|-----------|
| `Boolean?` | `.assert(): BooleanAssert` |
| `Byte?` | `.assert(): ByteAssert` |
| `Short?` | `.assert(): ShortAssert` |
| `Int?` | `.assert(): IntegerAssert` |
| `Long?` | `.assert(): LongAssert` |
| `Float?` | `.assert(): FloatAssert` |
| `Double?` | `.assert(): DoubleAssert` |
| `BigDecimal?` | `.assert(): BigDecimalAssert` |
| `String?` | `.assert(): StringAssert` |
| `T?` | `.assert(): ObjectAssert<T>` |
| `T : Comparable<T>?` | `.assert(): GenericComparableAssert<T>` |
| `Iterable<T>?` | `.assert(): IterableAssert<T>` |
| `Iterator<T>?` | `.assert(): IteratorAssert<T>` |
| `Collection<T>?` | `.assert(): CollectionAssert<T>` |
| `Array<T>?` | `.assert(): ObjectArrayAssert<T>` |
| `List<T>?` | `.assert(): ListAssert<T>` |
| `Optional<T>?` | `.assert(): OptionalAssert<T>` |
| `Map<K,V>?` | `.assert(): MapAssert<K,V>` |
| `Stream<T>?` | `.assert(): ListAssert<T>` |
| `Date?` | `.assert(): DateAssert` |
| `ZonedDateTime?` | `.assert(): ZonedDateTimeAssert` |
| `Temporal?` | `.assert(): TemporalAssert` |
| `LocalDateTime?` | `.assert(): LocalDateTimeAssert` |
| `OffsetDateTime?` | `.assert(): OffsetDateTimeAssert` |
| `OffsetTime?` | `.assert(): OffsetTimeAssert` |
| `LocalTime?` | `.assert(): LocalTimeAssert` |
| `LocalDate?` | `.assert(): LocalDateAssert` |
| `YearMonth?` | `.assert(): YearMonthAssert` |
| `Instant?` | `.assert(): InstantAssert` |
| `Duration?` | `.assert(): DurationAssert` |
| `Period?` | `.assert(): PeriodAssert` |
| `Path?` | `.assert(): PathAssert` |
| `File?` | `.assert(): FileAssert` |
| `URL?` | `.assert(): UrlAssert` |
| `URI?` | `.assert(): UriAssert` |
| `Future<V>?` | `.assert(): FutureAssert<V>` |
| `CompletableFuture<V>?` | `.assert(): CompletableFutureAssert<V>` |
| `CompletionStage<V>?` | `.assert(): CompletionStageAssert<V>` |
| `Predicate<T>?` | `.assert(): PredicateAssert<T>` |
| `Throwable?` | `.assert(): ThrowableAssert<T>` |

## BooleanAssert

```kotlin
true.assert().isTrue()
false.assert().isFalse()
nullableBool.assert().isNull()
```

## IntegerAssert / LongAssert / etc.

```kotlin
value.assert()
    .isEqualTo(42)
    .isNotEqualTo(0)
    .isGreaterThan(0)
    .isLessThan(100)
    .isBetween(0, 100)
    .isPositive()
    .isNegative()
    .isZero()
```

## StringAssert

```kotlin
"hello".assert()
    .isEqualTo("hello")
    .isNotEqualTo("world")
    .startsWith("hel")
    .endsWith("llo")
    .contains("ell")
    .doesNotContain("xyz")
    .hasLength(5)
    .isBlank()
    .isNotBlank()
    .matches("^hel.*")
    .doesNotMatch("^xyz")
```

## CollectionAssert / IterableAssert

```kotlin
listOf(1, 2, 3).assert()
    .hasSize(3)
    .contains(1, 2)
    .contains(2)
    .doesNotContain(4)
    .containsExactly(1, 2, 3)
    .containsExactlyInAnyOrder(3, 2, 1)
    .allMatch { it > 0 }
    .anyMatch { it == 2 }
    .noneMatch { it < 0 }
    .isEmpty()
    .isNotEmpty()
```

## ListAssert

```kotlin
listOf("a", "b", "c").assert()
    .hasSize(3)
    .contains("a")
    .element(0).isEqualTo("a")
    .element(1).isNotNull()
    .first().isEqualTo("a")
    .last().isEqualTo("c")
```

## MapAssert

```kotlin
mapOf("key1" to "value1", "key2" to "value2").assert()
    .hasSize(2)
    .containsKey("key1")
    .doesNotContainKey("key3")
    .containsValue("value1")
    .doesNotContainValue("value3")
    .containsEntry("key1", "value1")
    .doesNotContainEntry("key1", "value2")
```

## OptionalAssert

```kotlin
Optional.of("value").assert()
    .isPresent()
    .contains("value")
    .doesNotContain("other")

Optional.empty<String>().assert()
    .isEmpty()
    .isNotPresent()
```

## PathAssert / FileAssert

```kotlin
path.assert()
    .exists()
    .doesNotExist()
    .isReadable()
    .isWritable()
    .isExecutable()
    .isRegularFile()
    .isDirectory()
    .hasFileName("test.txt")
    .hasParent("parent")
    .startsWithRawPath(Paths.get("/tmp"))

file.assert()
    .exists()
    .isFile()
    .isDirectory()
    .canRead()
    .canWrite()
    .hasName("test.txt")
    .hasSize(100)
```

## URLAssert / UriAssert

```kotlin
url.assert()
    .hasProtocol("https")
    .hasHost("example.com")
    .hasPort(443)
    .hasPath("/api/v1")
    .hasQuery("key=value")
    .hasNoQuery()

uri.assert()
    .hasScheme("https")
    .hasHost("example.com")
    .hasPath("/api/v1")
    .hasQuery("key=value")
```

## Date / Time Assertions

```kotlin
date.assert()
    .isToday()
    .isBefore(otherDate)
    .isAfter(otherDate)
    .isBetween(start, end)

instant.assert()
    .isBefore(otherInstant)
    .isAfter(otherInstant)
    .isEqualTo(otherInstant)

localDate.assert()
    .isToday()
    .hasYear(2024)
    .hasMonth(3)
    .hasDayOfMonth(15)
    .isBefore(otherDate)
    .isAfter(otherDate)

localDateTime.assert()
    .isToday()
    .hasYear(2024)
    .hasHour(10)
    .hasMinute(30)
    .isBefore(other)

zonedDateTime.assert()
    .isToday()
    .hasZone(ZoneId.systemDefault())
    .hasOffset(ZoneOffset.UTC)
```

## FutureAssert / CompletableFutureAssert

```kotlin
future.assert()
    .isDone()
    .isNotDone()
    .isCancelled()
    .isNotCancelled()
    .isCompleted()
    .isCompletedWithValue("result")
    .isCompletedWithException()
    .willCompleteWithin(1, TimeUnit.SECONDS)

completableFuture.assert()
    .isCompleted()
    .isCompletedWithValue("success")
    .isCompletedExceptionally()
```

## ThrowableAssert

```kotlin
exception.assert()
    .hasMessage("error message")
    .hasNoMessage()
    .hasCause(originalCause)
    .hasRootCause(rootCause)
    .isInstanceOf(IllegalArgumentException::class.java)
    .hasStackTraceContaining("at line 42")
    .hasSuppressed(suppressed)
```

## assertThrownBy

```kotlin
// Kotlin reified version (preferred)
assertThrownBy<IllegalArgumentException> {
    throw IllegalArgumentException("invalid")
}.assert()
    .hasMessage("invalid")
    .isInstanceOf(IllegalArgumentException::class.java)

// With class parameter
assertThrownBy(IllegalArgumentException::class.java) {
    throw IllegalArgumentException("invalid")
}.assert().hasMessage("invalid")
```

## PredicateAssert

```kotlin
val isEven = Predicate<Int> { it % 2 == 0 }
isEven.assert()
    .accepts(2, 4, 6)
    .rejects(1, 3, 5)
```
