package de.fkkaiser.bundle;

/**
 * Exception thrown when there is a problem parsing a JSON access expression.
 */
public class JAccessParseException extends Exception {

    public JAccessParseException(String message) {
        super(message);
    }

    public JAccessParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
