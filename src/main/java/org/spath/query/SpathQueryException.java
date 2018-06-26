package org.spath.query;

public class SpathQueryException extends RuntimeException {
    private static final long serialVersionUID = 1548145984150934518L;

    public SpathQueryException(String message) {
        super(message);
    }

    public SpathQueryException(String message, Exception exception) {
        super(message, exception);
    }
}
