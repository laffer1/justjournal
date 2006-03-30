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
 * Date: Jan 3, 2004
 * Time: 11:21:22 PM
 * To change this template use Options | File Templates.
 */
public final class BioTo {
    private int userId;
    private String bio;

    public final int getUserId() {
        return this.userId;
    }

    public final void setUserId(int userId) {
        this.userId = userId;
    }

    public final String getBio() {
        return this.bio;
    }

    public final void setBio(String bio) {
        this.bio = bio;
    }

    public final String toString() {
        StringBuffer output = new StringBuffer();

        output.append("user id: ");
        output.append(userId);
        output.append('\n');

        output.append("bio: ");
        output.append(bio);
        output.append('\n');

        return output.toString();
    }

    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final BioTo bioTo = (BioTo) o;

        if (userId != bioTo.userId) return false;
        if (!bio.equals(bioTo.bio)) return false;

        return true;
    }

    public final int hashCode() {
        int result;
        result = userId;
        result = 29 * result + bio.hashCode();
        return result;
    }
}
