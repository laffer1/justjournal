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
import com.justjournal.core.Settings;
import com.justjournal.db.UserDao;
import com.justjournal.db.UserTo;
import com.justjournal.utility.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/signup")
final public class SignUpController {
    private static final Logger log = Logger.getLogger(SignUpController.class);

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    Map<String, String> post(@RequestParam String email, @RequestBody UserTo user, HttpServletResponse response) {

        Settings settings = new Settings();

        if (!settings.isUserAllowNew()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "Could not add user");
        }

        if (!StringUtil.lengthCheck(email, 6, 100)) {
            throw new IllegalArgumentException("Invalid email address");
        }

        if (!WebLogin.isUserName(user.getUserName())) {

            throw new IllegalArgumentException(
                    "Username must be letters and numbers only");
        }

        if (!WebLogin.isPassword(user.getPassword())) {

            throw new IllegalArgumentException(
                    "Password must be 5-18 characters.");
        }

        return newUser(user, email, response);
    }

    private Map<String, String> newUser(UserTo user, String email, HttpServletResponse response) {

        try {
            boolean result = UserDao.add(user, email.toLowerCase());

            if (!result) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Could not add user");
            }

            user = UserDao.get(user.getUserName());

            return java.util.Collections.singletonMap("id", Integer.toString(user.getId()));
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "Could not add user");
        }
    }
}
