/**
 * FormattingEngine by myraclez.
 */
package net.klyde.formattingengine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts formatted/readable numbers back into numbers.
 */
public final class NumberParser {

    private static final Pattern READABLE =
            Pattern.compile("^([+-]?\\d+(?:\\.\\d+)?)\\s*([kmbtq])?$", Pattern.CASE_INSENSITIVE);

    private NumberParser() {
    }

    /**
     * Parses grouped or readable numbers into a double.
     *
     * <p>Examples:
     * <pre>
     * "1,890,000" -> 1890000.0
     * "1.89m"     -> 1890000.0
     * "12.5"      -> 12.5
     * </pre>
     *
     * <p>Values whose magnitude exceeds 2^53 lose precision, as is inherent to double.
     *
     * @throws FormattingException if the input is null, blank, malformed, or out of double range
     */
    public static double parse(String input) {
        BigDecimal number = toBigDecimal(input);
        double parsed = number.doubleValue();

        if (Double.isInfinite(parsed)) {
            throw new FormattingException("Number is outside the double range: " + input);
        }

        return parsed;
    }

    /**
     * Parses grouped or readable numbers into a long, rounding halves away from zero.
     *
     * <p>Examples:
     * <pre>
     * "1,890,000" -> 1890000
     * "1.89m"     -> 1890000
     * "12.5"      -> 13
     * </pre>
     *
     * @throws FormattingException if the input is null, blank, malformed, or out of long range
     */
    public static long parseLong(String input) {
        try {
            return toBigDecimal(input).setScale(0, RoundingMode.HALF_UP).longValueExact();
        } catch (ArithmeticException exception) {
            throw new FormattingException("Number is outside the long range: " + input, exception);
        }
    }

    private static BigDecimal toBigDecimal(String input) {
        if (input == null || input.isBlank()) {
            throw new FormattingException("Number cannot be null or blank");
        }

        String normalized = input.trim().replace(",", "").replace("_", "");
        Matcher matcher = READABLE.matcher(normalized);

        if (!matcher.matches()) {
            throw new FormattingException("Invalid number: " + input);
        }

        BigDecimal number;
        try {
            number = new BigDecimal(matcher.group(1));
        } catch (NumberFormatException exception) {
            throw new FormattingException("Invalid number: " + input, exception);
        }

        String suffix = matcher.group(2);
        if (suffix != null) {
            number = number.multiply(BigDecimal.valueOf(multiplier(suffix.toLowerCase(Locale.ROOT))));
        }

        return number;
    }

    private static long multiplier(String suffix) {
        return switch (suffix.toLowerCase(Locale.ROOT)) {
            case "k" -> 1_000L;
            case "m" -> 1_000_000L;
            case "b" -> 1_000_000_000L;
            case "t" -> 1_000_000_000_000L;
            case "q" -> 1_000_000_000_000_000L;
            default -> throw new FormattingException("Unknown suffix: " + suffix);
        };
    }
}