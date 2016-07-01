/*
 * Copyright (c) 2014 Lucas Holt
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

package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * User Images
 *
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "user_images")
public class UserImage implements Serializable {
    private static final long serialVersionUID = 6356304916167520629L;
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "title", length = 150, nullable = true)
    private String title;

    @JsonBackReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner")
    private User user;

    @Column(name = "mimetype", length = 45, nullable = false)
    private String mimeType;

    @Column(name = "modified")
    private Date modified;

    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] image;

    @JsonCreator
    public UserImage() {
    }

    /**
     * Retrieve unique identifier for image
     *
     * @return int > 0
     */
    public final int getId() {
        return id;
    }

    /**
     * Set unique identifier for image
     *
     * @param id > 0
     */
    public final void setId(int id) {
        if (id > 0)
            this.id = id;
        else
            throw new IllegalArgumentException("id must be greater than zero");
    }

    /**
     * Retrieve the image title
     *
     * @return title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Set the image title so that it can be displayed instead of just the image itself.
     *
     * @param title ascii text
     */
    public final void setTitle(String title) {
        if (title != null)
            this.title = title;
        else
            throw new IllegalArgumentException("title cannot be null");
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
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
