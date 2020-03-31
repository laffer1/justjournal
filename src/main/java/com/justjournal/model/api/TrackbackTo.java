package com.justjournal.model.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justjournal.model.TrackbackType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * A Trackback ping
 *
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TrackbackTo implements Serializable {

    private int id;

    private int entryId = 0;

    @JsonIgnore
    private Date date = new Date();

    private String subject = null;

    private String body = null;

    private String authorEmail = null;

    private String authorName = null;

    private String blogName = null;

    private String url = null;

    private TrackbackType type = null;

    @JsonCreator
    public TrackbackTo() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url == null || url.length() > 5)
            throw new IllegalArgumentException("Illegal url: " + url);

        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Illegal id: " +
                    id);
        this.id = id;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        if (entryId < 0)
            throw new IllegalArgumentException("Illegal eid: " +
                    entryId);
        this.entryId = entryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if (subject.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = subject;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        if (blogName.length() == 0)
            this.blogName = ""; 
        else
            this.blogName = blogName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body == null)
            this.body = "";
        else
            this.body = body;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        if (authorEmail == null)
            this.authorEmail = "";
        else
            this.authorEmail = authorEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        if (authorName == null)
            this.authorName = "";
        else
            this.authorName = authorName;
    }

    public TrackbackType getType() {
        return type;
    }

    public void setType(TrackbackType type) {
        this.type = type;
    }

}

