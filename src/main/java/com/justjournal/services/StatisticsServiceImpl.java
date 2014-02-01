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

import com.justjournal.db.*;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Provide Statistics Services for Just Journal
 *
 * @author Lucas Holt
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    private static final Logger log = Logger.getLogger(StatisticsServiceImpl.class);

    private CommentDao commentDao;
    private EntryDao entryDao;

    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public void setEntryDao(EntryDao entryDao) {
        this.entryDao = entryDao;
    }

    @Override
    @Nullable
    public UserStatistics getUserStatistics(@NotNull String username) throws ServiceException {
        if ((commentDao == null)) throw new AssertionError();
        if ((entryDao == null)) throw new AssertionError();

        try {
            UserStatistics userStatistics = new UserStatisticsImpl();

            // check if user is valid
            if (username == null || UserDao.get(username) == null) {
                log.trace("getUserStatistics(): username not found: " + username);
                return null;
            }

            userStatistics.setUsername(username);
            userStatistics.setEntryCount(entryDao.entryCount(username));
            userStatistics.setCommentCount(commentDao.count(username));

            return userStatistics;
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(e);
        }
    }

    @Override
    @NotNull
    public Statistics getStatistics() throws ServiceException {
        try {
            Statistics statistics = new StatisticsImpl();

            statistics.setComments(SQLHelper.count("comments"));
            statistics.setEntries(SQLHelper.count("entry"));
            statistics.setUsers(SQLHelper.count("user"));
            statistics.setTags(SQLHelper.count("tags"));
            statistics.setStyles(SQLHelper.count("style"));
            statistics.setPublicEntries(SQLHelper.scalarInt("SELECT count(*) FROM entry WHERE security='2';"));
            statistics.setPrivateEntries(SQLHelper.scalarInt("SELECT count(*) FROM entry WHERE security='0';"));
            statistics.setFriendsEntries(SQLHelper.scalarInt("SELECT count(*) FROM entry WHERE security='1';"));

            return statistics;
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(e);
        }
    }
}
