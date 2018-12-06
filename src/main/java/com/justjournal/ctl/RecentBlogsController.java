/*-
 * Copyright (c) 2005-2011 Lucas Holt
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

import com.justjournal.core.Settings;
import com.justjournal.model.Entry;
import com.justjournal.model.Security;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.SecurityRepository;
import com.justjournal.rss.Rss;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Display recent blog entries in RSS format.
 *
 * @author Lucas Holt
 * @version $Id: RecentBlogs.java,v 1.15 2011/07/01 11:54:31 laffer1 Exp $
 */
@Slf4j
@Controller
@RequestMapping("/RecentBlogs")
public class RecentBlogsController {
   
    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private SecurityRepository securityDao;

    @Autowired
    private Settings set;

    @Autowired
    private Rss rss;

    @Cacheable("recentblogs")
    @RequestMapping(method = RequestMethod.GET, produces = "application/rss+xml")
    @ResponseBody
    public String get(final HttpServletResponse response) {
        response.setContentType("application/rss+xml;charset=UTF-8");
        response.setDateHeader("Expires", System.currentTimeMillis() + 1000 * 60);
        response.setHeader("Cache-Control", "max-age=60, private, proxy-revalidate");

        // Create an RSS object, set the required
        // properties (title, description language, url)
        // and write it to the sb output.
        try {

            final java.util.GregorianCalendar gregorianCalendar = new java.util.GregorianCalendar(java.util.TimeZone.getTimeZone("UTC"));
            gregorianCalendar.setTime(new java.util.Date());

            rss.setTitle("JJ New Posts");
            rss.setLink(set.getBaseUri());
            rss.setDescription("New blog posts on Just Journal");
            rss.setLanguage("en-us");
            rss.setCopyright("Copyright " + gregorianCalendar.get(Calendar.YEAR) + " JustJournal.com and its blog account owners.");
            rss.setWebMaster(set.getSiteAdminEmail() + " (" + set.getSiteAdmin() + ")");
            rss.setManagingEditor(set.getSiteAdminEmail() + " (" + set.getSiteAdmin() + ")");
            rss.setSelfLink(set.getBaseUri() + "RecentBlogs");

            final Security security = securityDao.findById(2).orElse(null); // public
            final Pageable pageable = new PageRequest(1,15);
            final Page<Entry> entries = entryRepository.findBySecurityOrderByDateDesc(security, pageable);

            final Map<String, Entry> map = new HashMap<>();
            int count = 0;
            for (final Entry e : entries) {
                if (count == 15)
                    break;
                if (map.containsKey(e.getUser().getUsername()))
                    continue;

                map.put(e.getUser().getUsername(), e);
                count++;
            }

            rss.populate(map.values());

            final Date d = rss.getNewestEntryDate();

            if (d != null)
                response.setDateHeader("Last-Modified", d.getTime());
            else
                response.setDateHeader("Last-Modified", System.currentTimeMillis());

            return rss.toXml();
        } catch (final Exception e) {
            // oops we goofed somewhere.  Its not in the original spec
            // how to handle error conditions with rss.
            // html back isn't good, but what do we do?
            log.debug(e.getMessage(), e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (final IOException e1) {
                log.debug(e1.getMessage(), e1);
            }
        }

        return "";
    }
}
