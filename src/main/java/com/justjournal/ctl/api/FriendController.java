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
import com.justjournal.model.Friend;
import com.justjournal.model.User;
import com.justjournal.repository.FriendsDao;
import com.justjournal.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Manage Friends
 *
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/friend")
public class FriendController {
    private static final Logger log = Logger.getLogger(FriendController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsDao friendsDao;

    // TODO: refactor to return user objects?

    /**
     * @param id       username
     * @param response http response
     * @return List of usernames as strings
     */
    @Cacheable(value = "friends", key = "id")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Collection<User> getById(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            ArrayList<User> friends = new ArrayList<User>();

            User user = userRepository.findByUsername(id);
            for (Friend friend : user.getFriends()) {
                friends.add(friend.getFriend());
            }
            return friends;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @CacheEvict(value = "friends", key = "friend")
    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public Map<String, String> put(@RequestParam String friend, HttpSession session, HttpServletResponse response) {
        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            final User friendUser = userRepository.findByUsername(friend);
            final User owner = userRepository.findOne(Login.currentLoginId(session));

            if (friendUser == null)
                return java.util.Collections.singletonMap("error", "Could not find friend's username");

            if (owner == null)
                return java.util.Collections.singletonMap("error", "Could not find logged in user account.");

            final Friend f = new Friend();
            f.setFriend(friendUser);
            f.setUser(owner);
            f.setPk(owner.getId());
            if (friendsDao.save(f) != null)
                return java.util.Collections.singletonMap("status", "success");

            return java.util.Collections.singletonMap("error", "Unable to add friend");
        } catch (final Exception e) {
            log.error(e.getMessage());
            return java.util.Collections.singletonMap("error", "Could not find friend's username");
        }

    }

    // TODO: api makes it hard to selectively delete cache entries
    @CacheEvict(value = "friends", allEntries = true)
    @RequestMapping(method = RequestMethod.DELETE)
    public
    @ResponseBody
    Map<String, String> delete(@RequestBody Friend friend, HttpSession session, HttpServletResponse response) throws Exception {


        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            User user = userRepository.findOne(Login.currentLoginId(session));
            friend.setUser(user);

            friendsDao.delete(friend);
            return java.util.Collections.singletonMap("status", "success");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return java.util.Collections.singletonMap("error", "Error deleting friend");
    }
}
