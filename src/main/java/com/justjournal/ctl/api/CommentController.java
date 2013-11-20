/*
Copyright (c) 2005, Lucas Holt
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

import com.justjournal.User;
import com.justjournal.WebLogin;
import com.justjournal.db.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/comment")
final public class CommentController {
    private static final Logger log = Logger.getLogger(CommentController.class);

    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    public
    @ResponseBody
    List<CommentTo> getComments(@RequestParam Integer entryId, HttpServletResponse response) throws Exception {
        EntryTo entry = EntryDAO.viewSingle(entryId, false);

        try {
            User user = new User(entry.getUserName());
            if (user.isPrivateJournal() ||
                    !entry.getAllowComments() ||
                    entry.getSecurityLevel() == 0) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return CommentDao.view(entryId);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public
    @ResponseBody
    Map<String, String> delete(@RequestBody CommentTo comment, HttpSession session, HttpServletResponse response) throws Exception {

        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            boolean result = CommentDao.delete(comment.getId(), WebLogin.currentLoginId(session));

            if (!result) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Error deleting your comment.");
            }
            return java.util.Collections.singletonMap("id", Integer.toString(comment.getId()));
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not delete the comment.");
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces="application/json")
    public
    @ResponseBody
    Map<String, String> update(@RequestBody CommentTo comment, HttpSession session, HttpServletResponse response) {

        try {
            CommentDao commentDao = new CommentDao();
            comment.setUserId(WebLogin.currentLoginId(session));
            boolean result = commentDao.update(comment);

            // TODO: old code sanitized single tick in strings.

           // TODO: check owner matches comment.

            if (!result) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Error editing comment.");
            }
            return java.util.Collections.singletonMap("id", Integer.toString(comment.getId()));
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not add the favorite.");
        }
    }

}
