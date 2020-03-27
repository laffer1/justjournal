package com.justjournal.services;

import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;

import static com.justjournal.services.BaseXmlRpcService.FAULT_CODE;
import static com.justjournal.services.BaseXmlRpcService.FAULT_STRING;
import static org.junit.Assert.assertEquals;

/**
 * @author Lucas Holt
 */
public class BaseXmlRpcServiceTests {

    final BaseXmlRpcService baseXmlRpcService = new BaseXmlRpcService();

    @Test
    public void testError() {
        HashMap<Object, Serializable> result = baseXmlRpcService.error("my string");
        assertEquals(4, result.get(FAULT_CODE));
        assertEquals("my string", result.get(FAULT_STRING));
    }
}
