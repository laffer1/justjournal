package com.justjournal.services;

import com.justjournal.model.EntryStatistic;
import com.justjournal.model.User;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.EntryStatisticRepository;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;

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
    public EntryStatisticService(final EntryStatisticRepository entryStatisticRepository, final EntryRepository entryRepository) {
        this.entryStatisticRepository = entryStatisticRepository;
        this.entryRepository = entryRepository;
    }


    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public Maybe<EntryStatistic> getEntryCount(final String username, final int year) {
        log.trace("Fetching entry count for " + username + " and year " + year);

        return Maybe.just(entryStatisticRepository.findByUsernameAndYear(username, year));
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public Observable<EntryStatistic> getEntryCounts(final String username) {
        return Observable.fromIterable(entryStatisticRepository.findByUsernameOrderByYearDesc(username));
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public io.reactivex.Observable<EntryStatistic> compute(final User user) {
        final GregorianCalendar calendarg = new GregorianCalendar();
        final int yearNow = calendarg.get(Calendar.YEAR);

        return compute(user, user.getSince(), yearNow);
    }


    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public io.reactivex.Observable<EntryStatistic> compute(final User user, final int startYear, final int endYear) {
        final GregorianCalendar calendarg = new GregorianCalendar();

        return io.reactivex.Observable.range(startYear, endYear - startYear + 1)
                .flatMap(new Function<Integer, ObservableSource<EntryStatistic>>() {
                    @Override
                    public ObservableSource<EntryStatistic> apply(final Integer yr) throws Exception {
                        return io.reactivex.Observable.fromCallable(new Callable<EntryStatistic>() {
                            @Override
                            public EntryStatistic call() throws Exception {

                                EntryStatistic es = entryStatisticRepository.findByUserAndYear(user, yr);
                                final long count = entryRepository.calendarCount(yr, user.getUsername());

                                if (es == null) {
                                    es = new EntryStatistic();
                                    es.setUser(user);
                                }

                                es.setCount(count);
                                es.setYear(yr);
                                es.setModified(calendarg.getTime());

                                return entryStatisticRepository.saveAndFlush(es);

                            }
                        });
                    }
                }).subscribeOn(Schedulers.io());
    }
}
