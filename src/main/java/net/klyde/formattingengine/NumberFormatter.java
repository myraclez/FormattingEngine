/**
 * FormattingEngine by myraclez.
 */
package net.klyde.formattingengine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Formatting utilities for whole and decimal numbers.
 */
public final class NumberFormatter {

    private static final String[] SUFFIXES = {"", "k", "m", "b", "t", "q"};

    private NumberFormatter() {
    }

    /**
     * Formats a number with grouping separators.
     * Example: 1000000 -> "1,000,000".
     */
    public static String format(long value) {
        return format((double) value, 0);
    }

    /**
     * Formats a decimal number with grouping separators.
     */
    public static String format(double value, int decimals) {
        validateDecimals(decimals);

        DecimalFormat format = new DecimalFormat(
                "#,##0" + (decimals > 0 ? "." + "#".repeat(decimals) : ""),
                DecimalFormatSymbols.getInstance(Locale.US)
        );
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(decimals);
        format.setMinimumFractionDigits(0);

        return format.format(value);
    }

    /**
     * Formats a number in compact form using two decimal places.
     * Example: 1890000 -> "1.89m".
     */
    public static String compact(long value) {
        return compact(value, 2);
    }

    /**
     * Formats a number in compact form with the requested maximum decimals.
     * Example: compact(1890000, 1) -> "1.9m".
     */
    public static String compact(long value, int decimals) {
        validateDecimals(decimals);

        if (value == 0) {
            return "0";
        }

        double absolute = Math.abs((double) value);
        int tier = 0;

        while (absolute >= 1000 && tier < SUFFIXES.length - 1) {
            absolute /= 1000;
            tier++;
        }

        double signed = value < 0 ? -absolute : absolute;
        return formatCompact(signed, SUFFIXES[tier], decimals);
    }

    /**
     * Formats a decimal number in compact form with the requested maximum decimals.
     * Example: compact(7723987.44, 2) -> "7.72m".
     */
    public static String compact(double value, int decimals) {
        validateDecimals(decimals);

        if (value == 0) {
            return "0";
        }

        double absolute = Math.abs(value);
        int tier = 0;

        while (absolute >= 1000 && tier < SUFFIXES.length - 1) {
            absolute /= 1000;
            tier++;
        }

        double signed = value < 0 ? -absolute : absolute;
        return formatCompact(signed, SUFFIXES[tier], decimals);
    }

    private static String formatCompact(double value, String suffix, int decimals) {
        BigDecimal rounded = BigDecimal.valueOf(value).setScale(decimals, RoundingMode.HALF_UP);

        // Rounding can promote 999.99k to 1.00m.
        if (rounded.abs().compareTo(BigDecimal.valueOf(1000)) >= 0) {
            int suffixIndex = indexOfSuffix(suffix);
            if (suffixIndex < SUFFIXES.length - 1) {
                rounded = rounded.movePointLeft(3);
                return stripTrailingZeros(rounded) + SUFFIXES[suffixIndex + 1];
            }
        }

        return stripTrailingZeros(rounded) + suffix;
    }

    private static int indexOfSuffix(String suffix) {
        for (int i = 0; i < SUFFIXES.length; i++) {
            if (SUFFIXES[i].equals(suffix)) {
                return i;
            }
        }
        return 0;
    }

    private static String stripTrailingZeros(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }

    private static void validateDecimals(int decimals) {
        if (decimals < 0 || decimals > 10) {
            throw new IllegalArgumentException("decimals must be between 0 and 10");
        }
    }
}
