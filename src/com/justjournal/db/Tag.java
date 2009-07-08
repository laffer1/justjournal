/*-
Copyright (c) 2008, Lucas Holt
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

import com.justjournal.utility.StringUtil;

/**
 * A tag is a form of metadata about a blog entry.  It is similar to a category.
 *
 * @author Lucas Holt
 * @version $Id: Tag.java,v 1.6 2009/07/08 01:13:59 laffer1 Exp $
 *          <p/>
 *          Date: Apr 25, 2008
 *          Time: 5:13:16 PM
 */
public final class Tag {
    private int id;      // Unique id to represent the global tag
    private String name; // common string representation for public consumption
    private int count;
    // timestamp comes later

    /**
     * Create a Tag with a unique id and name
     *
     * @param id   unique identity
     * @param name the user visible name
     */
    public Tag(int id, String name) {
        if (name == null || name.equalsIgnoreCase(""))
            throw new IllegalArgumentException("Name must be set");
        if (name.length() > 30)
            throw new IllegalArgumentException("Name cannot be longer than 30 characters.");
        if (!StringUtil.isAlpha(name))
            throw new IllegalArgumentException("Name contains invalid characters.  Must be A-Za-z");
        if (id < 1)
            throw new IllegalArgumentException("Tag id must be > 0");

        this.id = id;
        this.name = name.toLowerCase();
        count = 0; // default
    }

    /**
     * Create a Tag with a name.
     *
     * @param name user visible name
     */
    public Tag(String name) {
        if (name == null || name.equalsIgnoreCase(""))
            throw new IllegalArgumentException("Name must be set");
        if (name.length() > 30)
            throw new IllegalArgumentException("Name cannot be longer than 30 characters.");
        if (!StringUtil.isAlpha(name))
            throw new IllegalArgumentException("Name contains invalid characters.  Must be A-Za-z");

        this.id = 0;
        this.name = name.toLowerCase();
        count = 0; // default
    }

    /**
     * Get the unique identifier
     *
     * @return tag id > 0
     */
    public int getId() {
        return id;
    }

    /**
     * Set the unique id for the tag
     *
     * @param id tag id > 0
     */
    public void setId(int id) {
        if (id < 1)
            throw new IllegalArgumentException("Tag id must be > 0");
        this.id = id;
    }


    /**
     * The number of tags
     * @return  tag count
     */
    public int getCount() {
        return count;
    }

    /**
     * Set the number of tag instances
     * @param count number of tags
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * The common name for the tag which the user will see
     *
     * @return 30 char or less string
     */
    public String getName() {
        return name;
    }

    /**
     * Set the common name to display to the user
     *
     * @param name 30 character or less string with letters only.
     */
    public void setName(String name) {
        if (name == null || name.equalsIgnoreCase(""))
            throw new IllegalArgumentException("Name must be set");
        if (name.length() > 30)
            throw new IllegalArgumentException("Name cannot be longer than 30 characters.");

        if (!StringUtil.isAlpha(name))
            throw new IllegalArgumentException("Name contains invalid characters.  Must be A-Za-z");

        this.name = name.toLowerCase();
    }
}
