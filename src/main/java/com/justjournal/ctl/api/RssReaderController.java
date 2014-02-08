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

package com.justjournal.ctl.api;

import com.justjournal.WebLogin;
import com.justjournal.db.RssSubscriptionsDAO;
import com.justjournal.db.model.RssSubscriptionsTO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/rssreader")
public class RssReaderController {
    private static final Logger log = Logger.getLogger(RssReaderController.class);
    public static final int RSS_URL_MAX_LENGTH = 1024;
    public static final int RSS_URL_MIN_LENGTH = 10;

    @RequestMapping(method = RequestMethod.PUT)
    public
    @ResponseBody
    Map<String, String> create(@RequestBody String uri, HttpSession session, HttpServletResponse response) {

        try {
            RssSubscriptionsTO to = new RssSubscriptionsTO();
            boolean result;

            if (uri == null || uri.length() < RSS_URL_MIN_LENGTH || uri.length() > RSS_URL_MAX_LENGTH) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Error adding link.");
            }
            to.setId(WebLogin.currentLoginId(session));
            to.setUri(uri);
            result = RssSubscriptionsDAO.add(to);

            if (!result) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Error adding link.");
            }
            return java.util.Collections.singletonMap("id", ""); // XXX
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error adding link.");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public
    @ResponseBody
    Map<String, String> delete(@RequestBody int subId, HttpSession session, HttpServletResponse response) throws Exception {
        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        if (subId > 0) {
            RssSubscriptionsTO to = new RssSubscriptionsTO();
            to.setId(WebLogin.currentLoginId(session));
            to.setSubscriptionId(subId);

            if (RssSubscriptionsDAO.delete(to)) {
                return java.util.Collections.singletonMap("id", Integer.toString(subId));
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error deleting the subscription.");
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return java.util.Collections.singletonMap("error", "Error deleting the subscription. Bad id.");
    }
}
