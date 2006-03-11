package com.justjournal.core;

/**
 * Global settings for the site.  Any changes here will
 * effect the entire site.  User preferences are seperate and
 * any negative setting here will supersede per user settings
 * like commentMailEnable = false would disable email comments
 * even if users have them turned on.
 * <p/>
 * Date: Mar 5, 2006
 * Time: 11:03:13 PM
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 */
public class Settings {
    /* paths */
    private String baseUri;
    private String fsPath;
    private String contextPath;

    /* site settings */
    private boolean siteEnable;
    private String siteName;
    private String siteAdmin;
    private String siteAdminEmail;
    private String siteBlog;
    private boolean siteSearch;
    private boolean siteDirectory;
    private boolean siteRss;

    /* e-mail */
    private boolean mailEnable;
    private String mailHost;
    private String mailPort;
    private String mailUser;
    private String mailPass;
    private String mailFrom;
    private String mailSubject;

    /* comments */
    private boolean commentEnable;
    private boolean commentMailEnable;
    private boolean commentEnableAnonymous;

    /* time */
    private int tzOffset;
    private String tzName;
    private boolean tzLocalize;
    private boolean tzUseGMT;

    /* users */
    private boolean userAllowNew;

    public Settings() {

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

    public String getMailPort() {
        return mailPort;
    }

    public void setMailPort(String mailPort) {
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

}
