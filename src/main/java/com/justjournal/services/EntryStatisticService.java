package com.justjournal.services;

import com.justjournal.model.EntryStatistic;
import com.justjournal.model.User;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.EntryStatisticRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.Function;

/**
 * Compute yearly entry statistics
 *
 * @author Lucas Holt
 */
@Service
@Slf4j
public class EntryStatisticService {

    private final EntryStatisticRepository entryStatisticRepository;

    private final EntryRepository entryRepository;

    @Autowired
    public EntryStatisticService(final EntryStatisticRepository entryStatisticRepository,
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

        if (endYear < startYear)
            throw new IllegalArgumentException("endYear");
        if (endYear < 2003)
            throw new IllegalArgumentException("endYear");
        if (startYear < 2003)
            throw new IllegalArgumentException("startYear");

        return Flux.range(startYear, endYear - startYear + 1)
                .flatMap((Function<Integer, Mono<EntryStatistic>>) yr -> Mono.fromCallable(() -> {

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

                })).subscribeOn(Schedulers.boundedElastic());
    }

}
