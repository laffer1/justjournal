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

import com.justjournal.WebLogin;
import com.justjournal.db.*;
import com.justjournal.db.model.EntryImpl;
import com.justjournal.db.model.EntryTo;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Entry Controller, for managing blog entries
 *
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/entry")
public class EntryController {
    private static final Logger log = Logger.getLogger(EntryController.class);

    private CommentDao commentDao = null;
    private EntryDao entryDao = null;

    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public void setEntryDao(EntryDao entryDao) {
        this.entryDao = entryDao;
    }

    /**
     * Get an individual entry
     *
     * @param id entry id
     * @return entry
     */
    @RequestMapping(value = "{username}/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public EntryTo getById(@PathVariable("username") String username, @PathVariable("id") int id, HttpServletResponse response) {
        EntryTo entry = entryDao.viewSinglePublic(id);
        if (entry.getUserName().equalsIgnoreCase(username))
            return entry;

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Collection<EntryTo> getEntries(@PathVariable("username") String username) {
        return entryDao.viewAll(username, false);
    }

    /**
     * Creates a new entry resource
     *
     * @param entry    EntryTo
     * @param session  HttpSession
     * @param response HttpServletResponse
     * @return status ok or error
     */
    @CacheEvict(value = "recentblogs")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = {"Accept=*/*", "content-type=application/json"})
    public
    @ResponseBody
    Map<String, String> post(@RequestBody EntryImpl entry, HttpSession session, HttpServletResponse response, Model model) {

        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }
        entry.setUserId(WebLogin.currentLoginId(session)); // can't trust the client with this

        // TODO: validate
        boolean result = EntryDaoImpl.add(entry);

        if (!result) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not add the entry.");
        }
        model.addAttribute("status", "ok");
        // return java.util.Collections.singletonMap("id", Integer.toString(entry.getId()));
        return java.util.Collections.singletonMap("status", "OK");
    }

    /**
     * PUT generally allows for add or edit in REST.
     *
     * @param entry    User's Blog entry
     * @param session  HttpSession
     * @param response HttpServletResponse
     * @return
     */
    @CacheEvict(value = "recentblogs")
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    Map<String, String> put(@RequestBody EntryTo entry, HttpSession session, HttpServletResponse response) {
        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }
        entry.setUserId(WebLogin.currentLoginId(session)); // can't trust the client with this

        // TODO: validate
        boolean result;
        EntryTo entryTo = entryDao.viewSingle(entry.getId(), WebLogin.currentLoginId(session));
        if (entryTo != null && entryTo.getId() > 0)
            result = EntryDaoImpl.update(entry);
        else
            result = EntryDaoImpl.add(entry);

        if (!result) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not add/edit entry.");
        }
        return java.util.Collections.singletonMap("id", Integer.toString(entry.getId()));
    }

    /**
     * @param entryId  entry id
     * @param session  HttpSession
     * @param response HttpServletResponse
     * @return errors or entry id if success
     * @throws Exception
     */
    @CacheEvict(value = "recentblogs")
    @RequestMapping(method = RequestMethod.DELETE)
    public
    @ResponseBody
    Map<String, String> delete(@RequestBody int entryId, HttpSession session, HttpServletResponse response) throws Exception {

        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        if (entryId < 1)
            return Collections.singletonMap("error", "The entry id was invalid.");

        try {
            boolean result2;
            boolean result = EntryDaoImpl.delete(entryId, WebLogin.currentLoginId(session));

            if (!result) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Could not delete entry.");
            }

            result2 = commentDao.deleteByEntry(entryId);
            if (!result2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Could not delete comments associated with entry.");
            }

            return java.util.Collections.singletonMap("id", Integer.toString(entryId));
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not delete the comment.");
        }
    }
}
