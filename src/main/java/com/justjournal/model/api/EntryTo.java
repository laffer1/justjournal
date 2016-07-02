package com.justjournal.model.api;

/*
Copyright (c) 2016, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

import com.fasterxml.jackson.annotation.*;
import com.justjournal.model.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Journal entry transfer object.
 *
 * @author Lucas Holt
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntryTo implements Serializable {
    private static final long serialVersionUID = 6558001750470601777L;

    private int id = 0;
    private Date date = new Date();
    private Date modified;
    private int location;
    private int mood;
    private String user;
    private int security;
    private String subject = "";
    private String body = "";

    private String music = "";

    @JsonProperty("autoFormat")
    private Boolean autoFormat = true;

    @JsonProperty("allowComments")
    private Boolean allowComments = true;

    @JsonProperty("emailComments")
    private Boolean emailComments = true;

    private Boolean draft = false;

    @JsonProperty("tags")
    private Set<EntryTag> tags = new HashSet<EntryTag>();

    @JsonProperty("comments")
    private Set<Comment> comments = new HashSet<Comment>();

    @JsonCreator
    public EntryTo() {

    }

    public void setLocation(final int location) {
        this.location = location;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(final int mood) {
        this.mood = mood;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public int getSecurity() {
        return security;
    }

    public void setSecurity(final int security) {
        this.security = security;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(final Date modified) {
        this.modified = modified;
    }

    public int getLocation() {
        return location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(final String music) {
        this.music = music;
    }

    public Boolean getAutoFormat() {
        return autoFormat;
    }

    public void setAutoFormat(final Boolean autoFormat) {
        this.autoFormat = autoFormat;
    }

    public Boolean getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(final Boolean allowComments) {
        this.allowComments = allowComments;
    }

    public Boolean getEmailComments() {
        return emailComments;
    }

    public void setEmailComments(final Boolean emailComments) {
        this.emailComments = emailComments;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(final Boolean draft) {
        this.draft = draft;
    }

    public Set<EntryTag> getTags() {
        return tags;
    }

    public void setTags(final Set<EntryTag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(final Set<Comment> comments) {
        this.comments = comments;
    }
}