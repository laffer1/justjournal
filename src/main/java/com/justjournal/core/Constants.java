package com.justjournal.core;

/**
 * @author Lucas Holt
 */
public class Constants {

    private Constants() {
        super();
    }

    public static final char endl = '\n';
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static final String MIME_TYPE_RSS = "application/rss+xml";

    public static final String JJ_LOGIN_OK = "JJ.LOGIN.OK";
    public static final String JJ_LOGIN_FAIL = "JJ.LOGIN.FAIL";
    public static final String JJ_LOGIN_NONE = "JJ.LOGIN.NONE";

    public static final String LOGIN_ATTRNAME = "auth.user";
    public static final String LOGIN_ATTRID = "auth.uid";

    public static final int USERNAME_MAX_LENGTH = 15;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 18;
    public static final int BAD_USER_ID = 0;

    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";
    public static final String HEADER_EXPIRES = "Expires";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";
    public static final String HEADER_ACCEPT_ALL = "Accept=*/*";

    public static final String PARAM_SIZE = "size";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_ID = "id";
    public static final String PARAM_ENTRY_ID = "entryId";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_MOBILE = "mobile";
    public static final String PARAM_DASHBOARD = "dash";
    public static final String PARAM_SECURITY = "security";
    public static final String PARAM_LOCATION = "location";
    public static final String PARAM_MOOD = "mood";
    public static final String PARAM_MUSIC = "music";
    public static final String PARAM_TRACKBACK = "trackback";
    public static final String PARAM_AUTO_FORMAT = "aformat";
    public static final String PARAM_TAGS = "tags";
    public static final String PARAM_ALLOW_COMMENT = "allow_comment";
    public static final String PARAM_EMAIL_COMMENT = "email_comment";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_YEAR = "year";
    public static final String PARAM_TITLE = "title";

    public static final String PATH_USERS = "users/";
    public static final String PATH_ENTRY = "/entry/";

    public static final String ERR_INVALID_LOGIN = "The login timed out or is invalid.";
    public static final String ERR_ADD_USER = "Could not add user";
}
