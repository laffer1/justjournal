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
    private CommentRepository commentDao;
    @Autowired
    private EntryRepository entryDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityDao securityDao;

    /**
     * Get the recent blog entries list for the sidebar, but only use public entries.
     *
     * @param user blog user
     * @return subject & entry id data
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    public List<RecentEntry> getRecentEntriesPublic(User user) {
        Page<Entry> entries;
        List<RecentEntry> recentEntries = new ArrayList<RecentEntry>();

        try {
            Pageable page = new PageRequest(0, MAX_RECENT_ENTRIES);
            entries = entryDao.findByUserAndSecurityOrderByDateDesc(user, securityDao.findOne(2), page);

            for (Entry o : entries) {
                RecentEntry recentEntry = new RecentEntry();
                recentEntry.setId(o.getId());
                recentEntry.setSubject(Xml.cleanString(o.getSubject()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return recentEntries;
    }

    /**
     * Get the recent blog entries list for the sidebar.
     *
     * @param user blog user
     * @return subject & entry id data
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    public List<RecentEntry> getRecentEntries(User user) {
        Page<Entry> entries;
        List<RecentEntry> recentEntries = new ArrayList<RecentEntry>();


        try {
            Pageable page = new PageRequest(0, MAX_RECENT_ENTRIES);
            entries = entryDao.findByUserOrderByDateDesc(user, page);

            for (Entry o : entries) {
                RecentEntry recentEntry = new RecentEntry();
                recentEntry.setId(o.getId());
                recentEntry.setSubject(Xml.cleanString(o.getSubject()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return recentEntries;
    }

    @Transactional(value = Transactional.TxType.REQUIRED)

    public Entry getPublicEntry(int id, String username) throws ServiceException {
        try {
            Entry entry = entryDao.findOne(id);
            if (entry.getUser().getUsername().equalsIgnoreCase(username) && entry.getSecurity().getId() == 2) // public
                return entry;
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Transactional(value = Transactional.TxType.REQUIRED)

    public List<Entry> getPublicEntries(String username) throws ServiceException {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return null;
            }
            return entryDao.findByUserAndSecurityOrderByDateDesc(user, securityDao.findOne(2));
        } catch (Exception e) {
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
    @Transactional(value = Transactional.TxType.REQUIRED)
    public List<Entry> getFriendsEntries(String username) throws ServiceException {
        try {
            User user = userRepository.findByUsername(username);
            List<Friend> friends = user.getFriends();

            List<Entry> list = new ArrayList<Entry>();

            for (Friend friend : friends) {
                // TODO: limit record count
                Collection<Entry> fe = friend.getFriend().getEntries();
                for (Entry entry : fe) {
                    if (entry.getSecurity().getId() == 2)
                        list.add(entry);
                }
            }

            Collections.sort(list, new Comparator<Entry>() {
                public int compare(Entry m1, Entry m2) {
                    return m1.getDate().compareTo(m2.getDate());
                }
            });

            if (list.isEmpty()) return list;

            int end = list.size() - 1;
            int start = 0;
            if (end > 20)
                start = end - 20;

            return list.subList(start, end);
        } catch (Exception e) {
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
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Collection<Tag> getEntryTags(String username) throws ServiceException {
        try {
            assert (entryDao != null);
            assert (username != null);

            Map<String, Tag> tags = new HashMap<String, Tag>();

            // TODO: insanely slow. Refactor
            List<Entry> entries = entryDao.findByUsername(username);
            for (Entry entry : entries) {
                for (EntryTag entryTag : entry.getTags())
                    if (!tags.containsKey(entryTag.getTag().getName()))
                        tags.put(entryTag.getTag().getName(), entryTag.getTag());
            }

            return tags.values();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }

}
