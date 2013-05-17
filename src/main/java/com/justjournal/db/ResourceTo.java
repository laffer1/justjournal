/*
Copyright (c) 2005-2006, Lucas Holt
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
 * User: laffer1
 * Date: Jul 24, 2005
 * Time: 10:48:55 AM
 */
public final class ResourceTo {
    private int id;
    private String name;
    private boolean active;
    private int securityLevel;

    /**
     * Retrieves entry id as an int >0
     *
     * @return entry id
     */
    public final int getId() {
        return id;
    }


    /**
     * Set the entry id to an int >0
     *
     * @param id entry id to set
     * @throws IllegalArgumentException id < 0
     */
    public final void setId(int id)
            throws IllegalArgumentException {
        if (id < 0)
            throw new IllegalArgumentException("Illegal id: " +
                    id);

        this.id = id;
    }

    public final int getSecurityLevel() {
        return securityLevel;
    }

    public final void setSecurityLevel(int sec)
            throws IllegalArgumentException {
        if (sec < 0)
            throw new IllegalArgumentException("Illegal security level: " +
                    sec);
        securityLevel = sec;
    }

    public final boolean getActive() {
        return active;
    }

    public final void setActive(boolean active) {
        this.active = active;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name)
            throws IllegalArgumentException {
        if (name == null)
            throw new IllegalArgumentException("name can not be null");

        if (name.length() <= 200 && name.length() >= 1) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Illegal name length");
        }
    }

    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ResourceTo that = (ResourceTo) o;

        if (active != that.active) return false;
        if (id != that.id) return false;
        if (securityLevel != that.securityLevel) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    public final int hashCode() {
        int result;
        result = id;
        result = 29 * result + name.hashCode();
        result = 29 * result + (active ? 1 : 0);
        result = 29 * result + securityLevel;
        return result;
    }

}
