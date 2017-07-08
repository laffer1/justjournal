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

import com.justjournal.Login;
import com.justjournal.model.RssSubscription;
import com.justjournal.model.User;
import com.justjournal.repository.RssSubscriptionsRepository;
import com.justjournal.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/rssreader")
public class RssReaderController {
    public static final int RSS_URL_MAX_LENGTH = 1024;
    public static final int RSS_URL_MIN_LENGTH = 10;
    private static final Logger log = Logger.getLogger(RssReaderController.class);
    @Autowired
    private RssSubscriptionsRepository rssSubscriptionsDAO;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public RssSubscription getById(@PathVariable("id") Integer id) {
        return rssSubscriptionsDAO.findOne(id);
    }

    @Cacheable(value = "rsssubscription", key = "username")
    @RequestMapping(value = "user/{username}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Collection<RssSubscription> getByUser(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username);
        return rssSubscriptionsDAO.findByUser(user);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public
    @ResponseBody
    Map<String, String> create(@RequestBody String uri, HttpSession session, HttpServletResponse response) {

        try {
            RssSubscription to = new RssSubscription();

            if (uri == null || uri.length() < RSS_URL_MIN_LENGTH || uri.length() > RSS_URL_MAX_LENGTH) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Error adding link.");
            }
            User user = userRepository.findOne(Login.currentLoginId(session));
            to.setUser(user);
            to.setUri(uri);
            rssSubscriptionsDAO.save(to);

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
        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        if (subId > 0) {
            User user = userRepository.findOne(Login.currentLoginId(session));
            RssSubscription to = rssSubscriptionsDAO.findOne(subId);

            if (user.getId() == to.getUser().getId()) {
                rssSubscriptionsDAO.delete(to);
                return java.util.Collections.singletonMap("id", Integer.toString(subId));
            }
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return java.util.Collections.singletonMap("error", "Error deleting the subscription. Bad id.");
    }
}
