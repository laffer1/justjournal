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

package com.justjournal.rss;

import com.justjournal.Application;
import com.justjournal.ApplicationTest;
import com.justjournal.model.Entry;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.SecurityDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Lucas Holt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
public class RssTests {
    private final static String TEST_USER = "testuser";
    @Autowired
    private EntryRepository entryDao;
    @Autowired
    private SecurityDao securityDao;

    @Autowired
    private Rss rss;

    @Test
    public void testPopulate() {
        
        final java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        calendar.setTime(new java.util.Date());
        final Collection<Entry> entries = entryDao.findByUsernameAndSecurity(TEST_USER, securityDao.findOne(2));
        assertTrue(entries.size() > 0);
        rss.populate(entries);

        assertTrue(rss.size() > 0);

        final String xml = rss.toXml();
        assertTrue(xml.contains("<item"));
    }

    @Test
    public void testWebmaster() {
        final String webmaster = "test@test.com (test)";

        rss.setWebMaster(webmaster);
        assertEquals(webmaster, rss.getWebMaster());
    }

}
