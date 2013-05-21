package com.justjournal.model.auto;

import java.util.Date;

import org.apache.cayenne.CayenneDataObject;

/**
 * Class _Trackback was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Trackback extends CayenneDataObject {

    public static final String AUTHOR_EMAIL_PROPERTY = "authorEmail";
    public static final String AUTHOR_NAME_PROPERTY = "authorName";
    public static final String BLOGNAME_PROPERTY = "blogname";
    public static final String BODY_PROPERTY = "body";
    public static final String DATE_PROPERTY = "date";
    public static final String EID_PROPERTY = "eid";
    public static final String MODIFIED_PROPERTY = "modified";
    public static final String SUBJECT_PROPERTY = "subject";
    public static final String TYPE_PROPERTY = "type";
    public static final String URL_PROPERTY = "url";

    public static final String ID_PK_COLUMN = "id";

    public void setAuthorEmail(String authorEmail) {
        writeProperty(AUTHOR_EMAIL_PROPERTY, authorEmail);
    }
    public String getAuthorEmail() {
        return (String)readProperty(AUTHOR_EMAIL_PROPERTY);
    }

    public void setAuthorName(String authorName) {
        writeProperty(AUTHOR_NAME_PROPERTY, authorName);
    }
    public String getAuthorName() {
        return (String)readProperty(AUTHOR_NAME_PROPERTY);
    }

    public void setBlogname(String blogname) {
        writeProperty(BLOGNAME_PROPERTY, blogname);
    }
    public String getBlogname() {
        return (String)readProperty(BLOGNAME_PROPERTY);
    }

    public void setBody(String body) {
        writeProperty(BODY_PROPERTY, body);
    }
    public String getBody() {
        return (String)readProperty(BODY_PROPERTY);
    }

    public void setDate(Date date) {
        writeProperty(DATE_PROPERTY, date);
    }
    public Date getDate() {
        return (Date)readProperty(DATE_PROPERTY);
    }

    public void setEid(Long eid) {
        writeProperty(EID_PROPERTY, eid);
    }
    public Long getEid() {
        return (Long)readProperty(EID_PROPERTY);
    }

    public void setModified(Date modified) {
        writeProperty(MODIFIED_PROPERTY, modified);
    }
    public Date getModified() {
        return (Date)readProperty(MODIFIED_PROPERTY);
    }

    public void setSubject(String subject) {
        writeProperty(SUBJECT_PROPERTY, subject);
    }
    public String getSubject() {
        return (String)readProperty(SUBJECT_PROPERTY);
    }

    public void setType(String type) {
        writeProperty(TYPE_PROPERTY, type);
    }
    public String getType() {
        return (String)readProperty(TYPE_PROPERTY);
    }

    public void setUrl(String url) {
        writeProperty(URL_PROPERTY, url);
    }
    public String getUrl() {
        return (String)readProperty(URL_PROPERTY);
    }

}