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
import com.justjournal.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * @author Lucas Holt
 */
@Service
public class EntryServiceImpl implements EntryService {

    private static final Logger log = Logger.getLogger(EntryServiceImpl.class);

    private CommentRepository commentDao;
    private EntryRepository entryDao;
    private UserRepository userDao;

    /**
     * Get Friend public blog entries. TODO: Eventually, we'll want security and performance taken into account.
     *
     * @param username
     * @return
     */
    @Override
    public List<Entry> getFriendsEntries(String username) {
        User user = userDao.findByUsername(username);
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
    }

    public Collection<Tag> getEntryTags(String username) {
        HashMap<Object, Serializable> s = new HashMap<Object, Serializable>();

        Map<String, Tag> tags = new HashMap<String, Tag>();

        // TODO: insanely slow. Refactor
        List<Entry> entries = entryDao.findByUsername(username);
        for (Entry entry : entries) {
            for (EntryTag entryTag : entry.getTags())
                if (!tags.containsKey(entryTag.getTag().getName()))
                    tags.put(entryTag.getTag().getName(), entryTag.getTag());
        }

        return tags.values();
    }

    @Override
    public void setCommentDao(CommentRepository commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public void setEntryDao(EntryRepository entryDao) {
        this.entryDao = entryDao;
    }

    @Override
    public void setUserDao(final UserRepository userDao) {
        this.userDao = userDao;
    }
}
