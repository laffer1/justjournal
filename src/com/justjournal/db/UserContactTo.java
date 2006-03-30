/*
Copyright (c) 2005, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package com.justjournal.db;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 21, 2004
 * Time: 12:59:36 PM
 */
public final class UserContactTo {
    private String email;
    private String icq;
    private String aim;
    private String yahoo;
    private String msn;
    private String phone;
    private String hpTitle;
    private String hpUri;


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

    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserContactTo that = (UserContactTo) o;

        if (aim != null ? !aim.equals(that.aim) : that.aim != null) return false;
        if (!email.equals(that.email)) return false;
        if (hpTitle != null ? !hpTitle.equals(that.hpTitle) : that.hpTitle != null) return false;
        if (hpUri != null ? !hpUri.equals(that.hpUri) : that.hpUri != null) return false;
        if (icq != null ? !icq.equals(that.icq) : that.icq != null) return false;
        if (msn != null ? !msn.equals(that.msn) : that.msn != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (yahoo != null ? !yahoo.equals(that.yahoo) : that.yahoo != null) return false;

        return true;
    }

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
