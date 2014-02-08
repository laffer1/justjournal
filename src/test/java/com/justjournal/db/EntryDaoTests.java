/*
 * Copyright (c) 2014 Lucas Holt
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
import com.justjournal.db.model.EntryTo;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collection;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * @author Lucas Holt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/resources/mvc-dispatcher-servlet.xml")
public class EntryDaoTests {
    private final static String TEST_USER = "jjsite";
    private EntryDao entryDao = new EntryDaoImpl();


    @BeforeClass
    public static void setup() throws Exception {
        Util.setupDb();
    }

    @Test
    public void viewFriends() {
        Collection<EntryTo> list = entryDao.viewFriends(1, 0);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testViewPublic() {
        assertNotNull(entryDao);
        Collection<EntryTo> entries = entryDao.view(TEST_USER, false);
        assertNotNull(entries);
        assertTrue(entries.size() > 15);
    }

    @Test
    public void testViewThisUser() {
        assertNotNull(entryDao);
        Collection<EntryTo> entries = entryDao.view(TEST_USER, true);
        assertNotNull(entries);
        assertTrue(entries.size() > 15);
    }

    @Test
    public void testViewPublicNoSkip() {
        assertNotNull(entryDao);
        Collection<EntryTo> entries = entryDao.view(TEST_USER, false, 0);
        assertNotNull(entries);
        assertTrue(entries.size() > 15);
    }

    @Test
    public void testViewThisUserNoSkip() {
        assertNotNull(entryDao);
        Collection<EntryTo> entries = entryDao.view(TEST_USER, true, 0);
        assertNotNull(entries);
        assertTrue(entries.size() > 15);
    }
}
