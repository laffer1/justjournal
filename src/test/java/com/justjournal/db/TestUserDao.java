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

package com.justjournal.db;

import org.junit.*;

import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Lucas Holt
 */
public class TestUserDao {

    @Test
    public void testView() {
        UserTo user = UserDao.view("jjsite");

        assertEquals("jjsite", user.getUserName());
        assertTrue(user.getId() > 0);
        assertEquals("", user.getPassword());
    }

    @Test
    public void testViewWithPassword() {
        UserTo user = UserDao.viewWithPassword("jjsite");

        assertEquals("jjsite", user.getUserName());
        assertTrue(user.getId() > 0);
        assertTrue(user.getPassword().length() > 0);
        assertTrue(user.getPasswordSha1().length() > 0);
    }

    @Test
    public void testViewWithBadUser() {
        UserTo user = UserDao.view("realfakeuserstring");
        assertNull(user);
    }

    @Test
    public void testMemberList() {
        Collection list = UserDao.memberList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testNewUsers() {
        Collection list = UserDao.newUsers();
        assertNotNull(list);
        assertEquals(5, list.size());
    }
}
