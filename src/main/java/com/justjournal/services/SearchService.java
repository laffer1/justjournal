/*
 * Copyright (c) 2014 Lucas Holt
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

import com.justjournal.model.Entry;
import com.justjournal.repository.EntryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Holt
 */
@Service
public class SearchService {
    private static final Logger log = Logger.getLogger(SearchService.class);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Qualifier("entryRepository")
    @Autowired
    private EntryRepository entryRepository;

    public void indexEntries() {
        log.trace("Indexing Entries for Elasticsearch");

        Page<Entry> entries;
        Pageable pageable = new PageRequest(0, 100);

        entries = entryRepository.findAll(pageable);

        for (int i = 0; i < entries.getTotalPages(); i++) {
            log.trace("Loading page " + i);
            List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();

            for (Entry entry : entries) {
                log.trace("Adding entry to index " + entry.getId());
                IndexQuery indexQuery1 = new IndexQueryBuilder().withId(Integer.toString(entry.getId())).withObject(entry).build();
                indexQueries.add(indexQuery1);
            }

            elasticsearchTemplate.bulkIndex(indexQueries);

            pageable = entries.nextPageable();
            entries = entryRepository.findAll(pageable);
        }
    }

    public void indexEntry(Entry entry) {
        if (entry == null) {
            log.warn("Empty entry passed to SearchService indexEntry()");
            throw new IllegalArgumentException("entry");
        }

        log.trace("Elasticsearch: Adding entry to index " + entry.getId());

        IndexQuery indexQuery = new IndexQueryBuilder().withId(Integer.toString(entry.getId())).withObject(entry).build();
        elasticsearchTemplate.index(indexQuery);
    }
}
