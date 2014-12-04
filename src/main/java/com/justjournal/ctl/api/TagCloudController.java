/*
 * Copyright (c) 2014 Lucas Holt
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

package com.justjournal.ctl.api;

import com.justjournal.model.Tag;
import com.justjournal.services.EntryService;
import com.justjournal.services.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/tagcloud")
public class TagCloudController {
    private static final Logger log = Logger.getLogger(TagCloudController.class);

    @Autowired
    private EntryService entryService;

    public void setEntryService(final EntryService entryService) {
        this.entryService = entryService;
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Collection<Tag> getTags(@PathVariable("username") String username, HttpServletResponse response) {
        Collection<Tag> tags = null;
        try {
            tags = entryService.getEntryTags(username);
        } catch (ServiceException se) {
            log.error(se.getMessage());
        }

        if (tags == null || tags.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        int largest = 0;
        int smallest = 10;
        int cutSmall;
        int cutLarge;

        for (final Tag tag : tags) {
            if (tag.getCount() > largest)
                largest = tag.getCount();

            if (tag.getCount() < smallest)
                smallest = tag.getCount();
        }

        cutSmall = largest / 3;
        cutLarge = cutSmall * 2;

        for (final Tag tag : tags) {
            if (tag.getCount() > cutLarge)
                tag.setType("TagCloudLarge");
            else if (tag.getCount() < cutSmall)
                tag.setType("TagCloudSmall");
            else
                tag.setType("TagCloudMedium");
        }

        return tags;
    }
}
