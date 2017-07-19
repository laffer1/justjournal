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

import com.justjournal.model.*;
import com.justjournal.repository.*;
import com.justjournal.utility.Xml;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class EntryService {
    private static final int MAX_RECENT_ENTRIES = 5;

    @Autowired
    private EntryRepository entryDao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityRepository securityDao;

    @Autowired
    private TagRepository tagDao;

    @Autowired
    private EntryTagsRepository entryTagsRepository;
    
    private io.reactivex.Observable<RecentEntry> getRecentEntryObservable(Page<Entry> entries) {
        return io.reactivex.Observable.fromIterable(entries)
                .observeOn(Schedulers.computation())
                .map(new Function<Entry, RecentEntry>() {

                    @Override
                    public RecentEntry apply(final Entry o) throws Exception {
                        final RecentEntry recentEntry = new RecentEntry();
                        recentEntry.setId(o.getId());
                        recentEntry.setSubject(Xml.cleanString(o.getSubject()));
                        return recentEntry;
                    }
                });
    }

    /**
     * Get the recent blog entries list for the sidebar, but only use public entries.
     *
     * @param username blog user
     * @return subject & entry id data
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public io.reactivex.Observable<RecentEntry> getRecentEntriesPublic(final String username) throws ServiceException {
        try {
            final Pageable page = new PageRequest(0, MAX_RECENT_ENTRIES, new Sort(Sort.Direction.DESC, "date", "id"));
            final Page<Entry> entries = entryDao.findByUserAndSecurityAndDraftWithSubjectOnly(username, securityDao.findByName("public"), PrefBool.N, page);
            return getRecentEntryObservable(entries);
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Unable to retrieve recent blog entries for " + username, e);
        }
    }

    /**
     * Get the recent blog entries list for the sidebar.
     *
     * @param username blog username
     * @return subject & entry id data
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public io.reactivex.Observable<RecentEntry> getRecentEntries(final String username) throws ServiceException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("username not found in getRecentEntrieswith %s", username);
            return null;
        }

        try {
            final Pageable page = new PageRequest(0, MAX_RECENT_ENTRIES, Sort.Direction.DESC, "date", "id");
            final Page<Entry> entries = entryDao.findByUserOrderByDateDesc(user, page);
            return getRecentEntryObservable(entries);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Unable to retrieve recent blog entries for " + username);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public Entry getPublicEntry(final int id, final String username) throws ServiceException {
        try {
            final Entry entry = entryDao.findOne(id);
            if (entry.getUser().getUsername().equalsIgnoreCase(username) && entry.getSecurity().getId() == 2 && entry.getDraft().equals(PrefBool.N)) // public
                return entry;
            return null;
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
    public List<Entry> getPublicEntries(final String username) throws ServiceException {
        try {
            final User user = userRepository.findByUsername(username);
            if (user == null) {
                return Collections.emptyList();
            }
            return entryDao.findByUserAndSecurityAndDraftOrderByDateDesc(user, securityDao.findOne(2), PrefBool.N);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
    public Page<Entry> getPublicEntries(final String username, final Pageable pageable) throws ServiceException {
        try {
            final User user = userRepository.findByUsername(username);
            if (user == null) {
                return null;
            }
            return entryDao.findByUserAndSecurityAndDraft(user, securityDao.findOne(2), PrefBool.N, pageable);
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public Page<Entry> getEntries(final String username, final Pageable pageable) throws ServiceException {
        try {
            final User user = userRepository.findByUsername(username);
            if (user == null) {
                return null;
            }
            return entryDao.findByUserOrderByDateDesc(user, pageable);
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Get Friend public blog entries. TODO: Eventually, we'll want security and performance taken into account.
     *
     * @param username
     * @return
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public List<Entry> getFriendsEntries(final String username) throws ServiceException {
        try {
            final Pageable page = new PageRequest(0, 20, Sort.Direction.DESC, "date", "id");
            final Page<Entry> fe = entryDao.findByUserFriends(username, PrefBool.N, page);

           return fe.getContent();
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Get all the tags used on entries by a particular user
     *
     * @param username
     * @return
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public io.reactivex.Observable<Tag> getEntryTags(final String username) throws ServiceException {
        try {
            assert entryDao != null;
            assert username != null;

            final Map<String, Tag> tags = new HashMap<String, Tag>();

            final List<Tag> tagList = tagDao.findByUsername(username);
            for (final Tag t : tagList) {
                if (!tags.containsKey(t.getName())) {
                    t.setCount(1);
                    tags.put(t.getName(), t);
                } else {
                    final Tag tag = tags.get(t.getName());
                    tag.setCount(t.getCount() + 1);
                    tags.put(t.getName(), tag);
                }
            }

            return io.reactivex.Observable.fromIterable(tags.values());
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }


    /**
     * Add and remove tags on an entry from a list.
     *
     * @param entry entry to modify
     * @param tags  tags that should be present (removes anything not listed)
     */
    public void applyTags(final Entry entry, final Set<String> tags) {
        final List<EntryTag> entryTags = entryTagsRepository.findByEntry(entry);

        // find tags that are missing in the db and add them.
        for (final String tag : tags) {
            boolean found = false;
            for (final EntryTag entryTag : entryTags) {
                if (entryTag.getTag().getName().equalsIgnoreCase(tag)) {
                    // in list
                    found = true;
                    break;
                }
            }

            if (!found) {
                addTagToEntry(entry, tag);
            }
        }

        for (final EntryTag entryTag : entryTags) {
            boolean found = false;

            for (final String tag : tags) {
                if (tag.equalsIgnoreCase(entryTag.getTag().getName())) {
                    found = true;
                    break;
                }
            }

            if (!found)
                removeTagFromEntry(entry, entryTag.getTag().getName());
        }
    }

    /**
     * Add a tag to an entry
     *
     * @param entry entry to add tags to
     * @param tag   tag name
     */
    public void addTagToEntry(final Entry entry, final String tag) {
        Tag t = tagDao.findByName(tag);
        if (t == null)
            t = tagDao.save(new Tag(tag));

        final EntryTag ets = entryTagsRepository.findByEntryAndTag(entry, t);
        if (ets == null) {
            final EntryTag et = new EntryTag();
            et.setTag(t);
            et.setEntry(entry);
            entryTagsRepository.save(et);
        }
    }

    /**
     * Remove tag from entry
     *
     * @param entry entry to modify
     * @param tag   tag name
     */
    public void removeTagFromEntry(final Entry entry, final String tag) {
        final Tag t = tagDao.findByName(tag);
        if (t == null)
            return;

        final EntryTag ets = entryTagsRepository.findByEntryAndTag(entry, t);
        if (ets != null) {

            entryTagsRepository.delete(ets);
        }
    }
}
