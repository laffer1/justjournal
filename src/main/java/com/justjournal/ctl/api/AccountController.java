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

import com.justjournal.User;
import com.justjournal.UserImpl;
import com.justjournal.WebLogin;
import com.justjournal.db.SQLHelper;
import com.justjournal.db.UserDao;
import com.justjournal.db.UserTo;
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
final public class AccountController {

    private Map<String, String>
    changePassword(String passCurrent, String passNew, HttpSession session, HttpServletResponse response) {

        if (!WebLogin.isAuthenticated(session)) {
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
        boolean result = WebLogin.changePass(WebLogin.currentLoginName(session), passCurrent, passNew);

        if (!result) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error changing password.  Did you type in your old password correctly?");
        }
        return java.util.Collections.singletonMap("status", "Password Changed.");
    }

    private Map<String, String> updateUser(UserTo user, HttpSession session, HttpServletResponse response) {
        if (WebLogin.currentLoginId(session) == user.getId() && WebLogin.currentLoginName(session).equals(user.getUserName())) {

            boolean result = UserDao.update(user);

            if (!result) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Could not edit account.");
            }
            return java.util.Collections.singletonMap("id", Integer.toString(user.getId()));
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return java.util.Collections.singletonMap("error", "Could not edit account.");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Map<String, String> delete(HttpServletResponse response, HttpSession session) {
        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        int userID = WebLogin.currentLoginId(session);

        try {
            SQLHelper.executeNonQuery("DELETE FROM comments WHERE uid=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM entry WHERE uid=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM favorites WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM friends WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM friends_lj WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM rss_subscriptions WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_bio WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_contact WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_files WHERE ownerid=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_images WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_images_album WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_images_album_map WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_link WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_location WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_pic WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_pref WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_style WHERE id=" + userID + ";");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return java.util.Collections.singletonMap("error", "Error deleting account");
        }
        return java.util.Collections.singletonMap("status", "Account Deleted");
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    Map<String, String> post(@RequestParam String type, @RequestParam String passCurrent, @RequestParam String passNew, @RequestBody UserTo user, HttpSession session, HttpServletResponse response) {

        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        if (type.equals("password")) {
            return changePassword(passCurrent, passNew, session, response);
        } else {
            return updateUser(user, session, response);
        }
    }

    @RequestMapping("/api/account/{id}")
    @ResponseBody
    public User getById(@PathVariable String id, HttpSession session, HttpServletResponse response) {
        try {
            UserImpl user = new UserImpl(id);

            if (user.isPrivateJournal()) {
                if (
                        !WebLogin.isAuthenticated(session) || user.getUserName().equals(WebLogin.currentLoginName(session))) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return null;
                }
            }

            return user;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
