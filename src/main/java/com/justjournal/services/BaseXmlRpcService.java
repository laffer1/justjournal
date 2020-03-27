package com.justjournal.services;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Lucas Holt
 */
public class BaseXmlRpcService {
    protected static final String ERROR_USER_AUTH = "User authentication failed: ";
    protected static final String ERROR_ENTRY_ID =  "Invalid entry id ";

    protected static final String TITLE_KEY = "title";
    protected static final String DESC_KEY = "description";

    protected static final String FAULT_CODE = "faultCode";
    protected static final String FAULT_STRING = "faultString";

    protected HashMap<Object, Serializable> error(final String faultString) {
        final HashMap<Object, Serializable> s = new HashMap<>();
        s.put(FAULT_CODE, 4);
        s.put(FAULT_STRING, faultString);
        return s;
    }
}
