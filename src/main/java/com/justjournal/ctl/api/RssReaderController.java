/*
 * Copyright (c) 2003-2021 Lucas Holt
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

import static com.justjournal.core.Constants.PARAM_ID;
import static com.justjournal.core.Constants.PARAM_USERNAME;

import com.justjournal.Login;
import com.justjournal.core.Constants;
import com.justjournal.ctl.error.ErrorHandler;
import com.justjournal.model.RssSubscription;
import com.justjournal.model.User;
import com.justjournal.repository.RssSubscriptionsRepository;
import com.justjournal.repository.UserRepository;
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/** @author Lucas Holt */
@Log4j2
@RestController
@RequestMapping("/api/rssreader")
public class RssReaderController {
  public static final int RSS_URL_MAX_LENGTH = 1024;
  public static final int RSS_URL_MIN_LENGTH = 10;

  private final RssSubscriptionsRepository rssSubscriptionsDAO;

  private final UserRepository userRepository;

  @Autowired
  public RssReaderController(
      final RssSubscriptionsRepository rssSubscriptionsDAO, final UserRepository userRepository) {
    this.rssSubscriptionsDAO = rssSubscriptionsDAO;
    this.userRepository = userRepository;
  }

  @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public RssSubscription getById(@PathVariable(PARAM_ID) Integer id) {
    return rssSubscriptionsDAO.findById(id).orElse(null);
  }

  @Cacheable(value = "rsssubscription", key = "username")
  @GetMapping(value = "user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Collection<RssSubscription> getByUser(@PathVariable(PARAM_USERNAME) String username) {
    final User user = userRepository.findByUsername(username);
    return rssSubscriptionsDAO.findByUser(user);
  }

  @PutMapping
  @ResponseBody
  public Map<String, String> create(
      @RequestBody final String uri,
      final HttpSession session,
      final HttpServletResponse response) {

    try {
      final RssSubscription to = new RssSubscription();

      if (uri == null || uri.length() < RSS_URL_MIN_LENGTH || uri.length() > RSS_URL_MAX_LENGTH) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return ErrorHandler.modelError("Error adding link.");
      }

      final User user = userRepository.findById(Login.currentLoginId(session)).orElse(null);
      to.setUser(user);
      to.setUri(uri);
      rssSubscriptionsDAO.save(to);

      return java.util.Collections.singletonMap("id", ""); // XXX
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return ErrorHandler.modelError("Error adding link.");
    }
  }

  @DeleteMapping
  @ResponseBody
  public Map<String, String> delete(
      @RequestBody final int subId, final HttpSession session, final HttpServletResponse response) {
    if (!Login.isAuthenticated(session)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return ErrorHandler.modelError(Constants.ERR_INVALID_LOGIN);
    }

    if (subId > 0) {
      final User user = userRepository.findById(Login.currentLoginId(session)).orElse(null);
      final RssSubscription to = rssSubscriptionsDAO.findById(subId).orElse(null);

      if (user != null && to != null && user.getId() == to.getUser().getId()) {
        rssSubscriptionsDAO.delete(to);
        return java.util.Collections.singletonMap("id", Integer.toString(subId));
      }
    }

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return ErrorHandler.modelError("Error deleting the subscription. Bad id.");
  }
}
