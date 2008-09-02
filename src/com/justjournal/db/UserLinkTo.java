/*
Copyright (c) 2005, 2006, 2008 Lucas Holt
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
 * Date: Dec 22, 2005
 * Time: 2:58:39 PM
 *
 * @author Lucas Holt
 * @version $Id: UserLinkTo.java,v 1.6 2008/09/02 00:52:22 laffer1 Exp $
 */
public final class UserLinkTo {
    private int id;
    private int userId;
    private String title;
    private String uri;

    /**
     * Retrieve unique identifier for link
     * @return int > 0
     */
    public final int getId() {
        return id;
    }

    /**
     * Set unique identifier for link
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
     * Retrieve owner of link
     * @return user id
     */
    public final int getUserId() {
        return userId;
    }

    /**
     * Set the owner of the link
     * @param userId   > 0
     */
    public final void setUserId(int userId) {
        if (userId > 0)
            this.userId = userId;
        else
            throw new IllegalArgumentException("userId must be greater than zero");
    }

    /**
     * Retrieve the link title
     * @return title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Set the link title so that it can be displayed instead of just the hyperlink itself.
     * @param title ascii text
     */
    public final void setTitle(String title) {
        if (title != null)
            this.title = title;
        else
            throw new IllegalArgumentException("title cannot be null");
    }

    /**
     * Retrieve the address of the link which should be a complete URI
     * @return uri string
     */
    public final String getUri() {
        return uri;
    }

    /**
     * Set the Uniform resource identifier as a string
     * @param uri a valid uri
     */
    public final void setUri(String uri) {
        if (uri != null)
            this.uri = uri;
        else
            throw new IllegalArgumentException("uri cannot be null");
    }

    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserLinkTo that = (UserLinkTo) o;

        if (userId != that.userId) return false;
        if (id != that.id) return false;
        if (!title.equals(that.title)) return false;
        if (!uri.equals(that.uri)) return false;

        return true;
    }

    public final int hashCode() {
        int result;
        result = userId;
        result = 29 * result + id;
        result = 29 * result + title.hashCode();
        result = 29 * result + uri.hashCode();
        return result;
    }

    /**
     * Get a string representation of UserLink
     * @return comma seperated list of important components
     */
    public final String toString() {
        return id + "," + userId + "," + title + "," + uri;
    }

    public UserLinkTo(int id, int userId, String title, String uri) {
        /* calls so we execute checks and throw exceptions as needed */
        setId(id);
        setUserId(userId);
        setTitle(title);
        setUri(uri);
    }

    public UserLinkTo() {
    }
}
