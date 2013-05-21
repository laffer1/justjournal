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
 * Date: Jan 19, 2004
 * Time: 12:05:31 PM
 * To change this template use Options | File Templates.
 */
public final class FriendTo {
    private int id;
    private String userName;
    private int ownerId;
    private String ownerUserName;

    public int getid() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(final int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUserName() {
        return this.ownerUserName;
    }

    public void setOwnerUserName(final String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public String toString() {
        return Integer.toString(id) + "," + userName + ","
                + ownerId + "," + ownerUserName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FriendTo friendTo = (FriendTo) o;

        if (id != friendTo.id) return false;
        if (ownerId != friendTo.ownerId) return false;
        if (!ownerUserName.equals(friendTo.ownerUserName)) return false;
        if (!userName.equals(friendTo.userName)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = id;
        result = 29 * result + userName.hashCode();
        result = 29 * result + ownerId;
        result = 29 * result + ownerUserName.hashCode();
        return result;
    }
}