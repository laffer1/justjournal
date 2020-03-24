/*
Copyright (c) 2006,2008 Lucas Holt
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

package com.justjournal.ctl;

import com.justjournal.model.Trackback;
import com.justjournal.model.TrackbackType;
import com.justjournal.repository.TrackbackRepository;
import com.justjournal.utility.ServletUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Trackback and Post-IT Pings inbound http://wellformedweb.org/story/9
 *
 * @author Lucas Holt
 * @version $Id: TrackbackPing.java,v 1.5 2009/05/16 03:13:12 laffer1 Exp $ User: laffer1 Date: Aug 10, 2006 Time:
 *          8:25:03 PM
 */
@Slf4j
@Controller
@RequestMapping("/trackback")
public class TrackbackPingController {

    private static final String XML_HEADER =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static final String RESPONSE = "<response>";
    private static final String END_RESPONSE = "</response>";
    private static final String ERROR = "<error>";
    private static final String END_ERROR = "</error>";
    private static final String MESSAGE = "<message>";
    private static final String END_MESSAGE = "</message>";

    private final TrackbackRepository trackbackDao;

    @Autowired
    public TrackbackPingController(TrackbackRepository trackbackDao) {
        Assert.notNull(trackbackDao, "TrackbackDao must not be null");
        this.trackbackDao = trackbackDao;
    }

    @GetMapping(produces = "text/xml")
    @ResponseBody
    public String get(HttpServletRequest request, HttpServletResponse response) {
        final StringBuilder sb = new StringBuilder();

        try {
            response.setContentType("text/xml; charset=utf-8");
            Boolean istrackback = true;

            int postId = ServletUtilities.getIntParameter(request, "entryID", 0);
            if (postId > 1)
                throw new IllegalArgumentException("entry id is missing");

            String url = request.getParameter("url");
            if (url == null || url.length() < 1) {
                throw new IllegalArgumentException("Missing required parameter \"url\"");
            }
            String title = request.getParameter("title");
            String name = request.getParameter("name");  // post-it format title
            String blogName = request.getParameter("blog_name");
            String excerpt = request.getParameter("excerpt");
            String comment = request.getParameter("comment");
            String email = request.getParameter("email");

            // todo ... validate trackback.
            // TODO: add pingback support which looks xmlrpc-ish


            Trackback tb = new Trackback();
            if (title != null && title.length() > 0)  // trackback
                tb.setSubject(title);
            else if (name != null && name.length() > 0) {// post it
                tb.setSubject(name);
                istrackback = false;
            }

            if (excerpt != null && excerpt.length() > 0)
                tb.setBody(excerpt);
            else if (comment != null && comment.length() > 0) {
                tb.setBlogName(blogName);
                istrackback = false;
            }

            if (email != null && email.length() > 0)
                tb.setAuthorEmail(email);  // TODO: validate

            if (istrackback.booleanValue())
                tb.setType(TrackbackType.trackback);
            else
                tb.setType(TrackbackType.postit); // don't do pingbacks yet.  http://wellformedweb.org/story/9#ping_back_note

            tb.setEntryId(postId);

            final java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
            tb.setDate(now);

            trackbackDao.save(tb);

            sb.append(XML_HEADER);
            sb.append(RESPONSE);
            sb.append(ERROR);
            sb.append(0);
            sb.append(END_ERROR);
            sb.append(END_RESPONSE);

        } catch (final Exception e) {
            log.error("TrackbackPing exception: " + e.getMessage());
            sb.delete(0, sb.length() - 1);
            sb.append(XML_HEADER);
            sb.append(RESPONSE);
            sb.append(ERROR);
            sb.append(1);
            sb.append(END_ERROR);
            sb.append(MESSAGE);
            sb.append(e.getMessage());
            sb.append(END_MESSAGE);
            sb.append(END_RESPONSE);

            response.setStatus(500);
        }

        return sb.toString();
    }
}
