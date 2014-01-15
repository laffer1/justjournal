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

import com.justjournal.utility.HTMLUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Journal entry transfer object.  Contains one journal entry.
 * Maps relationship between table "entry" and java.
 *
 * @author Lucas Holt
 * @version 1.0
 * @see EntryDAO
 */
public class EntryTo {
    private int id;
    private int locationId;
    private int moodId;
    private int commentCount;
    private int userId;
    private int securityLevel;

    private Date date;

    private String subject;
    private String body;
    private String music;
    private String userName;
    private String moodName;
    private String locationName;

    private boolean autoFormat = true;
    private boolean allowComments = true;
    private boolean emailComments = true;
    private boolean draft = true;

    private int attachImage = 0;
    private int attachFile = 0;

    private ArrayList<String> tags = new ArrayList<String>();


    /**
     * Retrieves entry id as an int >0
     *
     * @return entry id
     */
    public int getId() {
        return id;
    }


    /**
     * Set the entry id to an int >0
     *
     * @param id entry id to set
     * @throws IllegalArgumentException id < 0
     */
    public void setId(int id)
            throws IllegalArgumentException {
        if (id < 0)
            throw new IllegalArgumentException("Illegal id: " +
                    id);

        this.id = id;
    }


    /**
     * Retrieve the current location id
     *
     * @return location id
     */
    public int getLocationId() {
        return locationId;
    }


    /**
     * Set the location id to an int >0
     *
     * @param loc location id to set
     * @throws IllegalArgumentException < 0
     */
    public void setLocationId(int loc)
            throws IllegalArgumentException {
        if (loc < 0)
            throw new IllegalArgumentException("Illegal location id: " +
                    loc);
        locationId = loc;
    }

    /**
     * Retrieves the mood id
     *
     * @return mood id
     */
    public int getMoodId() {
        return moodId;
    }

    /**
     * Sets the mood to an int > 0
     *
     * @param mood mood to set
     * @throws IllegalArgumentException < 0
     */
    public void setMoodId(int mood)
            throws IllegalArgumentException {
        if (mood < 0)
            throw new IllegalArgumentException("Illegal mood id: " +
                    mood);
        moodId = mood;
    }

    /**
     * Retrieve date
     *
     * @return current date
     */
    public Date getDate() {
        return date;
    }

    public DateTime getDateTime() {
        return new DateTimeBean(date);
    }

    /**
     * Set the date using a string in the form
     * 2004-01-30 22:02
     * <p/>
     * TODO: create a parser to check the date
     * more thoroughly.  DateTimeBean will throw
     * an exception if the format is wrong though!
     *
     * @param date date in format YYYY-MM-DD hh:mm
     * @throws IllegalArgumentException null or len < 6
     * @see DateTimeBean
     */
    public void setDate(String date)
            throws IllegalArgumentException {
        if (date == null)
            throw new IllegalArgumentException("Illegal date: null");

        if (date.length() < 6)
            throw new IllegalArgumentException("Illegal date: " +
                    date);
        DateTime newDate = new DateTimeBean();

        try {
            newDate.set(date);
            this.date = newDate.toDate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal date");
        }
    }


    /**
     * Set the date using a <code>DateTimeBean</code>
     *
     * @param date DateTimeBean
     * @see DateTimeBean
     */
    public void setDate(DateTime date) {
        this.date = date.toDate();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retrieve the subject
     *
     * @return The subject of the entry
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the subject.  If the subject is null or
     * an empty string, it will be set as (no subject).
     *
     * @param subject subject to use
     * @throws IllegalArgumentException
     */
    public void setSubject(String subject)
            throws IllegalArgumentException {

        if (subject == null || subject.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = subject;
    }


    /**
     * @return the body in text or html with or without autoformat
     */
    public String getBody() {
        return body;
    }

    public String getBodyWithLinks() {
        return HTMLUtil.uriToLink(body);
    }

    public String getBodyWithoutHTML() {
        return HTMLUtil.stripHTMLTags(body);
    }

    public void setBody(String body)
            throws IllegalArgumentException {
        if (body == null || body.length() < 2)
            throw new IllegalArgumentException("Illegal body: " +
                    body);

        this.body = body;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        if (music == null)
            this.music = "";
        else
            this.music = music;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int comment)
            throws IllegalArgumentException {
        if (comment < 0)
            throw new IllegalArgumentException("Illegal comment count: " +
                    comment);
        commentCount = comment;
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

    public int getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(int sec)
            throws IllegalArgumentException {
        if (sec < 0)
            throw new IllegalArgumentException("Illegal security level: " +
                    sec);
        securityLevel = sec;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        if (user == null)
            throw new IllegalArgumentException("Illegal User Name");

        userName = user;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String loc) {
        if (loc == null)
            throw new IllegalArgumentException("Illegal Location Name");

        locationName = loc;
    }

    public String getMoodName() {
        return moodName;
    }

    public void setMoodName(String mood) {
        if (mood == null)
            throw new IllegalArgumentException("Illegal Mood Name");

        moodName = mood;
    }

    public boolean getAllowComments() {
        return this.allowComments;
    }

    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    public boolean getEmailComments() {
        return this.emailComments;
    }

    public void setEmailComments(boolean emailComments) {
        this.emailComments = emailComments;
    }

    public boolean getAutoFormat() {
        return this.autoFormat;
    }

    public void setAutoFormat(boolean autoFormat) {
        this.autoFormat = autoFormat;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tag) {
        this.tags = tag;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append("entry id: ");
        output.append(id);
        output.append('\n');

        output.append("location id: ");
        output.append(locationId);
        output.append('\n');

        output.append("location name: ");
        output.append(locationName);
        output.append('\n');

        output.append("mood id: ");
        output.append(moodId);
        output.append('\n');

        output.append("mood name: ");
        output.append(moodName);
        output.append('\n');

        output.append("comment count: ");
        output.append(commentCount);
        output.append('\n');

        output.append("date: ");
        output.append(date.toString());
        output.append('\n');

        output.append("subject: ");
        output.append(subject);
        output.append('\n');

        output.append("body: ");
        output.append(body);
        output.append('\n');

        output.append("music: ");
        output.append(music);
        output.append('\n');

        output.append("security level: ");
        output.append(securityLevel);
        output.append('\n');

        output.append("user id: ");
        output.append(userId);
        output.append('\n');

        output.append("autoformat: ");
        output.append(autoFormat);
        output.append('\n');

        output.append("allowComments: ");
        output.append(allowComments);
        output.append('\n');

        output.append("emailComments: ");
        output.append(emailComments);
        output.append('\n');

        return output.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final EntryTo entryTo = (EntryTo) o;

        if (allowComments != entryTo.allowComments) return false;
        if (autoFormat != entryTo.autoFormat) return false;
        if (commentCount != entryTo.commentCount) return false;
        if (emailComments != entryTo.emailComments) return false;
        if (id != entryTo.id) return false;
        if (locationId != entryTo.locationId) return false;
        if (moodId != entryTo.moodId) return false;
        if (securityLevel != entryTo.securityLevel) return false;
        if (userId != entryTo.userId) return false;
        if (!body.equals(entryTo.body)) return false;
        if (!date.equals(entryTo.date)) return false;
        if (locationName != null ? !locationName.equals(entryTo.locationName) : entryTo.locationName != null)
            return false;
        if (moodName != null ? !moodName.equals(entryTo.moodName) : entryTo.moodName != null) return false;
        if (music != null ? !music.equals(entryTo.music) : entryTo.music != null) return false;
        if (subject != null ? !subject.equals(entryTo.subject) : entryTo.subject != null) return false;
        return !(userName != null ? !userName.equals(entryTo.userName) : entryTo.userName != null);
    }

    public int hashCode() {
        int result;
        result = id;
        result = 29 * result + locationId;
        result = 29 * result + moodId;
        result = 29 * result + commentCount;
        result = 29 * result + userId;
        result = 29 * result + securityLevel;
        result = 29 * result + date.hashCode();
        result = 29 * result + (subject != null ? subject.hashCode() : 0);
        result = 29 * result + body.hashCode();
        result = 29 * result + (music != null ? music.hashCode() : 0);
        result = 29 * result + (userName != null ? userName.hashCode() : 0);
        result = 29 * result + (moodName != null ? moodName.hashCode() : 0);
        result = 29 * result + (locationName != null ? locationName.hashCode() : 0);
        result = 29 * result + (autoFormat ? 1 : 0);
        result = 29 * result + (allowComments ? 1 : 0);
        result = 29 * result + (emailComments ? 1 : 0);
        return result;
    }

}