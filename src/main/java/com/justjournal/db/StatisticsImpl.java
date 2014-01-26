/*
 * Copyright (c) 2006, 2008, 2011 Lucas Holt
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

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.stereotype.Component;

/**
 * Track the number of users, entry and comment statistics, and other information.
 *
 * @author Lucas Holt
 */
@Component
final public class StatisticsImpl implements Statistics {

    private int users = 0;
    private int entries = 0;
    private int publicEntries = 0;
    private int friendsEntries = 0;
    private int privateEntries = 0;
    private int comments = 0;
    private int styles = 0;
    private int tags = 0;

    @JsonCreator
    public StatisticsImpl() {

    }

    public int getPublicEntries() {
        return publicEntries;
    }

    public void setPublicEntries(final int publicEntries) {
        this.publicEntries = publicEntries;
    }

    public int getFriendsEntries() {
        return friendsEntries;
    }

    public void setFriendsEntries(final int friendsEntries) {
        this.friendsEntries = friendsEntries;
    }

    public int getPrivateEntries() {
        return privateEntries;
    }

    public void setPrivateEntries(final int privateEntries) {
        this.privateEntries = privateEntries;
    }

    public void setUsers(final int users) {
        this.users = users;
    }

    public void setEntries(final int entries) {
        this.entries = entries;
    }

    public void setComments(final int comments) {
        this.comments = comments;
    }

    public void setStyles(final int styles) {
        this.styles = styles;
    }

    public void setTags(final int tags) {
        this.tags = tags;
    }

    public int getUsers() {
        return users;
    }

    public int getEntries() {
        return entries;
    }

    public int getComments() {
        return comments;
    }

    public int getStyles() {
        return styles;
    }

    public int getTags() {
        return tags;
    }
}
