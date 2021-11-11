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


import com.justjournal.model.EntryStatistic;
import com.justjournal.model.User;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.EntryStatisticRepository;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Compute yearly entry statistics
 *
 * @author Lucas Holt
 */
@Service
@Log4j2
public class EntryStatisticService {

  private final EntryStatisticRepository entryStatisticRepository;

  private final EntryRepository entryRepository;

  @Autowired
  public EntryStatisticService(
      final EntryStatisticRepository entryStatisticRepository,
      final EntryRepository entryRepository) {
    this.entryStatisticRepository = entryStatisticRepository;
    this.entryRepository = entryRepository;
  }

  @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
  public Mono<EntryStatistic> getEntryCount(final String username, final int year) {
    log.trace("Fetching entry count for " + username + " and year " + year);

    return Mono.justOrEmpty(entryStatisticRepository.findByUsernameAndYear(username, year));
  }

  @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
  public Flux<EntryStatistic> getEntryCounts(final String username) {
    return Flux.fromIterable(entryStatisticRepository.findByUsernameOrderByYearDesc(username));
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Flux<EntryStatistic> compute(final User user) {
    final GregorianCalendar calendarg = new GregorianCalendar();
    final int yearNow = calendarg.get(Calendar.YEAR);

    return compute(user, user.getSince(), yearNow);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Flux<EntryStatistic> compute(final User user, final int startYear, final int endYear) {
    final GregorianCalendar calendarg = new GregorianCalendar();

    if (endYear < startYear) throw new IllegalArgumentException("endYear");
    if (endYear < 2003) throw new IllegalArgumentException("endYear");
    if (startYear < 2003) throw new IllegalArgumentException("startYear");

    return Flux.range(startYear, endYear - startYear + 1)
        .flatMap(
            (Function<Integer, Mono<EntryStatistic>>)
                yr ->
                    Mono.fromCallable(
                        () -> {
                          log.debug("testing with year: " + yr + " user: " + user.getUsername());
                          EntryStatistic es = entryStatisticRepository.findByUserAndYear(user, yr);
                          final long count = entryRepository.calendarCount(yr, user.getUsername());

                          if (es == null) {
                            log.trace("Creating new entry statistic");
                            es = new EntryStatistic();
                            es.setUser(user);
                          }

                          es.setCount(count);
                          es.setYear(yr);
                          es.setModified(calendarg.getTime());

                          log.trace("save and flush time");
                          return entryStatisticRepository.saveAndFlush(es);
                        }))
        .subscribeOn(Schedulers.boundedElastic());
  }
}
