package com.justjournal.exception;

/**
 * @author Lucas Holt
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super("Not Implemented");
    }

    public NotImplementedException(final Exception e) {
        super(e);
    }

    public NotImplementedException(final String msg) {
        super(msg);
    }

    public NotImplementedException(final String msg, final Exception e) {
        super(msg, e);
    }
}
