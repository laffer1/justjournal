/*
 * Copyright (c) 2014 Lucas Holt
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

package com.justjournal.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Lucas Holt
 */
public interface EntryTo {
    /**
     * Retrieves entry id as an int >0
     *
     * @return entry id
     */
    int getId();

    /**
     * Set the entry id to an int >0
     *
     * @param id entry id to set
     * @throws IllegalArgumentException id < 0
     */
    void setId(int id)
            throws IllegalArgumentException;

    /**
     * Retrieve the current location id
     *
     * @return location id
     */
    int getLocationId();

    /**
     * Set the location id to an int >0
     *
     * @param loc location id to set
     * @throws IllegalArgumentException < 0
     */
    void setLocationId(int loc)
            throws IllegalArgumentException;

    /**
     * Retrieves the mood id
     *
     * @return mood id
     */
    int getMoodId();

    /**
     * Sets the mood to an int > 0
     *
     * @param mood mood to set
     * @throws IllegalArgumentException < 0
     */
    void setMoodId(int mood)
            throws IllegalArgumentException;

    /**
     * Retrieve date
     *
     * @return current date
     */
    Date getDate();

    DateTime getDateTime();

    /**
     * Set the date using a string in the form 2004-01-30 22:02
     *
     * @param date date in format YYYY-MM-DD hh:mm
     * @throws IllegalArgumentException null or len < 6
     * @see com.justjournal.db.DateTimeBean
     */
    void setDate(String date)
            throws IllegalArgumentException;

    /**
     * Set the date using a <code>DateTimeBean</code>
     *
     * @param dateTime DateTimeBean
     * @see com.justjournal.db.DateTimeBean
     */
    void setDate(DateTime dateTime);

    void setDate(Date date);

    /**
     * Retrieve the subject
     *
     * @return The subject of the entry
     */
    String getSubject();

    /**
     * Set the subject.  If the subject is null or an empty string, it will be set as (no subject).
     *
     * @param subject subject to use
     * @throws IllegalArgumentException
     */
    void setSubject(String subject)
            throws IllegalArgumentException;

    /**
     * @return the body in text or html with or without autoformat
     */
    String getBody();

    @JsonIgnore
    String getBodyWithLinks();

    @JsonIgnore
    String getBodyWithoutHTML();

    void setBody(String body)
            throws IllegalArgumentException;

    String getMusic();

    void setMusic(String music);

    int getCommentCount();

    void setCommentCount(int comment)
            throws IllegalArgumentException;

    int getUserId();

    void setUserId(int uid)
            throws IllegalArgumentException;

    int getSecurityLevel();

    void setSecurityLevel(int sec)
            throws IllegalArgumentException;

    String getUserName();

    void setUserName(String user);

    String getLocationName();

    void setLocationName(String loc);

    String getMoodName();

    void setMoodName(String mood);

    boolean getAllowComments();

    void setAllowComments(boolean allowComments);

    boolean getEmailComments();

    void setEmailComments(boolean emailComments);

    boolean getAutoFormat();

    void setAutoFormat(boolean autoFormat);

    ArrayList<String> getTags();

    void setTags(ArrayList<String> tag);
}
