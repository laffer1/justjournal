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

import com.justjournal.repository.CommentDao;
import com.justjournal.repository.EntryRepository;
import com.justjournal.model.EntryTo;
import com.justjournal.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Lucas Holt
 */
@Service
public class EntryServiceImpl implements EntryService {

    private static final Logger log = Logger.getLogger(EntryServiceImpl.class);

    private CommentDao commentDao;
    private EntryRepository entryDao;
    private UserRepository userDao;

    /**
     * Get Friend public blog entries. TODO: Eventually, we'll want security and performance taken into account.
     *
     * @param username
     * @return
     */
    @Override
    public List<EntryTo> getFriendsEntries(String username) {
        Collection<String> friends = userDao.friends(username);

        List<EntryTo> list = new ArrayList<EntryTo>();

        for (String friend : friends) {
            Collection<EntryTo> fe = entryDao.view(friend, false);
            list.addAll(fe);
        }

        Collections.sort(list, new Comparator<EntryTo>() {
            public int compare(EntryTo m1, EntryTo m2) {
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

    @Override
    public void setCommentDao(CommentDao commentDao) {
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
