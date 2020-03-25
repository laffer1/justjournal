package com.justjournal.exception;

/**
 * @author Lucas Holt
 */
public class ThumbnailException extends RuntimeException {
    public ThumbnailException() {
        super("Could not create thumbnail");
    }

    public ThumbnailException(final Exception e) {
        super(e);
    }

    public ThumbnailException(final String msg) {
        super(msg);
    }

    public ThumbnailException(final String msg, final Exception e) {
        super(msg, e);
    }
}
