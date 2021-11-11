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
package com.justjournal.core;


import com.justjournal.services.BlogSearchService;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Manage indexing blog entries into elasticsearch
 *
 * @author Lucas Holt
 */
@Log4j2
@Component
@Profile("!test")
public class SearchIndexer {
  private final BlogSearchService blogSearchService;

  @Autowired
  public SearchIndexer(final BlogSearchService blogSearchService) {
    this.blogSearchService = blogSearchService;
  }

  /** Periodically load new blog entries */
  @Scheduled(fixedDelay = 1000 * 60 * 30, initialDelay = 120000)
  public void loadNewEntries() {
    log.info("Search indexer - Load new entries from the last 30 minutes");

    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, -30);

    blogSearchService.indexPublicBlogEntriesSince(cal.getTime());
  }

  /** Load all public blog entries at startup into elasticsearch */
  @PostConstruct
  public void initialize() {
    log.info("Starting search indexer - Load all public entries");

    blogSearchService.indexAllPublicBlogEntries();
  }
}
