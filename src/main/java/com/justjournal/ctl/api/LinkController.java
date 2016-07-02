/*
Copyright (c) 2005-2006, Lucas Holt
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

package com.justjournal.ctl.api;

import com.justjournal.Login;
import com.justjournal.model.UserLink;
import com.justjournal.repository.UserLinkRepository;
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
 * User Links that appear in their blog
 *
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/link")
public class LinkController {
    private static final Logger log = Logger.getLogger(LinkController.class.getName());

    @Autowired
    private UserLinkRepository userLinkDao;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public UserLink getById(@PathVariable("id") Integer id) {
        return userLinkDao.findOne(id);
    }

    @Cacheable(value = "userlink", key = "#username")
    @RequestMapping(value = "user/{username}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Collection<UserLink> getByUser(@PathVariable("username") String username) {
        return userLinkDao.findByUsername(username);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public
    @ResponseBody
    Map<String, String> create(@RequestBody UserLink link, HttpSession session, HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            link.setUser(userRepository.findOne(Login.currentLoginId(session)));
            userLinkDao.save(link);
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
    Map<String, String> delete(@RequestBody int linkId, HttpSession session, HttpServletResponse response) throws Exception {


        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        if (linkId > 0) {
            /* valid link id */
            UserLink link = userLinkDao.findOne(linkId);
            if (link.getUser().getId() == Login.currentLoginId(session)) {
                userLinkDao.delete(linkId);

                return java.util.Collections.singletonMap("id", Integer.toString(linkId));
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return java.util.Collections.singletonMap("error", "Error deleting your link. Bad link id.");
    }
}
