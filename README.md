# I want to make clear THIS IS MADE WITH AI


# FormattingEngine

**FormattingEngine** is a small, dependency-free Java library by **myraclez** for formatting and parsing numbers and durations.

## Requirements

- Java 17+

## Number formatting

Standard grouped formatting:

```java
import net.klyde.formattingengine.NumberFormatter;

NumberFormatter.format(1_000_000);
// "1,000,000"
```

Compact formatting:

```java
NumberFormatter.compact(1_890_000);
// "1.89m"

NumberFormatter.compact(1_890_000, 1);
// "1.9m"
```

`compact()` is the name used for the abbreviated magnitude style because it describes the format directly: large numbers are compressed into `k`, `m`, `b`, and `t`.

## Number parsing

```java
import net.klyde.formattingengine.NumberParser;

NumberParser.parse("1,890,000");
// 1890000

NumberParser.parse("1.89m");
// 1890000
```

Supported compact suffixes:

- `k` = thousand
- `m` = million
- `b` = billion
- `t` = trillion
- 

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
