# I want to make clear THIS IS MADE WITH AI


# FormattingEngine


**FormattingEngine** is a small, dependency-free Java library by **myraclez** for formatting and parsing numbers and durations.

## Requirements

- Java 17+

## Number formatting

Standard grouped formatting, for both `long` and `double`:

```java
import net.klyde.formattingengine.NumberFormatter;

NumberFormatter.format(1_000_000);
// "1,000,000"

NumberFormatter.format(1_234_567.8, 2);
// "1,234,567.8"
```

Compact formatting:

```java
NumberFormatter.compact(1_890_000);
// "1.89m"

NumberFormatter.compact(1_890_000, 1);
// "1.9m"

// For double input, always pass the decimals explicitly
NumberFormatter.compact(1_890_000.0, 2);
// "1.89m"
```

`compact()` is the name used for the abbreviated magnitude style because it describes the format directly: large numbers are compressed into `k`, `m`, `b`, and `t`.

## Number parsing

`parse` returns a `double`, so grouped, compact, and plain decimal input all work:

```java
import net.klyde.formattingengine.NumberParser;

NumberParser.parse("1,890,000");
// 1890000.0

NumberParser.parse("1.89m");
// 1890000.0

NumberParser.parse("1.234567k");
// 1234.567

NumberParser.parse("12.5");
// 12.5
```

Use `parseLong` when a whole number is required; it rounds halves away from zero
and rejects anything outside the `long` range:

```java
NumberParser.parseLong("1.89m");
// 1890000

NumberParser.parseLong("12.5");
// 13
```

Both throw `FormattingException` for null, blank, or malformed input.

Values whose magnitude exceeds `2^53` lose precision, as is inherent to `double`.

Supported compact suffixes:

- `k` = thousand
- `m` = million
- `b` = billion
- `t` = trillion
- `q` = quadrillion 

## Time formatting

```java
import net.klyde.formattingengine.TimeFormatter;

TimeFormatter.format(122_581_000L);
// "1d 9h 3m 1s"
```

With configuration:

```java
import net.klyde.formattingengine.TimeFormatOptions;

TimeFormatOptions options = TimeFormatOptions.builder()
        .showDays(true)
        .showHours(false)
        .showMinutes(true)
        .showSeconds(true)
        .showMillis(false)
        .build();

TimeFormatter.format(122_581_000L, options);
// "1d 3m 1s"
```

## Time parsing

```java
TimeFormatter.parse("1d 9h 3m 1s");
// 122581000

TimeFormatter.parse("2h 30m");
// 9000000
```

Supported units:

- `d` = days
- `h` = hours
- `m` = minutes
- `s` = seconds
- `ms` = milliseconds

## API

```text
net.klyde.formattingengine
├── NumberFormatter
├── NumberParser
├── TimeFormatter
├── TimeFormatOptions
└── FormattingException
```

The runtime library has no external dependencies.

## Author

**myraclez**
