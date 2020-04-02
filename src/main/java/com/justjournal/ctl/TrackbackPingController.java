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

import com.justjournal.core.Constants;
import com.justjournal.model.Trackback;
import com.justjournal.model.TrackbackType;
import com.justjournal.services.TrackbackService;
import com.justjournal.utility.DNSUtil;
import com.justjournal.utility.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Trackback and Post-IT Pings inbound http://wellformedweb.org/story/9
 * http://archive.cweiske.de/trackback/trackback-1.2.html
 *
 * @author Lucas Holt
 */
@Slf4j
@Controller
@RequestMapping("/trackback")
public class TrackbackPingController {

    private final TrackbackService trackbackService;

    @Autowired
    public TrackbackPingController(final TrackbackService trackbackService) {
        this.trackbackService = trackbackService;
    }

    @GetMapping(produces = "text/xml")
    @ResponseBody
    public String get(@RequestParam("entryID") int entryId,
                      @RequestParam("url") String url,
                      @RequestParam(name = Constants.PARAM_TITLE, required = false) String title,
                      // post-it format title
                      @RequestParam(name = "name", required = false) String name,
                      @RequestParam(name = "blog_name", required = false) String blogName,
                      @RequestParam(name = "excerpt", required = false) String excerpt,
                      @RequestParam(name = "comment", required = false) String comment,
                      @RequestParam(name = "email", required = false) String email,
                      HttpServletResponse response) {
        try {
            response.setContentType("text/xml; charset=utf-8");
            boolean istrackback = true;

            if (entryId < 1)
                throw new IllegalArgumentException("entry id is missing");

            if (StringUtils.isEmpty(url) || !DNSUtil.isUrlDomainValid(url)) {
                throw new IllegalArgumentException("Missing required parameter \"url\"");
            }

            // todo ... validate trackback.
            // TODO: add pingback support which looks xmlrpc-ish

            final Trackback tb = new Trackback();
            if (StringUtils.isNotEmpty(title))  // trackback
                tb.setSubject(title);
            else if (StringUtils.isNotEmpty(name)) {// post it
                tb.setSubject(name);
                istrackback = false;
            }

            if (StringUtils.isNotEmpty(excerpt))
                tb.setBody(excerpt);
            else if (StringUtils.isNotEmpty(comment)) {
                tb.setBody(comment);
                istrackback = false;
            }

            if (StringUtil.isEmailValid(email) && DNSUtil.isEmailDomainValid(email))
                tb.setAuthorEmail(email);

            if (istrackback)
                tb.setType(TrackbackType.trackback);
            else
                tb.setType(TrackbackType.postit);
            // don't do pingbacks yet.

            tb.setBlogName(blogName);
            tb.setEntryId(entryId);
            tb.setUrl(url);

            trackbackService.save(tb);

            return trackbackService.generateResponse(0, null);
        } catch (final Exception e) {
            log.error("TrackbackPing failed ", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return trackbackService.generateResponse(1, e.getMessage());
        }
    }
}
