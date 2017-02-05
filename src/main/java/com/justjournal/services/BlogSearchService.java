package com.justjournal.services;

import com.justjournal.model.Entry;
import com.justjournal.model.Security;
import com.justjournal.model.search.BlogEntry;
import com.justjournal.model.search.Tag;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.SecurityDao;
import com.justjournal.repository.search.BlogEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Blog entry search services
 * @author Lucas Holt
 */
@Slf4j
@Service
public class BlogSearchService {

    private BlogEntryRepository blogEntryRepository;

    private EntryRepository entryRepository;

    private SecurityDao securityDao;

    @Autowired
    public BlogSearchService(final BlogEntryRepository blogEntryRepository, final EntryRepository entryRepository, final SecurityDao securityDao) {
        this.blogEntryRepository = blogEntryRepository;
        this.entryRepository = entryRepository;
        this.securityDao = securityDao;
    }

    /**
     * Find all blog entries mentioning a specific term
     * @param term search term
     * @param page page
     * @return a page of results
     */
    public Page<BlogEntry> search(final String term, final Pageable page) {
        return blogEntryRepository.findBySubjectContainsOrBodyContainsAllIgnoreCase(term, term, page);
    }

    /**
     * Find all blog entries for a specific user
     * @param term search term
     * @param username user to filter on
     * @param page page
     * @return a page of results
     */
    public Page<BlogEntry> search(final String term, final String username, final Pageable page) {
        final QueryBuilder qb =
                boolQuery()
                        .must(matchQuery("author", username))
                        .must(multiMatchQuery(
                                term,
                                "subject", "body"
                                )
                        );

        log.warn(qb.toString());

        return blogEntryRepository.search(qb, page);
    }

    /**
     * Index all public blog entries.
     */
    @Async
    public void indexAllPublicBlogEntries() {
        try {
            final Security sec = securityDao.findByName("public");
            Pageable pageable = new PageRequest(0, 100);

            Page<Entry> entries = entryRepository.findBySecurityOrderByDateDesc(sec, pageable);
            for (int i = 0; i < entries.getTotalPages(); i++) {
                ArrayList<BlogEntry> items = new ArrayList<BlogEntry>();
                
                for (final Entry entry : entries) {
                    items.add(convert(entry));
                }

                blogEntryRepository.save(items);

                pageable = new PageRequest(i + 1, 100);
                entries = entryRepository.findBySecurityOrderByDateDesc(sec, pageable);
            }
        } catch (final Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Index public blog entries since a specific date
     * @param date newer blog entries
     */
    @Async
    public void indexPublicBlogEntriesSince(final Date date) {
        final Security sec = securityDao.findByName("public");
        Pageable pageable = new PageRequest(0, 100);

        Page<Entry> entries = entryRepository.findBySecurityOrderByDateDesc(sec, pageable);
        for (int i = 0; i < entries.getTotalPages(); i++) {
            ArrayList<BlogEntry> items = new ArrayList<BlogEntry>();
            for (final Entry entry : entries) {
                if (entry.getDate().before(date)) {
                    blogEntryRepository.save(items);
                    // stop processing items.
                    return;
                }

                items.add(convert(entry));
            }

            blogEntryRepository.save(items);

            pageable = new PageRequest(i + 1, 100);
            entries = entryRepository.findBySecurityOrderByDateDesc(sec, pageable);
        }
    }

    /**
     * Convert an entry into a blog entry (search indexed document)
     *
     * @param entry entry domain object
     * @return blog entry for ES
     */
    public BlogEntry convert(final Entry entry) {
        final BlogEntry blogEntry = new BlogEntry();
        blogEntry.setAuthor(entry.getUser().getUsername());
        blogEntry.setId(entry.getId());
        blogEntry.setPrivateEntry(!entry.getSecurity().getName().equalsIgnoreCase("public"));
        blogEntry.setSubject(entry.getSubject());
        blogEntry.setBody(entry.getBody());

        final HashMap<String, Object> tags = new HashMap<String, Object>();
        for (final com.justjournal.model.EntryTag tag : entry.getTags()) {
            final String tagName = tag.getTag().getName();
            if (!tags.containsKey(tagName))
                tags.put(tagName, null);
        }

        final List<Tag> targetList = new ArrayList<Tag>();
        for (final String t : tags.keySet()) {
            final Tag tag = new Tag();
            tag.setName(t);
            targetList.add(tag);
        }
        blogEntry.setTags(targetList);

        return blogEntry;
    }
}
