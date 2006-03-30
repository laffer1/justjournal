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

package com.justjournal.content;

import com.justjournal.db.DateTime;

import java.util.Collection;

/**
 * A generic content interface.  Could represent a journal entry, news
 * article, user comment, etc.
 * <p/>
 * User: laffer1
 * Date: Jun 2, 2005
 * Time: 10:02:52 PM
 *
 * @author Lucas Holt
 * @version 1.0
 */
public interface Content {

    /**
     * Retrieves a string representation of the content
     * entities title.
     *
     * @return Title of the content entity
     */
    public String getTitle();

    /**
     * Sets the title of the content entity
     *
     * @param title The title of the content entity
     */
    public void setTitle(String title);

    /**
     * Retrieves the body or "meat" of the content entity.
     * (should have named this differently!)
     *
     * @return body of content entity
     */
    public Byte[] getContent();

    /**
     * Sets the body of the content entity.
     *
     * @param content body of entry.
     */
    public void setContent(Byte[] content);

    /**
     * Sets the format of the content property.  Valid
     * examples might be "plain" "text" "xml" "binary"
     * or maybe content types depending on the implementation.
     *
     * @return Identifier of the type of content.
     */
    public String getFormat();

    /**
     * Set the format of the content property.  Required
     * value varies by implemenation.
     *
     * @param format
     */
    public void setFormat(String format);

    /**
     * The author of the content entity.
     *
     * @return A string representation of the author's name
     */
    public String getAuthor();

    /**
     * Sets the authors name.
     *
     * @param author Name of author
     */
    public void setAuthor(String author);

    /**
     * Retrieve the date the item was created.
     *
     * @return Date created as a DateTimeBean.. format varies by
     *         implementation.
     */
    public DateTime getDateCreated();

    /**
     * Sets the date the item was created.  Format varies
     * by implemenatation.
     *
     * @param dateCreated date as DateTime item was created.
     */
    public void setDateCreated(DateTime dateCreated);

    /**
     * Retrieves the last modified date for the item.
     *
     * @return string version of date modfied.  Varies by
     *         implemenation in date format.
     */
    public DateTime getDateModified();

    /**
     * Sets the date the item was last modified.  Varies
     * by implemnation in format.
     *
     * @param dateModified date as string of last modification.
     */
    public void setDateModified(DateTime dateModified);

    /**
     * Retrieve additional properties of the meta data.  Follows the
     * <code>MetaData</code> interface.
     *
     * @return MetaData formatted name/value pair.
     */
    public Collection getMetaData();

    /**
     * Sets a colletion of properties in the <code>MetaData</code> interface.
     *
     * @param metaData MetaData collection of name/value pairs.
     */
    public void setMetaData(Collection metaData);

}