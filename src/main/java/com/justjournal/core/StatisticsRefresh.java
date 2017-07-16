package com.justjournal.core;

import com.justjournal.model.User;
import com.justjournal.repository.UserRepository;
import com.justjournal.services.EntryStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Lucas Holt
 */
@Slf4j
@Component
public class StatisticsRefresh {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryStatisticService entryStatisticService;

    // Every six hours?
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 6, initialDelay = 30000)
    public void run() {
        log.info("Statistics Refresh: init");

        try {
            for (final User user : userRepository.findAll()) {
                entryStatisticService.compute(user);
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("Statistics Refresh: Quit");
    }
}
