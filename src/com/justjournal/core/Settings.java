/*
 * Copyright (c) 2006, 2011 Lucas Holt
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

package com.justjournal.core;

import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.query.SelectQuery;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Global settings for the site.  Any changes here will effect the entire site.  User preferences are separate and any
 * negative setting here will supersede per user settings like commentMailEnable = false would disable email comments
 * even if Users have them turned on.
 *
 * @author Lucas Holt
 * @version $Id: Settings.java,v 1.10 2012/06/23 18:15:31 laffer1 Exp $
 * @since 1.0
 */
public class Settings {
    private static Logger log = Logger.getLogger(Settings.class.getName());

    /* paths */
    private String baseUri = "http://localhost:8080/";
    private String fsPath = ""; // file storage path.
    private String contextPath = "";

    /* site settings */
    private boolean siteEnable = true;
    private String siteName = "";
    private String siteAdmin = "";
    private String siteAdminEmail = "";
    private String siteBlog = "";  // blog about site activities, updates.
    private boolean siteSearch = true;  // enable site search feature
    private boolean siteDirectory = true;  // member directory
    private boolean siteRss = true; // recent blog posts

    /* e-mail */
    private boolean mailEnable = true;
    private String mailHost = "localhost";
    private int mailPort = 25;
    private String mailUser = "";
    private String mailPass = "";
    private String mailFrom = siteAdminEmail;
    private String mailSubject = siteName + " Notification";

    /* comments */
    private boolean commentEnable = true;
    private boolean commentMailEnable = true;
    private boolean commentEnableAnonymous = false;

    /* time */
    private int tzOffset = -5;
    private String tzName = "EST";
    private boolean tzLocalize = true;
    private boolean tzUseGMT = true;

    /* Users */
    private boolean userAllowNew = true;

    public Settings() {
        try {
            String name;
            String value;

            for (com.justjournal.model.Settings set : list()) {
                value = set.getValue();
                name = set.getKeyName();

                if (name == null) {
                    log.warn("setting.name is null");
                    continue;
                }
                if (value == null) {
                    log.warn("setting.value is null");
                    continue;
                }

                if (name.equalsIgnoreCase("baseuri"))
                    baseUri = value;
                else if (name.equalsIgnoreCase("fsPath"))
                    fsPath = value;
                else if (name.equalsIgnoreCase("contextPath"))
                    contextPath = value;
                else if (name.equalsIgnoreCase("siteEnable"))
                    siteEnable = value.equalsIgnoreCase("true");
                else if (name.equalsIgnoreCase("siteName"))
                    siteName = value;
                else if (name.equalsIgnoreCase("siteAdmin"))
                    siteAdmin = value;
                else if (name.equalsIgnoreCase("siteAdminEmail"))
                    siteAdminEmail = value;
                else if (name.equalsIgnoreCase("siteBlog"))
                    siteBlog = value;
                else if (name.equalsIgnoreCase("siteSearch")) {
                    siteSearch = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("siteDirectory")) {
                    siteDirectory = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("siteRss")) {
                    siteRss = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("mailEnable")) {
                    mailEnable = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("mailHost"))
                    mailHost = value;
                else if (name.equalsIgnoreCase("mailPort"))
                    mailPort = Integer.parseInt(value);
                else if (name.equalsIgnoreCase("mailUser"))
                    mailUser = value;
                else if (name.equalsIgnoreCase("mailPass"))
                    mailPass = value;
                else if (name.equalsIgnoreCase("mailFrom"))
                    mailFrom = value;
                else if (name.equalsIgnoreCase("mailSubject"))
                    mailSubject = value;
                else if (name.equalsIgnoreCase("commentEnable")) {
                    commentEnable = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("commentMailEnable")) {
                    commentMailEnable = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("commentEnableAnonymous")) {
                    mailEnable = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("tzOffset"))
                    tzOffset = Integer.parseInt(value);
                else if (name.equalsIgnoreCase("tzName"))
                    tzName = value;
                else if (name.equalsIgnoreCase("tzLocalize")) {
                    tzLocalize = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("tzUseGMT")) {
                    tzUseGMT = value.equalsIgnoreCase("true");
                } else if (name.equalsIgnoreCase("userAllowNew")) {
                    userAllowNew = value.equalsIgnoreCase("true");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    public List<com.justjournal.model.Settings> list() throws CayenneRuntimeException {
        ObjectContext dataContext;
        try {
            dataContext = DataContext.getThreadObjectContext();
        } catch (Exception e) {
            ServerRuntime cayenneRuntime = new ServerRuntime("cayenne-JustJournalDomain.xml");
            dataContext = cayenneRuntime.getContext();

            DataContext.bindThreadObjectContext(dataContext);
            log.debug(e);
        }
        final SelectQuery query = new SelectQuery(com.justjournal.model.Settings.class);
        return dataContext.performQuery(query);
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getFsPath() {
        return fsPath;
    }

    public void setFsPath(String fsPath) {
        this.fsPath = fsPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public boolean isSiteEnable() {
        return siteEnable;
    }

    public void setSiteEnable(boolean siteEnable) {
        this.siteEnable = siteEnable;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(String siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public String getSiteAdminEmail() {
        return siteAdminEmail;
    }

    public void setSiteAdminEmail(String siteAdminEmail) {
        this.siteAdminEmail = siteAdminEmail;
    }

    public String getSiteBlog() {
        return siteBlog;
    }

    public void setSiteBlog(String siteBlog) {
        this.siteBlog = siteBlog;
    }

    public boolean isSiteSearch() {
        return siteSearch;
    }

    public void setSiteSearch(boolean siteSearch) {
        this.siteSearch = siteSearch;
    }

    public boolean isSiteDirectory() {
        return siteDirectory;
    }

    public void setSiteDirectory(boolean siteDirectory) {
        this.siteDirectory = siteDirectory;
    }

    public boolean isSiteRss() {
        return siteRss;
    }

    public void setSiteRss(boolean siteRss) {
        this.siteRss = siteRss;
    }

    public boolean isMailEnable() {
        return mailEnable;
    }

    public void setMailEnable(boolean mailEnable) {
        this.mailEnable = mailEnable;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public int getMailPort() {
        return mailPort;
    }

    public void setMailPort(int mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailPass() {
        return mailPass;
    }

    public void setMailPass(String mailPass) {
        this.mailPass = mailPass;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public boolean isCommentEnable() {
        return commentEnable;
    }

    public void setCommentEnable(boolean commentEnable) {
        this.commentEnable = commentEnable;
    }

    public boolean isCommentMailEnable() {
        return commentMailEnable;
    }

    public void setCommentMailEnable(boolean commentMailEnable) {
        this.commentMailEnable = commentMailEnable;
    }

    public boolean isCommentEnableAnonymous() {
        return commentEnableAnonymous;
    }

    public void setCommentEnableAnonymous(boolean commentEnableAnonymous) {
        this.commentEnableAnonymous = commentEnableAnonymous;
    }

    public int getTzOffset() {
        return tzOffset;
    }

    public void setTzOffset(int tzOffset) {
        this.tzOffset = tzOffset;
    }

    public String getTzName() {
        return tzName;
    }

    public void setTzName(String tzName) {
        this.tzName = tzName;
    }

    public boolean isTzLocalize() {
        return tzLocalize;
    }

    public void setTzLocalize(boolean tzLocalize) {
        this.tzLocalize = tzLocalize;
    }

    public boolean isTzUseGMT() {
        return tzUseGMT;
    }

    public void setTzUseGMT(boolean tzUseGMT) {
        this.tzUseGMT = tzUseGMT;
    }

    public boolean isUserAllowNew() {
        return userAllowNew;
    }

    public void setUserAllowNew(boolean userAllowNew) {
        this.userAllowNew = userAllowNew;
    }

    public static Settings getSettings(ServletContext ctx) {
        Settings set;
        synchronized (ctx) {
            set = (Settings) ctx.getAttribute("JustJournal_Settings");
            if (set == null) {
                set = new Settings();
                ctx.setAttribute("JustJournal_Settings", set);
            }
        }
        return set;
    }

}
