/*
 * Copyright (c) 2013 Lucas Holt
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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Tag management
 *
 * @author Lucas Holt
 */
@Component
final public class TagDaoImpl implements TagDao {

    private static final Logger log = Logger.getLogger(TagDaoImpl.class);

    /**
     * Get a single tag by id
     *
     * @param id tag id
     * @return tag
     */
    @Override
    @Nullable
    public Tag get(int id) {
        Tag tag = null;

        try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            com.justjournal.model.Tags tagItem =
                    Cayenne.objectForPK(dataContext, com.justjournal.model.Tags.class, id);
            tag = new TagImpl(id, tagItem.getName());
            tag.setCount(tagItem.getTagsToEntryList().size());
        } catch (Exception e1) {
            log.error(e1);
        }

        return tag;
    }

    /**
     * List all tags
     *
     * @return tag collection
     */
    @Override
    @NotNull
    public Collection<Tag> list() {
        Collection<Tag> tags = new ArrayList<Tag>();
        try {
            // TODO: Convert to Cayenne
            ResultSet rs = SQLHelper.executeResultSet("select tags.id, tags.name as name, count(*) as count from entry_tags, tags where tags.id=entry_tags.tagid GROUP by tags.name;");

            while (rs.next()) {
                Tag tag = new TagImpl(rs.getInt(1), rs.getString(2));
                tag.setCount(rs.getInt(3));
                tags.add(tag);
            }
            rs.close();
        } catch (Exception ex) {
            log.error(ex);
        }
        return tags;
    }
}
