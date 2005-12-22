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

package com.justjournal.db;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 24, 2003
 * Time: 4:53:47 AM
 */
public class CommentTo {

    //id,uid,eid,date,subject,body
    private int id;
    private int eid;
    private int userId;

    private DateTimeBean date;
    private String subject;
    private String body;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id)
            throws IllegalArgumentException {
        if (id < 0)
            throw new IllegalArgumentException("Illegal id: " +
                    id);

        this.id = id;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid)
            throws IllegalArgumentException {
        if (eid < 0)
            throw new IllegalArgumentException("Illegal eid: " +
                    eid);

        this.eid = eid;
    }

    public DateTimeBean getDate() {
        return date;
    }

    public void setDate(String date)
            throws IllegalArgumentException {
        if (date.length() < 6)
            throw new IllegalArgumentException("Illegal date: " +
                    date);
        DateTimeBean newDate = new DateTimeBean();

        try {
            newDate.set(date);
            this.date = newDate;
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal date");
        }
    }

    public void setDate(DateTimeBean date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject)
            throws IllegalArgumentException {

        if (subject.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body)
            throws IllegalArgumentException {
        if (body.length() < 2)
            throw new IllegalArgumentException("Illegal body: " +
                    body);

        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int uid)
            throws IllegalArgumentException {
        if (uid < 0)
            throw new IllegalArgumentException("Illegal user id: " +
                    uid);
        userId = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        userName = user;
    }

    public String toString() {
        StringBuffer output = new StringBuffer();

        output.append("comment id: ");
        output.append(id);
        output.append('\n');

        output.append("entry id: ");
        output.append(eid);
        output.append('\n');

        output.append("date: ");
        output.append(date);
        output.append('\n');

        output.append("subject: ");
        output.append(subject);
        output.append('\n');

        output.append("body: ");
        output.append(body);
        output.append('\n');

        output.append("user id: ");
        output.append(userId);
        output.append('\n');

        return output.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CommentTo commentTo = (CommentTo) o;

        if (eid != commentTo.eid) return false;
        if (id != commentTo.id) return false;
        if (userId != commentTo.userId) return false;
        if (!body.equals(commentTo.body)) return false;
        if (!date.equals(commentTo.date)) return false;
        if (subject != null ? !subject.equals(commentTo.subject) : commentTo.subject != null) return false;
        if (!userName.equals(commentTo.userName)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = id;
        result = 29 * result + eid;
        result = 29 * result + userId;
        result = 29 * result + date.hashCode();
        result = 29 * result + (subject != null ? subject.hashCode() : 0);
        result = 29 * result + body.hashCode();
        result = 29 * result + userName.hashCode();
        return result;
    }

}
