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

package com.justjournal.services;

import com.justjournal.Util;
import com.justjournal.model.Statistics;
import com.justjournal.model.UserStatistics;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * @author Lucas Holt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/resources/mvc-dispatcher-servlet.xml")
public class ServiceTests {

    private static StatisticsService statisticsService;

    public void setStatisticsService(StatisticsService statisticsService1) {
        this.statisticsService = statisticsService1;
    }

    @BeforeClass
    public static void setup() throws Exception {
        Util.setupDb();
    }

    @Test
    public void testGetStatistics() throws ServiceException {
        Statistics statistics = statisticsService.getStatistics();
        assertNotNull(statistics);
        assertTrue(statistics.getComments() > 0);
        assertTrue(statistics.getEntries() > 0);
        assertTrue(statistics.getTags() > 0);
        assertTrue(statistics.getStyles() > 0);
        assertTrue(statistics.getPrivateEntries() > 0);
        assertTrue(statistics.getPublicEntries() > 0);
        assertTrue(statistics.getFriendsEntries() > 0);
    }

    @Test
    public void testGetUserStatistics() throws ServiceException {
        UserStatistics statistics = statisticsService.getUserStatistics("jjsite");
        assertNotNull(statistics);
        assertEquals("jjsite", statistics.getUsername());
        assertTrue(statistics.getEntryCount() > 0);
        assertTrue(statistics.getCommentCount() > 0);
    }

    @Test
    public void testGetUserStatisticsBadUser() throws ServiceException {
        UserStatistics statistics = statisticsService.getUserStatistics("root");
        assertNull(statistics);
    }
}
