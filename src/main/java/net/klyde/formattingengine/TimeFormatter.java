/**
 * FormattingEngine by myraclez.
 */
package net.klyde.formattingengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formats and parses durations.
 */
public final class TimeFormatter {

    private static final long MILLIS_PER_SECOND = 1_000L;
    private static final long MILLIS_PER_MINUTE = 60_000L;
    private static final long MILLIS_PER_HOUR = 3_600_000L;
    private static final long MILLIS_PER_DAY = 86_400_000L;

    private static final Pattern PART =
            Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(ms|[dhms])", Pattern.CASE_INSENSITIVE);

    private TimeFormatter() {
    }

    /**
     * Formats milliseconds with the default options.
     * Example: 122581000 -> "1d 9h 3m 1s".
     */
    public static String format(long millis) {
        return format(millis, TimeFormatOptions.defaults());
    }

    /**
     * Formats milliseconds using the supplied options.
     *
     * Disabled units are omitted and their value is carried by the next
     * enabled smaller unit only when that smaller unit is displayed.
     */
    public static String format(long millis, TimeFormatOptions options) {
        if (millis < 0) {
            throw new IllegalArgumentException("millis cannot be negative");
        }
        if (options == null) {
            throw new IllegalArgumentException("options cannot be null");
        }

        long remaining = millis;
        List<String> parts = new ArrayList<>(5);

        long[] values = {
                remaining / MILLIS_PER_DAY,
                (remaining % MILLIS_PER_DAY) / MILLIS_PER_HOUR,
                (remaining % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE,
                (remaining % MILLIS_PER_MINUTE) / MILLIS_PER_SECOND,
                remaining % MILLIS_PER_SECOND
        };
        String[] suffixes = {"d", "h", "m", "s", "ms"};
        boolean[] shown = {
                options.showDays(),
                options.showHours(),
                options.showMinutes(),
                options.showSeconds(),
                options.showMillis()
        };

        for (int i = 0; i < values.length; i++) {
            if (shown[i] && (values[i] > 0 || !parts.isEmpty())) {
                parts.add(values[i] + suffixes[i]);
            }
        }

        if (parts.isEmpty()) {
            // Preserve zero rather than returning an empty string.
            return options.showMillis() ? "0ms" :
                    options.showSeconds() ? "0s" :
                    options.showMinutes() ? "0m" :
                    options.showHours() ? "0h" : "0d";
        }

        return String.join(" ", parts);
    }

    /**
     * Parses a duration such as "1d 9h 3m 1s" back to milliseconds.
     */
    public static long parse(String input) {
        if (input == null || input.isBlank()) {
            throw new FormattingException("Time cannot be null or blank");
        }

        String normalized = input.trim().toLowerCase(Locale.ROOT);
        Matcher matcher = PART.matcher(normalized);

        long total = 0;
        int end = 0;
        boolean found = false;

        while (matcher.find()) {
            // Reject unknown text between valid components.
            if (!normalized.substring(end, matcher.start()).trim().isEmpty()) {
                throw new FormattingException("Invalid time: " + input);
            }

            double amount;
            try {
                amount = Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException exception) {
                throw new FormattingException("Invalid time: " + input, exception);
            }

            long multiplier = switch (matcher.group(2)) {
                case "d" -> MILLIS_PER_DAY;
                case "h" -> MILLIS_PER_HOUR;
                case "m" -> MILLIS_PER_MINUTE;
                case "s" -> MILLIS_PER_SECOND;
                case "ms" -> 1L;
                default -> throw new FormattingException("Invalid time unit");
            };

            double contribution = amount * multiplier;
            if (contribution > Long.MAX_VALUE - total) {
                throw new FormattingException("Time is outside the long range: " + input);
            }

            total += Math.round(contribution);
            end = matcher.end();
            found = true;
        }

        if (!found || !normalized.substring(end).trim().isEmpty()) {
            throw new FormattingException("Invalid time: " + input);
        }

        return total;
    }
}
