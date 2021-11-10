/*
 * Copyright (c) 2003-2021 Lucas Holt
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.justjournal.model.EntryStatistic;
import com.justjournal.model.User;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.EntryStatisticRepository;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** @author Lucas Holt */
@RunWith(MockitoJUnitRunner.class)
public class EntryStatisticsServiceTests {

  private static final String TEST_USER = "testuser";
  private static final int TEST_YEAR = 2003;

  @Mock private EntryStatisticRepository entryStatisticRepository;

  @Mock private EntryRepository entryRepository;

  @InjectMocks private EntryStatisticService entryStatisticService;

  private EntryStatistic entryStatistic;
  private User user;

  @Before
  public void setupMock() {
    user = new User();
    user.setUsername(TEST_USER);

    entryStatistic = new EntryStatistic();
    entryStatistic.setUser(user);
    entryStatistic.setCount(1L);
    entryStatistic.setYear(TEST_YEAR);
  }

  @Test
  public void getEntryCounts() {
    when(entryStatisticRepository.findByUsernameOrderByYearDesc(TEST_USER))
        .thenReturn(Collections.singletonList(entryStatistic));
    Flux<EntryStatistic> o = entryStatisticService.getEntryCounts(TEST_USER);
    final Iterable<EntryStatistic> myIterator = o.toIterable();

    assertNotNull(myIterator);

    Iterator<EntryStatistic> iterator = myIterator.iterator();
    assertTrue(iterator.hasNext());

    EntryStatistic es = iterator.next();

    verify(entryStatisticRepository, atLeastOnce()).findByUsernameOrderByYearDesc(TEST_USER);

    assertEquals(TEST_USER, es.getUser().getUsername());
    assertEquals(TEST_YEAR, es.getYear());
    assertEquals(1L, es.getCount());
  }

  @Test
  public void getEntryCount() {
    when(entryStatisticRepository.findByUsernameAndYear(TEST_USER, TEST_YEAR))
        .thenReturn(entryStatistic);

    Mono<EntryStatistic> o = entryStatisticService.getEntryCount(TEST_USER, TEST_YEAR);

    EntryStatistic es = o.block();
    verify(entryStatisticRepository, atLeastOnce()).findByUsernameAndYear(TEST_USER, TEST_YEAR);

    assertEquals(TEST_USER, es.getUser().getUsername());
    assertEquals(TEST_YEAR, es.getYear());
    assertEquals(1L, es.getCount());
  }

  @Test(expected = IllegalArgumentException.class)
  public void computeBadStartYear() {
    entryStatisticService.compute(user, 0, TEST_YEAR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void computeBadEndYear() {
    entryStatisticService.compute(user, TEST_YEAR, 0);
  }

  /*   TODO: not working due to rxjava scheduler io multithreading.
  @Test
  public void computeSingle() {
      when(entryStatisticRepository.findByUserAndYear(user, TEST_YEAR + 1)).thenReturn(entryStatistic);
      when(user.getUsername()).thenReturn(TEST_USER);
      when(entryRepository.calendarCount(TEST_YEAR, TEST_USER)).thenReturn(1L);
      when(entryStatistic.getUser()).thenReturn(user);
      when(entryStatistic.getCount()).thenReturn(1L);
      when(entryStatistic.getYear()).thenReturn(TEST_YEAR);
      when(entryStatisticRepository.saveAndFlush(entryStatistic)).thenReturn(entryStatistic);

      TestObserver<EntryStatistic> testObserver = entryStatisticService.compute(user, TEST_YEAR, TEST_YEAR )
              .test().awaitDone(5, TimeUnit.SECONDS);

    //  testObserver.awaitTerminalEvent();
      verify(entryStatisticRepository, atLeastOnce()).findByUserAndYear(user, TEST_YEAR);
        verify(entryRepository, atLeastOnce()).calendarCount(TEST_YEAR, TEST_USER);

      testObserver
          .assertNoErrors()
          .assertValue(new Predicate<EntryStatistic>() {
              @Override
              public boolean test(final EntryStatistic entryStatistic) throws Exception {
                  return entryStatistic.getCount() == 1L ;
              }
          });
  }
           */

  // @Rule
  //  public TrampolineSchedulerRule rule = new TrampolineSchedulerRule();
}
