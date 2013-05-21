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

package com.justjournal.ctl;

import com.justjournal.WebLogin;
import com.justjournal.db.EntryTo;
import com.justjournal.db.SQLHelper;
import com.justjournal.db.UserLinkDao;
import com.justjournal.db.UserLinkTo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/json/Link.json")
public class Link {
    private static final Logger log = Logger.getLogger(Link.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<UserLinkTo> getLink(HttpSession session) {

        return UserLinkDao.view(WebLogin.currentLoginId(session));
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, String> create(@RequestBody UserLinkTo link, HttpSession session, HttpServletResponse response) {

        try {
            UserLinkDao dao = new UserLinkDao();

            if (!dao.add(link)) {
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
    Map<String, String> delete(@RequestBody int linkId, HttpSession session, HttpServletResponse response) throws Exception {


        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        if (linkId > 0) {
            /* valid link id */
            UserLinkTo link = new UserLinkTo();
            link.setUserId(WebLogin.currentLoginId(session));
            link.setId(linkId);

            if (UserLinkDao.delete(link)) {
                return java.util.Collections.singletonMap("id", Integer.toString(linkId));
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error deleting your link.");
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return java.util.Collections.singletonMap("error", "Error deleting your link. Bad link id.");
    }
}
