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

import java.util.Arrays;

/**
 * User: laffer1
 * Date: Aug 15, 2005
 * Time: 9:39:50 PM
 */
public final class ContentTo {

    private int id;
    private int uriId;
    private int userId;
    private String mimeType;
    private boolean preferred;
    private int dataSize;
    private byte[] data;
    private String metaData;

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

    public final int getUriId() {
        return uriId;
    }

    public final void setUriId(int uriId)
            throws IllegalArgumentException {
        if (uriId < 0)
            throw new IllegalArgumentException("Illegal uriId: " + uriId);
        this.uriId = uriId;
    }

    public final int getUserId() {
        return userId;
    }

    public final void setUserId(int userId)
            throws IllegalArgumentException {
        if (userId < 0)
            throw new IllegalArgumentException("Illegal userId: " + userId);
        this.userId = userId;
    }

    public final String getMimeType() {
        return mimeType;
    }

    public final void setMimeType(String mimeType)
            throws IllegalArgumentException {
        this.mimeType = mimeType;
    }

    public final boolean getPreferred() {
        return preferred;
    }

    public final void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public final int getDataSize() {
        return dataSize;
    }

    public final void setDataSize(int dataSize)
            throws IllegalArgumentException {
        if (dataSize < 0)
            throw new IllegalArgumentException("Illegal dataSize: " + dataSize);
        this.dataSize = dataSize;
    }

    /**
     * Retrieves the body or "meat" of the content entity.
     * (should have named this differently!)
     *
     * @return body of content entity
     */
    public final byte[] getData() {
        return data;
    }

    /**
     * Sets the body of the content entity.
     *
     * @param data body of entry.
     */
    public final void setData(byte[] data) {
        this.data = data;
    }

    public final String getMetaData() {
        return metaData;
    }

    public final void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ContentTo contentTo = (ContentTo) o;

        if (dataSize != contentTo.dataSize) return false;
        if (id != contentTo.id) return false;
        if (preferred != contentTo.preferred) return false;
        if (uriId != contentTo.uriId) return false;
        if (userId != contentTo.userId) return false;
        if (!Arrays.equals(data, contentTo.data)) return false;
        return !(metaData != null ? !metaData.equals(contentTo.metaData) : contentTo.metaData != null) && !(mimeType != null ? !mimeType.equals(contentTo.mimeType) : contentTo.mimeType != null);

    }

    public final int hashCode() {
        int result;
        result = id;
        result = 29 * result + uriId;
        result = 29 * result + userId;
        result = 29 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 29 * result + (preferred ? 1 : 0);
        result = 29 * result + dataSize;
        result = 29 * result + (metaData != null ? metaData.hashCode() : 0);
        return result;
    }
}
