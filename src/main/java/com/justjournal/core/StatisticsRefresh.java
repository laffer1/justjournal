package com.justjournal.core;

import com.justjournal.model.User;
import com.justjournal.repository.UserRepository;
import com.justjournal.services.EntryStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Lucas Holt
 */
@Slf4j
@Component
@Profile("!test")
public class StatisticsRefresh {

    private final UserRepository userRepository;

    private final EntryStatisticService entryStatisticService;

    @Autowired
    public StatisticsRefresh(final UserRepository userRepository, final EntryStatisticService entryStatisticService) {
        this.userRepository = userRepository;
        this.entryStatisticService = entryStatisticService;
    }

    // Every six hours?
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 6, initialDelay = 30000)
    public void run() {
        log.info("Statistics Refresh: init");

        try {
            for (final User user : userRepository.findAll()) {
                entryStatisticService.compute(user).blockingSubscribe();
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("Statistics Refresh: Quit");
    }
}
