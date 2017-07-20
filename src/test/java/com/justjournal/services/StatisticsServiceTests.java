package com.justjournal.services;

import com.justjournal.ApplicationTest;
import com.justjournal.model.Statistics;
import com.justjournal.model.UserStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * @author Lucas Holt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
public class StatisticsServiceTests {
    private static final String TEST_USER = "testuser";
    
    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void testGetStatistics() throws ServiceException {
        final Statistics statistics = statisticsService.getStatistics();
        assertNotNull(statistics);
        assertTrue(statistics.getComments() > 0);
        assertTrue(statistics.getEntries() > 0);
        //assertTrue(statistics.getTags() > 0);
        // assertTrue(statistics.getStyles() > 0);
        // assertTrue(statistics.getPrivateEntries() > 0);
        assertTrue(statistics.getPublicEntries() > 0);
        // assertTrue(statistics.getFriendsEntries() > 0);
    }

    @Test
    public void testGetUserStatistics() throws ServiceException {
        final UserStatistics statistics = statisticsService.getUserStatistics(TEST_USER);
        assertNotNull(statistics);
        assertEquals("testuser", statistics.getUsername());
        assertTrue(statistics.getEntryCount() > 0);
        assertTrue(statistics.getCommentCount() > 0);
    }

    @Test
    public void testGetUserStatisticsBadUser() throws ServiceException {
        final UserStatistics statistics = statisticsService.getUserStatistics("root");
        assertNull(statistics);
    }
}
