/*
Copyright (c) 2005-2006, Lucas Holt
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
import com.justjournal.model.UserLink;
import com.justjournal.repository.UserLinkRepository;
import com.justjournal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * User Links that appear in their blog
 *
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/link")
public class LinkController {

    @Autowired
    private UserLinkRepository userLinkRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserLink getById(@PathVariable("id") final Integer id) {
        return userLinkRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "userlink", key = "#username")
    @GetMapping(value = "user/{username}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<UserLink>> getByUser(@PathVariable("username") String username) {
        final List<UserLink> links = userLinkRepository.findByUsernameOrderByTitleTitleAsc(username);

        return ResponseEntity
                .ok()
               // .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
            // TODO: etag code is not stable    .eTag(Integer.toString(links.hashCode()))
                .body(links);
    }

    @PutMapping
    @ResponseBody
    public
    Map<String, String> create(@RequestBody final UserLink link, final HttpSession session,
                               final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            link.setUser(userRepository.findById(Login.currentLoginId(session)).orElse(null));
            final UserLink l = userLinkRepository.save(link);
            return java.util.Collections.singletonMap("id", Integer.toString(l.getId()));
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error adding link.");
        }
    }

    @DeleteMapping
    @ResponseBody
    public
    Map<String, String> delete(@RequestBody final int linkId, final HttpSession session,
                               final HttpServletResponse response) {


        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        if (linkId > 0) {
            /* valid link id */
            final UserLink link = userLinkRepository.findById(linkId).orElse(null);
            if (link.getUser().getId() == Login.currentLoginId(session)) {
                userLinkRepository.deleteById(linkId);

                return java.util.Collections.singletonMap("id", Integer.toString(linkId));
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return java.util.Collections.singletonMap("error", "Error deleting your link. Bad link id.");
    }
}
