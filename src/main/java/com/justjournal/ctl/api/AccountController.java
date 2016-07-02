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
import com.justjournal.repository.*;
import com.justjournal.utility.SQLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FriendsDao friendsDao;

    @Autowired
    private UserBioDao userBioDao;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UserLinkRepository userLinkRepository;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private RssSubscriptionsDAO rssSubscriptionsDAO;

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private UserPrefRepository userPrefRepository;

    private Map<String, String>
    changePassword(String passCurrent, String passNew, HttpSession session, HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

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
        boolean result = webLogin.changePass(Login.currentLoginName(session), passCurrent, passNew);

        if (!result) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error changing password.  Did you type in your old password correctly?");
        }
        return java.util.Collections.singletonMap("status", "Password Changed.");
    }

    private Map<String, String> updateUser(User user, HttpSession session, HttpServletResponse response) {
        if (Login.currentLoginId(session) == user.getId() && Login.currentLoginName(session).equals(user.getUsername())) {

            userDao.save(user);
            return java.util.Collections.singletonMap("id", Integer.toString(user.getId()));
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return java.util.Collections.singletonMap("error", "Could not edit account.");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Map<String, String> delete(HttpServletResponse response, HttpSession session) {
        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        int userID = Login.currentLoginId(session);

        try {
            User user = userDao.findOne(userID);
            commentRepository.delete(commentRepository.findByUser(user));
            entryRepository.deleteInBatch(entryRepository.findByUser(user));
            entryRepository.flush();

            SQLHelper.executeNonQuery("DELETE FROM favorites WHERE owner=" + userID + ";");

            friendsDao.delete(userID);

            SQLHelper.executeNonQuery("DELETE FROM friends_lj WHERE id=" + userID + ";");

            rssSubscriptionsDAO.delete(rssSubscriptionsDAO.findByUser(user));

            SQLHelper.executeNonQuery("DELETE FROM user_files WHERE ownerid=" + userID + ";");

            userImageRepository.delete(userImageRepository.findByUsername(user.getUsername()));
            SQLHelper.executeNonQuery("DELETE FROM user_images_album WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_images_album_map WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_pic WHERE id=" + userID + ";");
            userPrefRepository.delete(userID);
            SQLHelper.executeNonQuery("DELETE FROM user_style WHERE id=" + userID + ";");

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

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    Map<String, String> post(@RequestParam String type, @RequestParam String passCurrent, @RequestParam String passNew, @RequestBody User user, HttpSession session, HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        if (type.equals("password")) {
            return changePassword(passCurrent, passNew, session, response);
        } else {
            return updateUser(user, session, response);
        }
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public User getByUsername(@PathVariable("username") String username, HttpSession session, HttpServletResponse response) {
        try {
            User user = userDao.findByUsername(username);

            if (user.getUserPref().getOwnerViewOnly() == PrefBool.Y) {
                if (
                        !Login.isAuthenticated(session) || user.getUsername().equals(Login.currentLoginName(session))) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return null;
                }
            }

            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
