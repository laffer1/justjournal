/*
 * Copyright (c) 2013, 2014 Lucas Holt
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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justjournal.Login;
import com.justjournal.model.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Log user into session
 *
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private static final String JJ_LOGIN_OK = "JJ.LOGIN.OK";
    private static final String JJ_LOGIN_FAIL = "JJ.LOGIN.FAIL";
    private static final String JJ_LOGIN_NONE = "JJ.LOGIN.NONE";

    @Autowired
    private com.justjournal.Login webLogin;

    /**
     * Check the login status of the user
     *
     * @param session HttpSession
     * @return LoginResponse with login OK or NONE
     */
    @GetMapping(headers = "Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public LoginResponse getLoginStatus(final HttpSession session) {
        final LoginResponse response = new LoginResponse();
        final String username = (String) session.getAttribute("auth.user");
        response.setUsername(username);
        response.setStatus(username == null ? JJ_LOGIN_NONE : JJ_LOGIN_OK);
        return response;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, headers = {"Accept=*/*", "content-type=application/json"})
    @ResponseBody
    public
    ResponseEntity<LoginResponse> post(@RequestBody final com.justjournal.core.Login login,
                                       final HttpServletRequest request) {
        final LoginResponse loginResponse = new LoginResponse();

        try {
            // Current authentication needs to get whacked
            HttpSession session = request.getSession(true);
            if (Login.isAuthenticated(session)) {
                session.invalidate();
                session = request.getSession(true); // reset
            }

            final int userID = webLogin.validate(login.getUsername(), login.getPassword());
            if (userID > Login.BAD_USER_ID) {
                log.debug("LoginController.post(): Username is " + login.getUsername());
                session.setAttribute("auth.uid", userID);
                session.setAttribute("auth.user", login.getUsername());
            } else {
                log.error("Login attempt failed with user: " + login.getUsername());

                loginResponse.setStatus(JJ_LOGIN_FAIL);
                return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
            }

            loginResponse.setUsername(login.getUsername());
            loginResponse.setStatus(JJ_LOGIN_OK);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
