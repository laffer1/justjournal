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

package com.justjournal.db;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * User contact information including IM accounts, email and homepage
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UserContactTo {
    private String email;
    private String icq;
    private String aim;
    private String yahoo;
    private String msn;
    private String phone;
    private String hpTitle;
    private String hpUri;

    @JsonCreator
    public UserContactTo() {

    }

    public UserContactTo(String email, String icq, String aim, String yahoo, String msn, String phone, String hpTitle, String hpUri) {
        this.email = email;
        this.icq = icq;
        this.aim = aim;
        this.yahoo = yahoo;
        this.msn = msn;
        this.phone = phone;
        this.hpTitle = hpTitle;
        this.hpUri = hpUri;
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserContactTo that = (UserContactTo) o;

        return !(aim != null ? !aim.equals(that.aim) : that.aim != null) && email.equals(that.email) && !(hpTitle != null ? !hpTitle.equals(that.hpTitle) : that.hpTitle != null) && !(hpUri != null ? !hpUri.equals(that.hpUri) : that.hpUri != null) && !(icq != null ? !icq.equals(that.icq) : that.icq != null) && !(msn != null ? !msn.equals(that.msn) : that.msn != null) && !(phone != null ? !phone.equals(that.phone) : that.phone != null) && !(yahoo != null ? !yahoo.equals(that.yahoo) : that.yahoo != null);
    }

    @Override
    public final int hashCode() {
        int result;
        result = email.hashCode();
        result = 29 * result + (icq != null ? icq.hashCode() : 0);
        result = 29 * result + (aim != null ? aim.hashCode() : 0);
        result = 29 * result + (yahoo != null ? yahoo.hashCode() : 0);
        result = 29 * result + (msn != null ? msn.hashCode() : 0);
        result = 29 * result + (phone != null ? phone.hashCode() : 0);
        result = 29 * result + (hpTitle != null ? hpTitle.hashCode() : 0);
        result = 29 * result + (hpUri != null ? hpUri.hashCode() : 0);
        return result;
    }

}
