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
import com.justjournal.model.*;
import com.justjournal.model.api.PasswordChange;
import com.justjournal.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/account")
public class AccountController {

    private Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserRepository userDao;

    @Autowired
    private Login webLogin;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private FriendsRepository friendsDao;

    @Autowired
    private UserBioRepository userBioDao;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UserLinkRepository userLinkRepository;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private RssSubscriptionsRepository rssSubscriptionsDAO;

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private UserPrefRepository userPrefRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Map<String, String>
    changePassword(final PasswordChange passwordChange, HttpSession session, HttpServletResponse response) {
        final String passCurrent = passwordChange.getPassCurrent();
        final String passNew = passwordChange.getPassNew();

        if (passCurrent == null ||
                passCurrent.length() < 5 ||
                passCurrent.length() > 15) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The current password is invalid.");
        }
        if (passNew == null ||
                passNew.length() < 5 ||
                passNew.length() > 15) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The new password is invalid.");
        }

        // TODO: Refactor change pass
        final boolean result = webLogin.changePass(Login.currentLoginName(session), passCurrent, passNew);

        if (!result) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error changing password.  Did you type in your old password correctly?");
        }
        return java.util.Collections.singletonMap("status", "Password Changed.");
    }

    private Map<String, String> updateUser(final User user, final HttpSession session, final HttpServletResponse response) {
        if (Login.currentLoginId(session) == user.getId() && Login.currentLoginName(session).equals(user.getUsername())) {

            userDao.save(user);
            return java.util.Collections.singletonMap("id", Integer.toString(user.getId()));
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return java.util.Collections.singletonMap("error", "Could not edit account.");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Map<String, String> delete(final HttpServletResponse response, final HttpSession session) {
        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        final int userID = Login.currentLoginId(session);

        try {
            final User user = userDao.findOne(userID);
            commentRepository.delete(commentRepository.findByUser(user));
            entryRepository.deleteInBatch(entryRepository.findByUser(user));
            entryRepository.flush();

            favoriteRepository.deleteInBatch(favoriteRepository.findByUser(user));
            favoriteRepository.flush();

            friendsDao.delete(userID);

            jdbcTemplate.execute("DELETE FROM friends_lj WHERE id=" + userID + ";");

            rssSubscriptionsDAO.delete(rssSubscriptionsDAO.findByUser(user));

            jdbcTemplate.execute("DELETE FROM user_files WHERE ownerid=" + userID + ";");

            userImageRepository.delete(userImageRepository.findByUsername(user.getUsername()));
            jdbcTemplate.execute("DELETE FROM user_images_album WHERE owner=" + userID + ";");
            jdbcTemplate.execute("DELETE FROM user_images_album_map WHERE owner=" + userID + ";");
            jdbcTemplate.execute("DELETE FROM user_pic WHERE id=" + userID + ";");
            userPrefRepository.delete(userID);
            jdbcTemplate.execute("DELETE FROM user_style WHERE id=" + userID + ";");

            jdbcTemplate.execute("DELETE FROM journal WHERE user_id=" + userID + ";");

            userLinkRepository.delete(userID);
            userLocationRepository.delete(userID);
            userBioDao.delete(userID);
            userContactRepository.delete(userID);
            userDao.delete(user);
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return java.util.Collections.singletonMap("error", "Error deleting account");
        }
        return java.util.Collections.singletonMap("status", "Account Deleted");
    }

     @RequestMapping(value="/password", method = RequestMethod.POST, produces = "application/json")
     public
     @ResponseBody
     Map<String, String> post(@RequestBody PasswordChange passwordChange,
                              HttpSession session, HttpServletResponse response) {

         if (!Login.isAuthenticated(session)) {
             response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
             return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
         }

         return changePassword(passwordChange, session, response);
     }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    Map<String, String> post(@RequestBody final User user,
                             final HttpSession session,
                             final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        return updateUser(user, session, response);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public User getByUsername(@PathVariable("username") final String username,
                              final HttpSession session,
                              final HttpServletResponse response) {
        try {
            final User user = userDao.findByUsername(username);

            // TODO: we should handle this per journal rather than globally if one is there. Be conservative for now
            for (final Journal journal : user.getJournals()) {
                if (journal.isOwnerViewOnly()) {
                    if (
                            !Login.isAuthenticated(session) || user.getUsername().equals(Login.currentLoginName(session))) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return null;
                    }
                }
            }

            return user;
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
