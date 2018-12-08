package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Lucas Holt
 */
@Entity
@Table(name = "user_pic")
public class UserPic implements Serializable {

    private static final long serialVersionUID = -180763510438968573L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /*
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "owner")
    private User user;
    */

    @Column(name = "mimetype", length = 75, nullable = false)
    private String mimeType;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date_modified")
    private Date modified;

    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] image;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(final Date modified) {
        this.modified = modified;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(final byte[] image) {
        this.image = image;
    }
}
