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

package com.justjournal.db.model;

import com.fasterxml.jackson.annotation.*;
import com.justjournal.utility.HTMLUtil;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * Journal entry transfer object.  Contains one journal entry. Maps relationship between table "entry" and java.
 *
 * @author Lucas Holt
 * @version 1.0
 * @see com.justjournal.db.EntryDao
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class EntryImpl implements EntryTo {
    @JsonProperty("entryId")
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

    private String subject = "";

    @JsonProperty("body")
    private String body = "";

    @JsonProperty("music")
    private String music = "";

    @JsonProperty("userName")
    private String userName = "";

    @JsonProperty("moodName")
    private String moodName = "";

    @JsonProperty("locationName")
    private String locationName = "";

    @JsonProperty("autoFormat")
    private boolean autoFormat = true;

    @JsonProperty("allowComments")
    private boolean allowComments = true;

    @JsonProperty("emailComments")
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
     * Retrieves entry entryId as an int >0
     *
     * @return entry entryId
     */
    @Override
    public int getId() {
        return id;
    }


    /**
     * Set the entry entryId to an int >0
     *
     * @param entryId entry entryId to set
     * @throws IllegalArgumentException entryId < 0
     */
    @Override
    public void setId(int entryId)
            throws IllegalArgumentException {
        if (entryId < 0)
            throw new IllegalArgumentException("Illegal entryId: " +
                    entryId);

        this.id = entryId;
    }


    /**
     * Retrieve the current location entryId
     *
     * @return location entryId
     */
    @Override
    public int getLocationId() {
        return locationId;
    }


    /**
     * Set the location entryId to an int >0
     *
     * @param loc location entryId to set
     * @throws IllegalArgumentException < 0
     */
    @Override
    public void setLocationId(int loc)
            throws IllegalArgumentException {
        if (loc < 0)
            throw new IllegalArgumentException("Illegal location entryId: " +
                    loc);
        locationId = loc;
    }

    /**
     * Retrieves the mood entryId
     *
     * @return mood entryId
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
            throw new IllegalArgumentException("Illegal mood entryId: " +
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
     * Retrieve the entrySubject
     *
     * @return The entrySubject of the entry
     */
    @Override
    public String getSubject() {
        return subject;
    }

    /**
     * Set the entrySubject.  If the entrySubject is null or an empty string, it will be set as (no entrySubject).
     *
     * @param entrySubject entrySubject to use
     * @throws IllegalArgumentException
     */
    @Override
    public void setSubject(String entrySubject)
            throws IllegalArgumentException {

        if (entrySubject == null || entrySubject.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = entrySubject;
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
            throw new IllegalArgumentException("Illegal user entryId: " +
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
    @NotNull
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
    @NotNull
    public String getMoodName() {
        return moodName;
    }

    @Override
    public void setMoodName(@NotNull String mood) {
        if (mood == null)
            throw new IllegalArgumentException("Illegal Mood Name");

        moodName = mood;
    }

    @Override
    @NotNull
    public boolean getAllowComments() {
        return allowComments;
    }

    @Override
    public void setAllowComments(@NotNull boolean allowComments) {
        this.allowComments = allowComments;
    }

    @Override
    @NotNull
    public boolean getEmailComments() {
        return emailComments;
    }

    @Override
    @NotNull
    public void setEmailComments(@NotNull boolean emailComments) {
        this.emailComments = emailComments;
    }

    @Override
    @NotNull
    public boolean getAutoFormat() {
        return autoFormat;
    }

    @Override
    public void setAutoFormat(@NotNull boolean autoFormat) {
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

        output.append("entry entryId: ");
        output.append(getId());
        output.append('\n');

        output.append("location entryId: ");
        output.append(getLocationId());
        output.append('\n');

        output.append("location name: ");
        output.append(getLocationName());
        output.append('\n');

        output.append("mood entryId: ");
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

        output.append("entrySubject: ");
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

        output.append("user entryId: ");
        output.append(getUserId());
        output.append('\n');

        output.append("autoformat: ");
        output.append(getAutoFormat());
        output.append('\n');

        output.append("allowComments: ");
        output.append(getAllowComments());
        output.append('\n');

        output.append("emailComments: ");
        output.append(getEmailComments());
        output.append('\n');

        return output.toString();
    }

    @JsonIgnore
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final EntryTo entryTo = (EntryTo) o;

        if (getAllowComments() != entryTo.getAllowComments()) return false;
        if (getAutoFormat() != entryTo.getAutoFormat()) return false;
        if (getCommentCount() != entryTo.getCommentCount()) return false;
        if (getEmailComments() != entryTo.getEmailComments()) return false;
        if (getId() != entryTo.getId()) return false;
        if (getLocationId() != entryTo.getLocationId()) return false;
        if (getMoodId() != entryTo.getMoodId()) return false;
        if (getSecurityLevel() != entryTo.getSecurityLevel()) return false;
        if (getUserId() != entryTo.getUserId()) return false;
        if (!getBody().equals(entryTo.getBody())) return false;
        return getDate().equals(entryTo.getDate()) && !(getLocationName() != null ? !getLocationName().equals(entryTo.getLocationName()) : entryTo.getLocationName() != null) && !(getMoodName() != null ? !getMoodName().equals(entryTo.getMoodName()) : entryTo.getMoodName() != null) && !(getMusic() != null ? !getMusic().equals(entryTo.getMusic()) : entryTo.getMusic() != null) && !(getSubject() != null ? !getSubject().equals(entryTo.getSubject()) : entryTo.getSubject() != null) && !(getUserName() != null ? !getUserName().equals(entryTo.getUserName()) : entryTo.getUserName() != null);
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
        result = 29 * result + (getAutoFormat() ? 1 : 0);
        result = 29 * result + (getAllowComments() ? 1 : 0);
        result = 29 * result + (getEmailComments() ? 1 : 0);
        return result;
    }
}