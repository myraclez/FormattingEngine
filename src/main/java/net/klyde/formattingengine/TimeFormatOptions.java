/**
 * FormattingEngine by myraclez.
 */
package net.klyde.formattingengine;

/**
 * Immutable options controlling which time units are displayed.
 */
public final class TimeFormatOptions {

    private final boolean showDays;
    private final boolean showHours;
    private final boolean showMinutes;
    private final boolean showSeconds;
    private final boolean showMillis;

    private TimeFormatOptions(Builder builder) {
        this.showDays = builder.showDays;
        this.showHours = builder.showHours;
        this.showMinutes = builder.showMinutes;
        this.showSeconds = builder.showSeconds;
        this.showMillis = builder.showMillis;
    }

    public static TimeFormatOptions defaults() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean showDays() { return showDays; }
    public boolean showHours() { return showHours; }
    public boolean showMinutes() { return showMinutes; }
    public boolean showSeconds() { return showSeconds; }
    public boolean showMillis() { return showMillis; }

    public static final class Builder {
        private boolean showDays = true;
        private boolean showHours = true;
        private boolean showMinutes = true;
        private boolean showSeconds = true;
        private boolean showMillis = false;

        public Builder showDays(boolean value) {
            this.showDays = value;
            return this;
        }

        public Builder showHours(boolean value) {
            this.showHours = value;
            return this;
        }

        public Builder showMinutes(boolean value) {
            this.showMinutes = value;
            return this;
        }

        public Builder showSeconds(boolean value) {
            this.showSeconds = value;
            return this;
        }

        public Builder showMillis(boolean value) {
            this.showMillis = value;
            return this;
        }

        public TimeFormatOptions build() {
            if (!showDays && !showHours && !showMinutes && !showSeconds && !showMillis) {
                throw new IllegalArgumentException("At least one time unit must be enabled");
            }
            return new TimeFormatOptions(this);
        }
    }
}
