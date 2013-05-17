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
package com.justjournal;

import com.justjournal.db.EntryDAO;
import com.justjournal.rss.Rss;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Display recent blog entries in RSS format.
 * @author Lucas Holt
 * @version $Id: RecentBlogs.java,v 1.15 2011/07/01 11:54:31 laffer1 Exp $
 */
public class RecentBlogs extends JustJournalBaseServlet {
    private static final Logger log = Logger.getLogger(RecentBlogs.class);

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {
        response.setContentType("application/rss+xml;charset=UTF-8");
        response.setDateHeader("Expires", System.currentTimeMillis() + 1000 * 60);
        response.setHeader("Cache-Control", "max-age=60, private, proxy-revalidate");

        // Create an RSS object, set the required
        // properties (title, description language, url)
        // and write it to the sb output.
        try {
            Rss rss = new Rss();

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
            rss.populate(EntryDAO.viewRecentUniqueUsers());

            Date d = rss.getNewestEntryDate();

            if (d != null)
                response.setDateHeader("Last-Modified", d.getTime());
            else
                response.setDateHeader("Last-Modified", System.currentTimeMillis());

            sb.append(rss.toXml());
        } catch (Exception e) {
            // oops we goofed somewhere.  Its not in the original spec
            // how to handle error conditions with rss.
            // html back isn't good, but what do we do?
            log.debug(e);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                log.debug(e1);
            }
            //WebError.Display("RSS ERROR", "Unable to retrieve RSS content.", sb);
        }

    }
}
