package com.justjournal;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Lucas Holt
 */
public class LoginTests {

    @Test
    public void testIsPasswordLowercaseOnly() {
        boolean result = Login.isPassword("abcdefg");
        assertTrue(result);
    }

    @Test
    public void testIsPasswordUpperCaseeOnly() {
        boolean result = Login.isPassword("ABCDEFG");
        assertTrue(result);
    }

    @Test
    public void testIsPasswordAlphaNum() {
        boolean result = Login.isPassword("ABCDEFGkadkdk39393");
        assertTrue(result);
    }

    @Test
    public void testIsPasswordSpecial() {
        boolean result = Login.isPassword("_@.!&*#$?^ ");
        assertTrue(result);
    }

    /**
     * We don't allow %
     */
    @Test
    public void testIsPasswordInvalid() {
        boolean result = Login.isPassword("%_@.!&*#$?^ ");
        assertFalse(result);
    }

    @Test
    public void testIsUserNameSpecial() {
        boolean result = Login.isUserName("%_@.!&*#$?^ ");
        assertFalse(result);
    }

    @Test
    public void testIsUserNameSpaceInvalid() {
        boolean result = Login.isUserName("foo bar");
        assertFalse(result);
    }

    @Test
    public void testIsUserName() {
        boolean result = Login.isUserName("testing");
        assertTrue(result);
    }

    @Test
    public void testIsUserNameWeirdButValid() {
        boolean result = Login.isUserName("testing123_");
        assertTrue(result);
    }
}
