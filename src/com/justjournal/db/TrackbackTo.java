/*
Copyright (c) 2008 Lucas Holt
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

package com.justjournal.db;

/**
 * A Trackback ping
 *
 * @author Lucas Holt
 * @version $Id: TrackbackTo.java,v 1.5 2008/05/05 09:36:16 laffer1 Exp $
 *          User: laffer1
 *          Date: May 5, 2008
 *          Time: 3:11:11 AM
 */
public class TrackbackTo {
    /*
    id  int 10  unsigned auto  (trackback unit id)
    eid (entry it refers to)
    date datetime
    subject  (title, name)   varchar 150
    body   (comment, excert)
    email (author email)
    name  (author name)
    type  (trackback, pingback, post-it)
    */
    private int id;
    private int entryId;
    private DateTime date;
    private String subject;
    private String body;
    private String authorEmail;
    private String authorName;
    private String blogName;
    private String url;
    private TrackbackType type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) throws IllegalArgumentException {
        if (url == null || url.length() > 5)
            throw new IllegalArgumentException("Illegal url: " + url);

        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws IllegalArgumentException {
        if (id < 0)
            throw new IllegalArgumentException("Illegal id: " +
                    id);
        this.id = id;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) throws IllegalArgumentException {
        if (entryId < 0)
            throw new IllegalArgumentException("Illegal eid: " +
                    entryId);
        this.entryId = entryId;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(String date) throws IllegalArgumentException {
        if (date.length() < 6)
            throw new IllegalArgumentException("Illegal date: " +
                    date);
        DateTime newDate = new DateTimeBean();

        try {
            newDate.set(date);
            this.date = newDate;
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal date");
        }
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) throws IllegalArgumentException {
        if (subject.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = subject;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) throws IllegalArgumentException {
        if (blogName.length() == 0)
            this.blogName = "";  // TODO: Hardcode something like subjects have?
        else
            this.blogName = blogName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body == null)
            this.body = "";
        else
            this.body = body;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        if (authorEmail == null)
            this.authorEmail = "";
        else
            this.authorEmail = authorEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        if (authorName == null)
            this.authorName = "";
        else
            this.authorName = authorName;
    }

    public TrackbackType getType() {
        return type;
    }

    public void setType(TrackbackType type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackbackTo that = (TrackbackTo) o;

        if (entryId != that.entryId) return false;
        if (id != that.id) return false;
        if (authorEmail != null ? !authorEmail.equals(that.authorEmail) : that.authorEmail != null) return false;
        if (authorName != null ? !authorName.equals(that.authorName) : that.authorName != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (type != that.type) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = id;
        result = 31 * result + entryId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (authorEmail != null ? authorEmail.hashCode() : 0);
        result = 31 * result + (authorName != null ? authorName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

}
