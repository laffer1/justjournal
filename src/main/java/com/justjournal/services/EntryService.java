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
import com.justjournal.repository.CommentRepository;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.SecurityDao;
import com.justjournal.repository.UserRepository;
import com.justjournal.utility.Xml;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author Lucas Holt
 */
@Service
public class EntryService {
    private static final int MAX_RECENT_ENTRIES = 5;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(EntryService.class);

    @Autowired
    private EntryRepository entryDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityDao securityDao;

    /**
     * Get the recent blog entries list for the sidebar, but only use public entries.
     *
     * @param username blog user
     * @return subject & entry id data
     */
    @Transactional(value = Transactional.TxType.SUPPORTS)
    public List<RecentEntry> getRecentEntriesPublic(final String username) throws ServiceException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("username not found in getRecentEntriesPublic with " + username);
            return null;
        }
        final Page<Entry> entries;
        final List<RecentEntry> recentEntries = new ArrayList<RecentEntry>(MAX_RECENT_ENTRIES);

        try {
            final Pageable page = new PageRequest(0, MAX_RECENT_ENTRIES, new Sort(
                    new Sort.Order(Sort.Direction.DESC, "date")
            ));
            entries = entryDao.findByUserAndSecurityAndDraft(user, securityDao.findOne(2), PrefBool.N, page);

            for (final Entry o : entries) {
                final RecentEntry recentEntry = new RecentEntry();
                recentEntry.setId(o.getId());
                recentEntry.setSubject(Xml.cleanString(o.getSubject()));
                recentEntries.add(recentEntry);
            }
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Unable to retrieve recent blog entries for " + username);
        }

        return recentEntries;
    }

    /**
     * Get the recent blog entries list for the sidebar.
     *
     * @param username blog username
     * @return subject & entry id data
     */
    @Transactional(value = Transactional.TxType.SUPPORTS)
    public List<RecentEntry> getRecentEntries(final String username) throws ServiceException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }

        final Page<Entry> entries;
        final List<RecentEntry> recentEntries = new ArrayList<RecentEntry>();

        try {
            final Pageable page = new PageRequest(0, MAX_RECENT_ENTRIES, new Sort(
                                new Sort.Order(Sort.Direction.DESC, "date")
                        ));
            entries = entryDao.findByUserOrderByDateDesc(user, page);

            for (final Entry o : entries) {
                final RecentEntry recentEntry = new RecentEntry();
                recentEntry.setId(o.getId());
                recentEntry.setSubject(Xml.cleanString(o.getSubject()));
                recentEntries.add(recentEntry);
            }
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Unable to retrieve recent blog entries for " + username);
        }

        return recentEntries;
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
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
                return null;
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

    @Transactional(value = Transactional.TxType.SUPPORTS)
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
    @Transactional(value = Transactional.TxType.SUPPORTS)
    public List<Entry> getFriendsEntries(final String username) throws ServiceException {
        try {
            final User user = userRepository.findByUsername(username);
            final List<Friend> friends = user.getFriends();

            final List<Entry> list = new ArrayList<Entry>();

            for (final Friend friend : friends) {
                // TODO: limit record count
                final Collection<Entry> fe = friend.getFriend().getEntries();
                for (final Entry entry : fe) {
                    if (entry.getSecurity().getId() == 2 && entry.getDraft().equals(PrefBool.N))
                        list.add(entry);
                }
            }

            Collections.sort(list, new Comparator<Entry>() {
                public int compare(Entry m1, Entry m2) {
                    return m1.getDate().compareTo(m2.getDate());
                }
            });

            if (list.isEmpty()) return list;

            final int end = list.size() - 1;
            int start = 0;
            if (end > 20)
                start = end - 20;

            return list.subList(start, end);
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
    @Transactional(value = Transactional.TxType.SUPPORTS)
    public Collection<Tag> getEntryTags(final String username) throws ServiceException {
        try {
            assert (entryDao != null);
            assert (username != null);

            final Map<String, Tag> tags = new HashMap<String, Tag>();

            // TODO: insanely slow. Refactor
            final List<Entry> entries = entryDao.findByUsername(username);
            for (final Entry entry : entries) {
                for (final EntryTag entryTag : entry.getTags())
                    if (!tags.containsKey(entryTag.getTag().getName()))
                        tags.put(entryTag.getTag().getName(), entryTag.getTag());
            }

            return tags.values();
        } catch (final Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

}
