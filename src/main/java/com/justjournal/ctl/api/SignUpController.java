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


import com.justjournal.Login;
import com.justjournal.core.Constants;
import com.justjournal.core.Settings;
import com.justjournal.ctl.error.ErrorHandler;
import com.justjournal.model.User;
import com.justjournal.model.api.NewUser;
import com.justjournal.services.AccountService;
import com.justjournal.utility.DNSUtil;
import com.justjournal.utility.StringUtil;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Create new accounts in Just Journal. To delete accounts, use AccountController.
 *
 * @author Lucas Holt
 * @see com.justjournal.ctl.api.AccountController
 */
@Slf4j
@Transactional
@RestController
@RequestMapping("/api/signup")
public class SignUpController {

  @Autowired private AccountService accountService;

  @Autowired private Settings settings;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Map<String, String> post(
      @RequestBody final NewUser user, final HttpServletResponse response) {

    if (!settings.isUserAllowNew()) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return ErrorHandler.modelError(Constants.ERR_ADD_USER);
    }

    if (!StringUtil.lengthCheck(user.getEmail(), 3, 100)) {
      throw new IllegalArgumentException("Invalid email address");
    }

    if (!StringUtil.isEmailValid(user.getEmail())) {
      throw new IllegalArgumentException("Invalid email address.");
    }

    if (!DNSUtil.isEmailDomainValid((user.getEmail()))) {
      throw new IllegalArgumentException("Invalid email address. Domain not found.");
    }

    if (!Login.isUserName(user.getUsername())) {
      log.warn("Username used for signup is invalid: " + user.getUsername());

      throw new IllegalArgumentException("Username must be letters and numbers only");
    }

    if (!Login.isPassword(user.getPassword())) {
      log.warn("Password for signup is invalid");

      throw new IllegalArgumentException("Password must be 5-18 characters.");
    }

    return newUser(user, response);
  }

  private Map<String, String> newUser(final NewUser newUser, final HttpServletResponse response) {

    try {
      final User user = accountService.signup(newUser);

      response.setStatus(HttpServletResponse.SC_CREATED);
      return java.util.Collections.singletonMap("id", Integer.toString(user.getId()));
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return ErrorHandler.modelError(Constants.ERR_ADD_USER);
    }
  }
}
