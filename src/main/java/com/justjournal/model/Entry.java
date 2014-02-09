/*
Copyright (c) 2005, Lucas Holt
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

package com.justjournal.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Journal entry transfer object.  Contains one journal entry. Maps relationship between table "entry" and java.
 *
 * @author Lucas Holt
 * @version 1.0
 * @see com.justjournal.repository.EntryRepository
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "entry")
public class Entry implements Serializable {
    private static final long serialVersionUID = 6558001750470601772L;
    @Id
    @GeneratedValue
    private int id = 0;

    @JsonIgnore
    @Column(name = "date")
    @Temporal(value = TemporalType.DATE)
    private Date date = new Date();

    @JsonProperty("locationId")
    @OneToMany
    @JoinColumn(name = "location_id")
    private Location location;

    @JsonProperty("mood")
    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    @JsonProperty("user")
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @JsonProperty("security")
    @ManyToOne
    @JoinColumn(name = "security_id")
    private Security security;

    @JsonProperty("subject")
    @Column(name = "subject", length = 255)
    private String subject = "";

    @JsonProperty("body")
    @Column(name = "body")
    @Lob
    private String body = "";

    @JsonProperty("music")
    @Column(name = "music", length = 125)
    private String music = "";

    @JsonProperty("autoFormat")
    @Column(name = "autoformat", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private PrefBool autoFormat;

    @JsonProperty("allowComments")
    @Column(name = "allow_comments", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private PrefBool allowComments;

    @JsonProperty("emailComments")
    @Column(name = "email_comments", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private PrefBool emailComments;

    @Column(name = "draft", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private PrefBool draft;

    // TODO: implement
    transient private int attachImage = 0;
    transient private int attachFile = 0;

    @JsonProperty("tags")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entry")
    private Set<Tag> tags = new HashSet<Tag>();

    @JsonProperty("comments")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entry")
    private Set<Comment> comments = new HashSet<Comment>();

    @JsonCreator
    public Entry() {

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(final Mood mood) {
        this.mood = mood;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(final Security security) {
        this.security = security;
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

    public PrefBool getAutoFormat() {
        return autoFormat;
    }

    public void setAutoFormat(final PrefBool autoFormat) {
        this.autoFormat = autoFormat;
    }

    public PrefBool getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(final PrefBool allowComments) {
        this.allowComments = allowComments;
    }

    public PrefBool getEmailComments() {
        return emailComments;
    }

    public void setEmailComments(final PrefBool emailComments) {
        this.emailComments = emailComments;
    }

    public PrefBool getDraft() {
        return draft;
    }

    public void setDraft(final PrefBool draft) {
        this.draft = draft;
    }

    public int getAttachImage() {
        return attachImage;
    }

    public void setAttachImage(final int attachImage) {
        this.attachImage = attachImage;
    }

    public int getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(final int attachFile) {
        this.attachFile = attachFile;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(final Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(final Set<Comment> comments) {
        this.comments = comments;
    }
}