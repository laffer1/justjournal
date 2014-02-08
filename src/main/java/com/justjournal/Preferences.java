/*
 * Copyright (c) 2003, 2004, 2005, 2011 Lucas Holt
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

package com.justjournal;

import com.justjournal.db.UserDao;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;

/**
 * Loads and stores preferences for a just journal user given their username.
 * <p/>
 * This class almost fits the bean concept now.
 * <p/>
 * private journal field was added 1/2004.  If this option is set, the user does not want anyone to read their journal.
 * To read the journal, the user must login.  Public access is denied.
 *
 * @author Lucas Holt
 * @version $Id: Preferences.java,v 1.11 2012/07/04 18:49:20 laffer1 Exp $
 * @since 1.0
 */

public final class Preferences {
    private org.slf4j.Logger log = LoggerFactory.getLogger(Preferences.class);

    /* Users real name */
    private String name = "";   // real name!
    private int id = 0; // user id
    /* Default style */
    private int styleId = 1;  // theme of journal?
    private boolean allowSpider = false;
    private boolean privateJournal = false;  // journal viewable only by owner.
    private String styleDoc = "";
    private String styleUrl = "";
    private int emoticon = 1;  // default emoticon theme
    private int startYear = 2011;
    private String emailAddress = "";
    private boolean showAvatar = false;

    public Preferences(String userName)
            throws Exception {
        try {

            ResultSet RS = UserDao.getJournalPreferences(userName);

            if (RS.next()) {
                this.name = RS.getString("name");
                this.id = RS.getInt("id");
                this.styleId = RS.getInt("style");
                this.styleDoc = RS.getString("cssdoc");
                this.styleUrl = RS.getString("cssurl");
                this.emailAddress = RS.getString("email");

                if (RS.getInt("since") > 2003)
                    startYear = RS.getInt("since");

                this.allowSpider = RS.getString("allow_spider").equals("Y");

                this.privateJournal = RS.getString("owner_view_only").equals("Y");

                this.showAvatar = RS.getString("show_avatar").equals("Y");
            }

            RS.close();
        } catch (Exception ePrefs) {
            log.debug(ePrefs.getMessage());
            throw new Exception("Error loading preferences", ePrefs);
        }
    }

    public Preferences() {
        // shouldn't use this constructor.
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSpiderAllowed() {
        return this.allowSpider;
    }

    public boolean isSpiderAllowed(boolean allowSpider) {
        this.allowSpider = allowSpider;
        return allowSpider;
    }

    public boolean isPrivateJournal() {
        return this.privateJournal;
    }

    public boolean isPrivateJournal(boolean privateJournal) {
        this.privateJournal = privateJournal;
        return privateJournal;
    }

    public int getEmoticon() {
        return emoticon;
    }

    public void setEmoticon(int value) {
        this.emoticon = value;
    }

    public int getStyleId() {
        return this.styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public String getStyleDoc() {
        return this.styleDoc;
    }

    public void setStyleDoc(String doc) {
        this.styleDoc = doc;
    }

    public String getStyleUrl() {
        return this.styleUrl;
    }

    public void setStyleUrl(String url) {
        this.styleUrl = url;
    }

    public int getStartYear() {
        return this.startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean showAvatar() {
        return showAvatar;
    }

    public void showAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }

    public void recycle() {
        name = "";
        id = 0;  // user id
        styleId = 1;
        allowSpider = false;
        privateJournal = false;
        styleDoc = "";
        styleUrl = "";
        startYear = 2011;
        emailAddress = "";
    }
}