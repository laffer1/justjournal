/*
 * Copyright (c) 2004, 2005, 2014 Lucas Holt
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

package com.justjournal.db.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

/**
 * Friend
 *
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Component
public final class FriendTo {
    private int id = 0;
    private String userName = "";
    private int ownerId = 0;
    private String ownerUserName = "";

    @JsonCreator
    public FriendTo() {

    }

    @JsonIgnore
    public FriendTo(int id, String userName, int ownerId, String ownerUserName) {
        this.setId(id);
        this.userName = userName;
        this.ownerId = ownerId;
        this.ownerUserName = ownerUserName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(final int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUserName() {
        return this.ownerUserName;
    }

    public void setOwnerUserName(final String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return Integer.toString(getId()) + "," + userName + ","
                + ownerId + "," + ownerUserName;
    }

    @JsonIgnore
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FriendTo friendTo = (FriendTo) o;

        return getId() == friendTo.getId() && ownerId == friendTo.ownerId && ownerUserName.equals(friendTo.ownerUserName) && userName.equals(friendTo.userName);
    }

    @JsonIgnore
    @Override
    public int hashCode() {
        int result;
        result = getId();
        result = 29 * result + userName.hashCode();
        result = 29 * result + ownerId;
        result = 29 * result + ownerUserName.hashCode();
        return result;
    }
}
