package com.justjournal.core;

import com.justjournal.model.Entry;
import com.justjournal.model.Security;
import com.justjournal.model.search.BlogEntry;
import com.justjournal.model.search.Tag;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.SecurityDao;
import com.justjournal.repository.search.BlogEntryRepository;
import com.justjournal.services.BlogSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Manage indexing blog entries into elasticsearch
 *
 * @author Lucas Holt
 */
@Slf4j
@Component
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
