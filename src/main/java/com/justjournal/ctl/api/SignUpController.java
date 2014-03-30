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
import com.justjournal.core.Settings;
import com.justjournal.model.*;
import com.justjournal.repository.UserRepository;
import com.justjournal.utility.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * @author Lucas Holt
 */
@Transactional
@Controller
@RequestMapping("/api/signup")
public class SignUpController {
    private static final Logger log = Logger.getLogger(SignUpController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Settings settings;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    Map<String, String> post(@RequestParam String email, @RequestBody User user, HttpServletResponse response) {

        if (!settings.isUserAllowNew()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "Could not add user");
        }

        if (!StringUtil.lengthCheck(email, 6, 100)) {
            throw new IllegalArgumentException("Invalid email address");
        }

        if (!Login.isUserName(user.getUsername())) {

            throw new IllegalArgumentException(
                    "Username must be letters and numbers only");
        }

        if (!Login.isPassword(user.getPassword())) {

            throw new IllegalArgumentException(
                    "Password must be 5-18 characters.");
        }

        return newUser(user, email, response);
    }

    private Map<String, String> newUser(User user, String email, HttpServletResponse response) {

        try {
            UserPref userPref = new UserPref();
            userPref.setAllowSpider(PrefBool.Y);
            userPref.setOwnerViewOnly(PrefBool.N);
            userPref.setPingServices(PrefBool.Y);
            userPref.setJournalName(user.getName() + "\'s Journal");
            user.setUserPref(userPref);

            UserContact userContact = new UserContact();
            userContact.setEmail(email);
            user.setUserContact(userContact);

            UserBio userBio = new UserBio();
            userBio.setBio("");
            user.setBio(userBio);

            userRepository.save(user);

            user = userRepository.findByUsername(user.getUsername());

            return java.util.Collections.singletonMap("id", Integer.toString(user.getId()));
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "Could not add user");
        }
    }
}
