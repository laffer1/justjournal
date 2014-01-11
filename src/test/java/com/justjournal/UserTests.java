/*
 * Copyright (c) 2013 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package com.justjournal;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Lucas Holt
 */
public class UserTests {

    @BeforeClass
    public static void setup() throws Exception {
        Util.setupDb();
    }

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("jjsite");
    }

    @Test
    public void testGetUserName() throws Exception {
        assertTrue(user.getUserName().equals("jjsite"));
    }

    @Test
    public void testSetUserName() throws Exception {
        user.setUserName("laffer1");
        assertTrue(user.getUserName().equals("laffer1"));
    }

    @Test
    public void testGetUserId() throws Exception {
        assertTrue(user.getUserId() == 2);
    }

    @Test
    public void testSetUserId() throws Exception {
        user.setUserId(1);
        assertTrue(user.getUserId() == 1);
    }

    @Test
    public void testGetType() throws Exception {
        assertTrue(user.getType() == 0);
    }

    @Test
    public void testSetType() throws Exception {

    }

    @Test
    public void testGetFirstName() throws Exception {

    }

    @Test
    public void testSetFirstName() throws Exception {

    }
}
