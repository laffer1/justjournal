package com.justjournal.utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Lucas Holt
 */
public class DNSUtilTests {

    @Test
    public void testUrlDomainValid() {
        boolean result = DNSUtil.isUrlDomainValid("http://justjournal.com/users/jjsite");
        assertTrue(result);
    }

    @Test
    public void testGetDomainFromEmail() {
        String result = DNSUtil.getDomainFromEmail("test@justjournal.com");
        assertEquals("justjournal.com", result);
    }

    @Test
    public void testIsEmailDomainValid() {
        boolean result = DNSUtil.isEmailDomainValid("test@justjournal.com");
        assertTrue(result);
    }

    @Test
    public void testIsEmailDomainInvalid() {
        boolean result = DNSUtil.isDomainValid("test@iamareallylonginvaliddomainname.justjournal.com");
        assertFalse(result);
    }

    @Test
    public void testIsDomainValid() {
        boolean result = DNSUtil.isDomainValid("justjournal.com");
        assertTrue(result);
    }

    @Test
    public void testIsDomainInvalid() {
        boolean result = DNSUtil.isDomainValid("iamareallylonginvaliddomainname.justjournal.com");
        assertFalse(result);
    }

    @Test
    public void testIsDomainNull() {
        boolean result = DNSUtil.isDomainValid(null);
        assertFalse(result);
    }
}
