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

import com.fasterxml.jackson.annotation.*;
import com.justjournal.utility.HTMLUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * Journal entry transfer object.  Contains one journal entry. Maps relationship between table "entry" and java.
 *
 * @author Lucas Holt
 * @version 1.0
 * @see EntryDaoImpl
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class EntryImpl implements EntryTo {
    @JsonProperty("id")
    private int id = 0;

    @JsonProperty("locationId")
    private int locationId = 0;

    @JsonProperty("moodId")
    private int moodId = 0;

    @JsonProperty("commentCount")
    private int commentCount = 0;
    private int userId = 0;
    private int securityLevel = 0;

    @JsonIgnore
    private Date date = new Date();

    private String subject;

    @JsonProperty("body")
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

    @JsonIgnore
    private ArrayList<String> tags = new ArrayList<String>();

    @JsonCreator
    public EntryImpl() {

    }


    /**
     * Retrieves entry id as an int >0
     *
     * @return entry id
     */
    @Override
    public int getId() {
        return id;
    }


    /**
     * Set the entry id to an int >0
     *
     * @param id entry id to set
     * @throws IllegalArgumentException id < 0
     */
    @Override
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
    @Override
    public int getLocationId() {
        return locationId;
    }


    /**
     * Set the location id to an int >0
     *
     * @param loc location id to set
     * @throws IllegalArgumentException < 0
     */
    @Override
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
    @Override
    public int getMoodId() {
        return moodId;
    }

    /**
     * Sets the mood to an int > 0
     *
     * @param mood mood to set
     * @throws IllegalArgumentException < 0
     */
    @Override
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
    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public DateTime getDateTime() {
        return new DateTimeBean(getDate());
    }

    /**
     * Set the date using a string in the form 2004-01-30 22:02
     * <p/>
     * TODO: create a parser to check the date more thoroughly.  DateTimeBean will throw an exception if the format is
     * wrong though!
     *
     * @param date date in format YYYY-MM-DD hh:mm
     * @throws IllegalArgumentException null or len < 6
     * @see DateTimeBean
     */
    @Override
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
            this.setDate(newDate.toDate());
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal date");
        }
    }


    /**
     * Set the date using a <code>DateTimeBean</code>
     *
     * @param dateTime DateTimeBean
     * @see DateTimeBean
     */
    @Override
    public void setDate(DateTime dateTime) {
        this.setDate(dateTime.toDate());
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retrieve the subject
     *
     * @return The subject of the entry
     */
    @Override
    public String getSubject() {
        return subject;
    }

    /**
     * Set the subject.  If the subject is null or an empty string, it will be set as (no subject).
     *
     * @param subject subject to use
     * @throws IllegalArgumentException
     */
    @Override
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
    @Override
    public String getBody() {
        return body;
    }

    @Override
    @JsonIgnore
    public String getBodyWithLinks() {
        return HTMLUtil.uriToLink(getBody());
    }

    @Override
    @JsonIgnore
    public String getBodyWithoutHTML() {
        return HTMLUtil.stripHTMLTags(getBody());
    }

    @Override
    public void setBody(String body)
            throws IllegalArgumentException {
        if (body == null || body.length() < 2)
            throw new IllegalArgumentException("Illegal body: " +
                    body);

        this.body = body;
    }

    @Override
    public String getMusic() {
        return music;
    }

    @Override
    public void setMusic(String music) {
        if (music == null)
            this.music = "";
        else
            this.music = music;
    }

    @Override
    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public void setCommentCount(int comment)
            throws IllegalArgumentException {
        if (comment < 0)
            throw new IllegalArgumentException("Illegal comment count: " +
                    comment);
        commentCount = comment;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int uid)
            throws IllegalArgumentException {
        if (uid < 0)
            throw new IllegalArgumentException("Illegal user id: " +
                    uid);
        userId = uid;
    }

    @Override
    public int getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public void setSecurityLevel(int sec)
            throws IllegalArgumentException {
        if (sec < 0)
            throw new IllegalArgumentException("Illegal security level: " +
                    sec);
        securityLevel = sec;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String user) {
        if (user == null)
            throw new IllegalArgumentException("Illegal User Name");

        userName = user;
    }

    @Override
    public String getLocationName() {
        return locationName;
    }

    @Override
    public void setLocationName(String loc) {
        if (loc == null)
            throw new IllegalArgumentException("Illegal Location Name");

        locationName = loc;
    }

    @Override
    public String getMoodName() {
        return moodName;
    }

    @Override
    public void setMoodName(String mood) {
        if (mood == null)
            throw new IllegalArgumentException("Illegal Mood Name");

        moodName = mood;
    }

    @Override
    public boolean getAllowComments() {
        return this.isAllowComments();
    }

    @Override
    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    @Override
    public boolean getEmailComments() {
        return this.isEmailComments();
    }

    @Override
    public void setEmailComments(boolean emailComments) {
        this.emailComments = emailComments;
    }

    @Override
    public boolean getAutoFormat() {
        return this.isAutoFormat();
    }

    @Override
    public void setAutoFormat(boolean autoFormat) {
        this.autoFormat = autoFormat;
    }

    @Override
    public ArrayList<String> getTags() {
        return tags;
    }

    @Override
    public void setTags(ArrayList<String> tag) {
        this.tags = tag;
    }

    @Override
    @JsonIgnore
    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append("entry id: ");
        output.append(getId());
        output.append('\n');

        output.append("location id: ");
        output.append(getLocationId());
        output.append('\n');

        output.append("location name: ");
        output.append(getLocationName());
        output.append('\n');

        output.append("mood id: ");
        output.append(getMoodId());
        output.append('\n');

        output.append("mood name: ");
        output.append(getMoodName());
        output.append('\n');

        output.append("comment count: ");
        output.append(getCommentCount());
        output.append('\n');

        output.append("date: ");
        output.append(getDate().toString());
        output.append('\n');

        output.append("subject: ");
        output.append(getSubject());
        output.append('\n');

        output.append("body: ");
        output.append(getBody());
        output.append('\n');

        output.append("music: ");
        output.append(getMusic());
        output.append('\n');

        output.append("security level: ");
        output.append(getSecurityLevel());
        output.append('\n');

        output.append("user id: ");
        output.append(getUserId());
        output.append('\n');

        output.append("autoformat: ");
        output.append(isAutoFormat());
        output.append('\n');

        output.append("allowComments: ");
        output.append(isAllowComments());
        output.append('\n');

        output.append("emailComments: ");
        output.append(isEmailComments());
        output.append('\n');

        return output.toString();
    }

    @JsonIgnore
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final EntryImpl entryTo = (EntryImpl) o;

        if (isAllowComments() != entryTo.isAllowComments()) return false;
        if (isAutoFormat() != entryTo.isAutoFormat()) return false;
        if (getCommentCount() != entryTo.getCommentCount()) return false;
        if (isEmailComments() != entryTo.isEmailComments()) return false;
        if (getId() != entryTo.getId()) return false;
        if (getLocationId() != entryTo.getLocationId()) return false;
        if (getMoodId() != entryTo.getMoodId()) return false;
        if (getSecurityLevel() != entryTo.getSecurityLevel()) return false;
        if (getUserId() != entryTo.getUserId()) return false;
        if (!getBody().equals(entryTo.getBody())) return false;
        if (!getDate().equals(entryTo.getDate())) return false;
        if (getLocationName() != null ? !getLocationName().equals(entryTo.getLocationName()) : entryTo.getLocationName() != null)
            return false;
        if (getMoodName() != null ? !getMoodName().equals(entryTo.getMoodName()) : entryTo.getMoodName() != null)
            return false;
        if (getMusic() != null ? !getMusic().equals(entryTo.getMusic()) : entryTo.getMusic() != null) return false;
        return !(getSubject() != null ? !getSubject().equals(entryTo.getSubject()) : entryTo.getSubject() != null) && !(getUserName() != null ? !getUserName().equals(entryTo.getUserName()) : entryTo.getUserName() != null);
    }

    @JsonIgnore
    @Override
    public int hashCode() {
        int result;
        result = getId();
        result = 29 * result + getLocationId();
        result = 29 * result + getMoodId();
        result = 29 * result + getCommentCount();
        result = 29 * result + getUserId();
        result = 29 * result + getSecurityLevel();
        result = 29 * result + getDate().hashCode();
        result = 29 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 29 * result + getBody().hashCode();
        result = 29 * result + (getMusic() != null ? getMusic().hashCode() : 0);
        result = 29 * result + (getUserName() != null ? getUserName().hashCode() : 0);
        result = 29 * result + (getMoodName() != null ? getMoodName().hashCode() : 0);
        result = 29 * result + (getLocationName() != null ? getLocationName().hashCode() : 0);
        result = 29 * result + (isAutoFormat() ? 1 : 0);
        result = 29 * result + (isAllowComments() ? 1 : 0);
        result = 29 * result + (isEmailComments() ? 1 : 0);
        return result;
    }

    public boolean isAllowComments() {
        return allowComments;
    }

    public boolean isAutoFormat() {
        return autoFormat;
    }

    public boolean isEmailComments() {
        return emailComments;
    }
}