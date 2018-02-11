package org.spath;

public class SpathException extends Exception {
    private static final long serialVersionUID = 1548145984150934518L;

    public SpathException(String message) {
        super(message);
    }

    public SpathException(String message, Exception exception) {
        super(message, exception);
    }
}
