package net.klyde.formattingengine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormattingEngineTest {

    @Test
    void numbers() {
        assertEquals("1,000,000", NumberFormatter.format(1_000_000));
        assertEquals("1.89m", NumberFormatter.compact(1_890_000));
        assertEquals("1.9m", NumberFormatter.compact(1_890_000, 1));
        assertEquals("-1.5k", NumberFormatter.compact(-1_500, 1));
        assertEquals("1.01m", NumberFormatter.compact(1_005_000, 2));
    }

    @Test
    void numberParsing() {
        assertEquals(1_890_000L, NumberParser.parse("1,890,000"));
        assertEquals(1_890_000L, NumberParser.parse("1.89m"));
        assertEquals(1_900_000L, NumberParser.parse("1.9M"));
        assertEquals(1_500L, NumberParser.parse("1.5k"));
        assertEquals(-2_000L, NumberParser.parse("-2k"));
    }

    @Test
    void time() {
        long value = 86_400_000L + 9 * 3_600_000L + 3 * 60_000L + 1_000L;
        assertEquals("1d 9h 3m 1s", TimeFormatter.format(value));
        assertEquals(value, TimeFormatter.parse("1d 9h 3m 1s"));
        assertEquals(9_000_000L, TimeFormatter.parse("2h 30m"));
    }

    @Test
    void timeOptions() {
        TimeFormatOptions options = TimeFormatOptions.builder()
                .showDays(true)
                .showHours(false)
                .showMinutes(true)
                .showSeconds(true)
                .showMillis(false)
                .build();

        assertEquals("1d 3m 1s", TimeFormatter.format(86_400_000L + 9 * 3_600_000L + 3 * 60_000L + 1_000L, options));
    }

    @Test
    void invalidInput() {
        assertThrows(FormattingException.class, () -> NumberParser.parse("abc"));
        assertThrows(FormattingException.class, () -> TimeFormatter.parse("hello"));
    }
}
