package com.justjournal.model.auto;

/** Class _Entry was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _Entry extends org.apache.cayenne.CayenneDataObject {

    public static final String ALLOW_COMMENTS_PROPERTY = "allowComments";
    public static final String ATTACH_FILE_PROPERTY = "attachFile";
    public static final String ATTACH_IMAGE_PROPERTY = "attachImage";
    public static final String AUTOFORMAT_PROPERTY = "autoformat";
    public static final String BODY_PROPERTY = "body";
    public static final String DATE_PROPERTY = "date";
    public static final String DRAFT_PROPERTY = "draft";
    public static final String EMAIL_COMMENTS_PROPERTY = "emailComments";
    public static final String LOCATION_PROPERTY = "location";
    public static final String MODIFIED_PROPERTY = "modified";
    public static final String MOOD_PROPERTY = "mood";
    public static final String MUSIC_PROPERTY = "music";
    public static final String SECURITY_PROPERTY = "security";
    public static final String SUBJECT_PROPERTY = "subject";

    public static final String ID_PK_COLUMN = "id";

    public void setAllowComments(String allowComments) {
        writeProperty("allowComments", allowComments);
    }
    public String getAllowComments() {
        return (String)readProperty("allowComments");
    }
    
    
    public void setAttachFile(Long attachFile) {
        writeProperty("attachFile", attachFile);
    }
    public Long getAttachFile() {
        return (Long)readProperty("attachFile");
    }
    
    
    public void setAttachImage(Long attachImage) {
        writeProperty("attachImage", attachImage);
    }
    public Long getAttachImage() {
        return (Long)readProperty("attachImage");
    }
    
    
    public void setAutoformat(String autoformat) {
        writeProperty("autoformat", autoformat);
    }
    public String getAutoformat() {
        return (String)readProperty("autoformat");
    }
    
    
    public void setBody(String body) {
        writeProperty("body", body);
    }
    public String getBody() {
        return (String)readProperty("body");
    }
    
    
    public void setDate(java.util.Date date) {
        writeProperty("date", date);
    }
    public java.util.Date getDate() {
        return (java.util.Date)readProperty("date");
    }
    
    
    public void setDraft(String draft) {
        writeProperty("draft", draft);
    }
    public String getDraft() {
        return (String)readProperty("draft");
    }
    
    
    public void setEmailComments(String emailComments) {
        writeProperty("emailComments", emailComments);
    }
    public String getEmailComments() {
        return (String)readProperty("emailComments");
    }
    
    
    public void setLocation(Byte location) {
        writeProperty("location", location);
    }
    public Byte getLocation() {
        return (Byte)readProperty("location");
    }
    
    
    public void setModified(java.util.Date modified) {
        writeProperty("modified", modified);
    }
    public java.util.Date getModified() {
        return (java.util.Date)readProperty("modified");
    }
    
    
    public void setMood(Byte mood) {
        writeProperty("mood", mood);
    }
    public Byte getMood() {
        return (Byte)readProperty("mood");
    }
    
    
    public void setMusic(String music) {
        writeProperty("music", music);
    }
    public String getMusic() {
        return (String)readProperty("music");
    }
    
    
    public void setSecurity(Byte security) {
        writeProperty("security", security);
    }
    public Byte getSecurity() {
        return (Byte)readProperty("security");
    }
    
    
    public void setSubject(String subject) {
        writeProperty("subject", subject);
    }
    public String getSubject() {
        return (String)readProperty("subject");
    }
    
    
}
