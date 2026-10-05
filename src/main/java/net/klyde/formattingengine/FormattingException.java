/**
 * FormattingEngine by myraclez.
 */
package net.klyde.formattingengine;

/**
 * Thrown when a formatted value cannot be parsed.
 */
public class FormattingException extends IllegalArgumentException {

    public FormattingException(String message) {
        super(message);
    }

    public FormattingException(String message, Throwable cause) {
        super(message, cause);
    }
}
