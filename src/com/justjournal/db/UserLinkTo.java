package com.justjournal.db;

/**
 * User: laffer1
 * Date: Dec 22, 2005
 * Time: 2:58:39 PM
 */
public class UserLinkTo {
    private int userId;
    private String title;
    private String uri;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserLinkTo that = (UserLinkTo) o;

        if (userId != that.userId) return false;
        if (!title.equals(that.title)) return false;
        if (!uri.equals(that.uri)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = userId;
        result = 29 * result + title.hashCode();
        result = 29 * result + uri.hashCode();
        return result;
    }

    public String toString() {
        return userId + "," + title + "," + uri;
    }

    public UserLinkTo(int userId, String title, String uri) {
        this.userId = userId;
        this.title = title;
        this.uri = uri;
    }

    public UserLinkTo() {
    }
}
