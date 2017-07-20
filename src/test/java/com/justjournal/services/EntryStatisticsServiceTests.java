package com.justjournal.services;

import com.justjournal.ApplicationTest;
import com.justjournal.model.EntryStatistic;
import com.justjournal.model.User;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.EntryStatisticRepository;
import com.justjournal.repository.UserRepository;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Lucas Holt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
public class EntryStatisticsServiceTests {

    private static final String TEST_USER = "testuser";
    private static final int TEST_YEAR = 2003;

    private EntryStatisticService entryStatisticService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntryStatisticRepository entryStatisticRepository;

    @Mock
    private EntryRepository entryRepository;

    @Mock
    private EntryStatistic entryStatistic;

    @Mock
    private User user;


    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        entryStatisticService = new EntryStatisticService(entryStatisticRepository, entryRepository);
    }

    @Test
    public void testGetEntryCounts() {
        when(entryStatisticRepository.findByUsernameOrderByYearDesc(TEST_USER)).thenReturn(Collections.singletonList(entryStatistic));
        when(entryStatistic.getUser()).thenReturn(user);
        when(entryStatistic.getCount()).thenReturn(1L);
        when(entryStatistic.getYear()).thenReturn(TEST_YEAR);
        when(user.getUsername()).thenReturn(TEST_USER);

        Observable<EntryStatistic> o = entryStatisticService.getEntryCounts(TEST_USER);
        final Iterable<EntryStatistic> myIterator = o.blockingIterable();
        verify(entryStatisticRepository, atLeastOnce()).findByUsernameOrderByYearDesc(TEST_USER);

        assertNotNull(myIterator);

        Iterator<EntryStatistic> iterator = myIterator.iterator();
        assertTrue(iterator.hasNext());

        EntryStatistic es = iterator.next();

        assertEquals(TEST_USER, es.getUser().getUsername());
        assertEquals(TEST_YEAR, es.getYear());
        assertEquals(1L, es.getCount());
    }


    @Test
    public void testGetEntryCount() {
        when(entryStatisticRepository.findByUsernameAndYear(TEST_USER, TEST_YEAR)).thenReturn(entryStatistic);
        when(entryStatistic.getUser()).thenReturn(user);
        when(entryStatistic.getCount()).thenReturn(1L);
        when(entryStatistic.getYear()).thenReturn(TEST_YEAR);
        when(user.getUsername()).thenReturn(TEST_USER);

        Maybe<EntryStatistic> o = entryStatisticService.getEntryCount(TEST_USER, TEST_YEAR);

        EntryStatistic es = o.blockingGet();
        verify(entryStatisticRepository, atLeastOnce()).findByUsernameAndYear(TEST_USER, TEST_YEAR);

        assertEquals(TEST_USER, es.getUser().getUsername());
        assertEquals(TEST_YEAR, es.getYear());
        assertEquals(1L, es.getCount());
    }

}
