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

import com.justjournal.Util;
import com.justjournal.db.model.UserTo;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Lucas Holt
 */
public class UserDaoTests {

    @BeforeClass
    public static void setup() throws Exception {
        Util.setupDb();
    }

    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testGet() throws Exception {
        UserTo userTo = UserDao.get(16);
        assertNotNull(userTo);
        assertEquals("jjsite", userTo.getUserName());
        assertEquals(16, userTo.getId());
    }

    @Test
    public void testGetWithUserName() throws Exception {
        UserTo userTo = UserDao.get("jjsite");
        assertNotNull(userTo);
        assertEquals("jjsite", userTo.getUserName());
        assertEquals(16, userTo.getId());
    }

    @Test
    public void testMemberList() throws Exception {
        Collection<UserTo> users = UserDao.memberList();
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    public void testNewUsers() throws Exception {
        Collection<UserTo> users = UserDao.newUsers();
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    public void testFriends() throws Exception {

    }

    @Test
    public void testFriendsof() throws Exception {

    }

    @Test
    public void testGetJournalPreferences() throws Exception {

    }

    @Test
    public void testUpdateSecurity() throws Exception {

    }
}
