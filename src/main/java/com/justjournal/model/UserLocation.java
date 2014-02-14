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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "user_location")
public class UserLocation implements Serializable {
    private static final long serialVersionUID = -5429965410191859512L;

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String city;

    @Column
    private String zip;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "state", nullable = true)
    private State state;

    @ManyToOne
    @JoinColumn(name = "country", nullable = true)
    private Country country;

    @Basic(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "modified", nullable = false)
    private Timestamp modified;

    @JsonCreator
    public UserLocation() {

    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(final String zip) {
        this.zip = zip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(final Timestamp modified) {
        this.modified = modified;
    }
}
