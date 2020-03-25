package com.justjournal.exception;

/**
 * @author Lucas Holt
 */
public class HashNotSupportedException extends RuntimeException {
    public HashNotSupportedException() {
        super("hash not supported");
    }

    public HashNotSupportedException(final Exception e) {
        super(e);
    }

    public HashNotSupportedException(final String msg) {
        super(msg);
    }

    public HashNotSupportedException(final String msg, final Exception e) {
        super(msg, e);
    }
}
