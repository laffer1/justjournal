/*
 * Copyright (c) 2004, 2005, 2014 Lucas Holt
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
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User contact information including IM accounts, email and homepage
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "user_contact")
public class UserContact implements Serializable {
    private static final long serialVersionUID = 1152818084136626047L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String email;

    @Column
    private String icq;

    @Column
    private String aim;

    @Column
    private String yahoo;

    @Column
    private String msn;

    @Column
    private String phone;

    @Column(name = "hp_title")
    private String hpTitle;

    @Column(name = "hp_uri")
    private String hpUri;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonCreator
    public UserContact() {

    }

    public final String getEmail() {
        return this.email;
    }

    public final void setEmail(final String email) {
        this.email = email;
    }

    public final String getIcq() {
        return this.icq;
    }

    public final void setIcq(final String icq) {
        this.icq = icq;
    }

    public final String getAim() {
        return this.aim;
    }

    public final void setAim(final String aim) {
        this.aim = aim;
    }

    public final String getYahoo() {
        return this.yahoo;
    }

    public final void setYahoo(final String yahoo) {
        this.yahoo = yahoo;
    }

    public final String getMsn() {
        return this.msn;
    }

    public final void setMsn(final String msn) {
        this.msn = msn;
    }

    public final String getPhone() {
        return this.phone;
    }

    public final void setPhone(final String phone) {
        this.phone = phone;
    }

    public final String getHpTitle() {
        return this.hpTitle;
    }

    public final void setHpTitle(final String hpTitle) {
        this.hpTitle = hpTitle;
    }

    public final String getHpUri() {
        return this.hpUri;
    }

    public final void setHpUri(final String hpUri) {
        this.hpUri = hpUri;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
