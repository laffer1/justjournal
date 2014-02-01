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

package com.justjournal.db;

import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Entry Data Access
 *
 * @author Lucas Holt
 */
@Component
public interface EntryDao {
    public EntryTo viewSingle(final EntryTo ets);

    public EntryTo viewSinglePublic(final int entryId);

    public boolean exists(final int entryId);

    public EntryTo viewSingle(final int entryId);

    public List<EntryTo> view(final String userName, final boolean thisUser);

    public List<EntryTo> view(final String userName, final boolean thisUser, final int skip);

    public List<EntryTo> viewAll(final String userName, final boolean thisUser);

    public List<EntryTo> viewFriends(final int userID, final int aUserId);

    public int calendarCount(final int year, final String userName)
            throws Exception;

    @NotNull
    public int entryCount(final String userName) throws Exception;

    public Collection<EntryTo> viewCalendarYear(final int year,
                                                final String userName,
                                                final boolean thisUser)
            throws Exception;

    @NotNull
    public Collection<EntryTo> viewCalendarMonth(final int year,
                                                 final int month,
                                                 final String userName,
                                                 final boolean thisUser)
            throws Exception;

    @NotNull
    public Collection<EntryTo> viewCalendarDay(final int year,
                                               final int month,
                                               final int day,
                                               final String userName,
                                               final boolean thisUser);

    @NotNull
    public Collection<EntryTo> getEntriesByDateRange(Date startDate, Date endDate,
                                                     final String userName,
                                                     final boolean thisUser);

    public
    @NotNull
    Collection<EntryTo> viewRecentUniqueUsers();

    public
    @NotNull
    ArrayList<String> getTags(int entryId);

    public boolean setTags(int entryId, Iterable tags);

    public int getTagId(String tagname);

    public
    @NotNull
    ArrayList<Tag> getUserTags(int userId);

    @NotNull
    public EntryTo viewSingle(final int entryId, final int userId);
}
