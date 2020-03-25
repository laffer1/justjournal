package com.justjournal.core;

import com.justjournal.services.BlogSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;

/**
 * Manage indexing blog entries into elasticsearch
 *
 * @author Lucas Holt
 */
@Slf4j
@Component
@Profile("!test")
public class SearchIndexer {
    private final BlogSearchService blogSearchService;

    @Autowired
    public SearchIndexer(final BlogSearchService blogSearchService) {
        this.blogSearchService = blogSearchService;
    }

    /**
     * Periodically load new blog entries
     */
    @Scheduled(fixedDelay = 1000 * 60 * 30, initialDelay = 120000)
    public void loadNewEntries() {
        log.info("Search indexer - Load new entries from the last 30 minutes");

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -30);

        blogSearchService.indexPublicBlogEntriesSince(cal.getTime());
    }

    /**
     * Load all public blog entries at startup into elasticsearch
     */
    @PostConstruct
    public void initialize() {
        log.info("Starting search indexer - Load all public entries");

        blogSearchService.indexAllPublicBlogEntries();
    }

}
