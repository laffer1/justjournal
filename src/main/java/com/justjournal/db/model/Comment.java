/*
 * Copyright (c) 2005-2011 Lucas Holt
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
import com.justjournal.utility.HTMLUtil;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Component;

/**
 * A comment
 *
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Component
public final class Comment {

    private int id = 0;
    private int eid = 0;
    private int userId = 0;

    private DateTime date = new DateTimeBean();
    private String subject = "";
    private String body = "";
    private String userName = "";

    @JsonCreator
    public Comment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int commentId)
            throws IllegalArgumentException {
        if (commentId < 0)
            throw new IllegalArgumentException("Illegal commentId: " +
                    commentId);

        this.id = commentId;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int entryId)
            throws IllegalArgumentException {
        if (entryId < 0)
            throw new IllegalArgumentException("Illegal entryId: " +
                    entryId);

        this.eid = entryId;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(String commentDate)
            throws IllegalArgumentException {
        if (commentDate.length() < 6)
            throw new IllegalArgumentException("Illegal commentDate: " +
                    commentDate);
        DateTime newDate = new DateTimeBean();

        try {
            newDate.set(commentDate);
            this.setDate(newDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal commentDate");
        }
    }

    public void setDate(DateTime dateTime) {
        this.date = dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subjectText)
            throws IllegalArgumentException {

        if (subjectText.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = subjectText;
    }

    public String getBody() {
        return body;
    }

    @JsonIgnore
    public String getBodyWithLinks() {
        return HTMLUtil.uriToLink(getBody());
    }

    @JsonIgnore
    public String getBodyWithoutHTML() {
        return HTMLUtil.stripHTMLTags(getBody());
    }

    public void setBody(String bodyText)
            throws IllegalArgumentException {
        if (bodyText.length() < 2)
            throw new IllegalArgumentException("Illegal bodyText: " +
                    bodyText);

        this.body = bodyText;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int uid)
            throws IllegalArgumentException {
        if (uid < 0)
            throw new IllegalArgumentException("Illegal user commentId: " +
                    uid);
        userId = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        userName = user;
    }

    @JsonIgnore
    @NotNull
    @Override
    public final String toString() {

        return "comment commentId: " + getId() + '\n' + "entry commentId: " + getEid() + '\n'
                + "commentDate: " + getDate() + '\n' + "subjectText: " + getSubject() + '\n'
                + "bodyText: " + getBody() + '\n' + "user commentId: " + getUserId() + '\n';
    }

    @JsonIgnore
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Comment commentTo = (Comment) o;

        return getEid() == commentTo.getEid() && getId() == commentTo.getId() && getUserId() == commentTo.getUserId() && getBody().equals(commentTo.getBody()) && getDate().equals(commentTo.getDate()) && !(getSubject() != null ? !getSubject().equals(commentTo.getSubject()) : commentTo.getSubject() != null) && getUserName().equals(commentTo.getUserName());
    }

    @JsonIgnore
    @Override
    public final int hashCode() {
        int result;
        result = getId();
        result = 29 * result + getEid();
        result = 29 * result + getUserId();
        result = 29 * result + getDate().hashCode();
        result = 29 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 29 * result + getBody().hashCode();
        result = 29 * result + getUserName().hashCode();
        return result;
    }

}
