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
import com.justjournal.ctl.error.ErrorHandler;
import com.justjournal.model.Journal;
import com.justjournal.model.User;
import com.justjournal.model.api.PasswordChange;
import com.justjournal.repository.CommentRepository;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.FavoriteRepository;
import com.justjournal.repository.FriendsRepository;
import com.justjournal.repository.RssSubscriptionsRepository;
import com.justjournal.repository.UserBioRepository;
import com.justjournal.repository.UserContactRepository;
import com.justjournal.repository.UserImageRepository;
import com.justjournal.repository.UserLinkRepository;
import com.justjournal.repository.UserLocationRepository;
import com.justjournal.repository.UserPrefRepository;
import com.justjournal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

/**
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private Login webLogin;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private FriendsRepository friendsDao;

    @Autowired
    private UserBioRepository userBioDao;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UserLinkRepository userLinkRepository;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private RssSubscriptionsRepository rssSubscriptionsDAO;

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private UserPrefRepository userPrefRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Map<String, String>
    changePassword(final PasswordChange passwordChange, final HttpSession session, final HttpServletResponse response) {
        final String passCurrent = passwordChange.getPassCurrent();
        final String passNew = passwordChange.getPassNew();

        if (passCurrent == null ||
                passCurrent.length() < 5 ||
                passCurrent.length() > 18) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError(  "The current password is invalid.");
        }
        if (passNew == null ||
                passNew.length() < 5 ||
                passNew.length() > 18) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError(  "The new password is invalid.");
        }

        // TODO: Refactor change pass
        final boolean result = webLogin.changePass(Login.currentLoginName(session), passCurrent, passNew);

        if (!result) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError(  "Error changing password.  Did you type in your old password correctly?");
        }
        return java.util.Collections.singletonMap("status", "Password Changed.");
    }

    private Map<String, String> updateUser(final User user, final HttpSession session, final HttpServletResponse response) {
        if (Login.currentLoginId(session) == user.getId() && Login.currentLoginName(session).equals(user.getUsername())) {

            userDao.save(user);
            return java.util.Collections.singletonMap("id", Integer.toString(user.getId()));
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return ErrorHandler.modelError(  "Could not edit account.");
    }

    @DeleteMapping
    public Map<String, String> delete(final HttpServletResponse response, final HttpSession session) {
        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ErrorHandler.modelError(  "The login timed out or is invalid.");
        }

        final int userID = Login.currentLoginId(session);

        try {
            final Optional<User> user = userDao.findById(userID);
            if (!user.isPresent()) {
                throw new RuntimeException("User should always exist at this point");
            }
            commentRepository.deleteAll(commentRepository.findByUser(user.get()));
            entryRepository.deleteInBatch(entryRepository.findByUser(user.get()));
            entryRepository.flush();

            favoriteRepository.deleteInBatch(favoriteRepository.findByUser(user.get()));
            favoriteRepository.flush();

            friendsDao.deleteById(userID);

            jdbcTemplate.execute("DELETE FROM friends_lj WHERE id=" + userID + ";");

            rssSubscriptionsDAO.deleteAll(rssSubscriptionsDAO.findByUser(user.get()));

            jdbcTemplate.execute("DELETE FROM user_files WHERE ownerid=" + userID + ";");

            userImageRepository.deleteAll(userImageRepository.findByUsername(user.get().getUsername()));
            jdbcTemplate.execute("DELETE FROM user_images_album WHERE owner=" + userID + ";");
            jdbcTemplate.execute("DELETE FROM user_images_album_map WHERE owner=" + userID + ";");
            jdbcTemplate.execute("DELETE FROM user_pic WHERE id=" + userID + ";");
            userPrefRepository.deleteById(userID);
            jdbcTemplate.execute("DELETE FROM user_style WHERE id=" + userID + ";");

            jdbcTemplate.execute("DELETE FROM journal WHERE user_id=" + userID + ";");

            userLinkRepository.deleteById(userID);
            userLocationRepository.deleteById(userID);
            userBioDao.deleteById(userID);
            userContactRepository.deleteById(userID);
            userDao.deleteById(userID);
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorHandler.modelError(  "Error deleting account");
        }
        return java.util.Collections.singletonMap("status", "Account Deleted");
    }

    @PostMapping(value = "/password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> post(@RequestBody final PasswordChange passwordChange,
                                    final HttpSession session, final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError(  "The login timed out or is invalid.");
        }

        return changePassword(passwordChange, session, response);
    }

    @PostMapping(produces = "application/json")
    @ResponseBody
    public Map<String, String> post(@RequestBody final User user,
                                    final HttpSession session,
                                    final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError(  "The login timed out or is invalid.");
        }

        return updateUser(user, session, response);
    }

    @GetMapping(value = "{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User getByUsername(@PathVariable("username") final String username,
                              final HttpSession session,
                              final HttpServletResponse response) {
        try {
            final User user = userDao.findByUsername(username);

            // TODO: we should handle this per journal rather than globally if one is there. Be conservative for now
            for (final Journal journal : user.getJournals()) {
                if (journal.isOwnerViewOnly() &&
                        (!Login.isAuthenticated(session) || user.getUsername().equals(Login.currentLoginName(session)))) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return null;
                }
            }

            return user;
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
