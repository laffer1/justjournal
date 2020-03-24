package com.justjournal.ctl.error;

import java.util.Map;

/**
 * @author Lucas Holt
 */
public class ErrorHandler {

    private static final String ERROR_KEY = "error";

    public static Map<String, String> modelError(final String errorMessage) {
        return java.util.Collections.singletonMap(ERROR_KEY, errorMessage);
    }
}
