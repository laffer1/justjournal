/*
Copyright (c) 2003-2021, Lucas Holt
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

import static com.justjournal.core.Constants.*;

import com.justjournal.Login;
import com.justjournal.model.LoginResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Log user into session
 *
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

  @Autowired private com.justjournal.Login webLogin;

  /**
   * Check the login status of the user
   *
   * @param session HttpSession
   * @return LoginResponse with login OK or NONE
   */
  @GetMapping(headers = HEADER_ACCEPT_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public LoginResponse getLoginStatus(final HttpSession session) {
    final LoginResponse response = new LoginResponse();
    final String username = (String) session.getAttribute(LOGIN_ATTRNAME);
    response.setUsername(username);
    response.setStatus(username == null ? JJ_LOGIN_NONE : JJ_LOGIN_OK);
    return response;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      headers = {HEADER_ACCEPT_ALL, "content-type=application/json"})
  @ResponseBody
  public ResponseEntity<LoginResponse> post(
      @RequestBody final com.justjournal.core.Login login, final HttpServletRequest request) {
    final LoginResponse loginResponse = new LoginResponse();

    try {
      // Current authentication needs to get whacked
      HttpSession session = request.getSession(true);
      if (Login.isAuthenticated(session)) {
        session.invalidate();
        session = request.getSession(true); // reset
      }

      final int userID = webLogin.validate(login.getUsername(), login.getPassword());
      if (userID > BAD_USER_ID) {
        log.debug("LoginController.post(): Username is " + login.getUsername());
        session.setAttribute(LOGIN_ATTRID, userID);
        session.setAttribute(LOGIN_ATTRNAME, login.getUsername());
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
