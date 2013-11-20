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

package com.justjournal.ctl;

import com.justjournal.db.SQLHelper;
import com.justjournal.db.Tag;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/json/Tags.json")
public class Tags {
    private static final Logger log = Logger.getLogger(Tags.class);

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Tag> getTags() {
        Collection<Tag> tags = new ArrayList<Tag>();
        try {
            // TODO: Convert to Cayenne
            ResultSet rs = SQLHelper.executeResultSet("select tags.id, tags.name as name, count(*) as count from entry_tags, tags where tags.id=entry_tags.tagid GROUP by tags.name;");

            while (rs.next()) {
                Tag tag = new Tag(rs.getInt(1), rs.getString(2));
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
