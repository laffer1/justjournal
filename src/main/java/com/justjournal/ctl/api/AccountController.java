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
                return java.util.Collections.singletonMap("error", "Could not edit the comment.");
            }
            return java.util.Collections.singletonMap("id", Integer.toString(user.getId()));
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return java.util.Collections.singletonMap("error", "Could not edit the comment.");
    }

    @RequestMapping(method = RequestMethod.POST)
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
}
