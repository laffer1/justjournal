package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Style sheet theme entries.
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "style")
public class Style implements Serializable {

    private static final long serialVersionUID = -1788221747557111756L;
    /**
     * Unique id to represent the global style
     */
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, length = 30)
    private String title = "";

    @Column(name = "desc", nullable = false, length = 255)
    private String description;

    @Column(name = "modified")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modified;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(final Date modified) {
        this.modified = modified;
    }
}