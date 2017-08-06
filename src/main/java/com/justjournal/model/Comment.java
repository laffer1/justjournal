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

package com.justjournal.model;

import com.fasterxml.jackson.annotation.*;
import com.justjournal.utility.HTMLUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * A comment
 *
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "comments")
public final class Comment implements Serializable {

    private static final long serialVersionUID = 3594701186407268256L;

    @Id
    @GeneratedValue
    private int id = 0;

    @JsonBackReference(value = "entry-comment")
    @JsonProperty("entry")
    @ManyToOne
    @JoinColumn(name = "eid")
    private Entry entry;

    @Column(name = "eid", insertable = false, updatable = false)
    private int eid;

    @JsonBackReference(value = "comment-user")
    @JsonProperty("user")
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @JsonIgnore
    @Column(name = "date")
    @Temporal(value = TemporalType.DATE)
    private Date date = new Date();

    @JsonProperty("subject")
    @Column(name = "subject", length = 150)
    private String subject = "";

    // @Basic(fetch = FetchType.LAZY)
    @JsonProperty("body")
    @Column(name = "body")
    @Lob
    private String body = "";

    @Getter
    @Setter 
    @JsonProperty("format")
    @Column(name = "format", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private FormatType format = FormatType.TEXT;

    @JsonCreator
    public Comment() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(final int commentId) {
        if (commentId < 0)
            throw new IllegalArgumentException("Illegal commentId: " +
                    commentId);

        this.id = commentId;
    }


    public int getEid() {
        return eid;
    }

    public void setEid(final int eid) {
        this.eid = eid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date dateTime) {
        this.date = dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subjectText) {

        if (subjectText.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = subjectText;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String bodyText) {
        if (bodyText.length() < 2)
            throw new IllegalArgumentException("Illegal bodyText: " +
                    bodyText);

        this.body = bodyText;
    }

    @JsonIgnore
    @Transient
    public String getBodyWithLinks() {
        return HTMLUtil.uriToLink(getBody());
    }

    @JsonIgnore
    @Transient
    public String getBodyWithoutHTML() {
        return HTMLUtil.stripHTMLTags(getBody());
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(final Entry entry) {
        this.entry = entry;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
