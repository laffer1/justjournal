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
import com.justjournal.utility.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/login")
final public class LoginController {

    private static final String JJ_LOGIN_OK = "JJ.LOGIN.OK";
    private static final String JJ_LOGIN_FAIL = "JJ.LOGIN.FAIL";

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Model post(@RequestParam String userName, @RequestParam String password, Model model, HttpSession session) {

        // Current authentication needs to get whacked
        if (WebLogin.isAuthenticated(session)) {
            session.invalidate();
        }

        if (StringUtil.lengthCheck(userName, 3, 15) && StringUtil.lengthCheck(password, 5, 18)) {
            int userID = WebLogin.validate(userName, password);
            if (userID > 0) {
                session.setAttribute("auth.uid", userID);
                session.setAttribute("auth.user", userName);
                model.addAttribute("username", userName);
                model.addAttribute("status", JJ_LOGIN_OK);
            } else
                error(model);

        } else {
            error(model);
        }

        return model;
    }

    private void error(Model model) {
        model.addAttribute("status", JJ_LOGIN_FAIL);
        model.addAttribute("error", "Unable to login.  Please check your username and password.");

    }
}
