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

package com.justjournal.ctl.api.entry;

import com.justjournal.Login;
import com.justjournal.core.Constants;
import com.justjournal.ctl.error.ErrorHandler;
import com.justjournal.exception.ServiceException;
import com.justjournal.model.Comment;
import com.justjournal.model.Entry;
import com.justjournal.model.Location;
import com.justjournal.model.Mood;
import com.justjournal.model.RecentEntry;
import com.justjournal.model.Security;
import com.justjournal.model.User;
import com.justjournal.model.api.EntryTo;
import com.justjournal.repository.CommentRepository;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.LocationRepository;
import com.justjournal.repository.MoodRepository;
import com.justjournal.repository.SecurityRepository;
import com.justjournal.repository.UserRepository;
import com.justjournal.services.EntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static com.justjournal.core.Constants.*;

/**
 * Entry Controller, for managing blog entries
 *
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/entry")
public class EntryController {

    private static final int DEFAULT_SIZE = 20;

    @Qualifier("commentRepository")
    @Autowired
    private CommentRepository commentDao = null;

    @Qualifier("entryRepository")
    @Autowired
    private EntryRepository entryRepository = null;

    @Qualifier("securityRepository")
    @Autowired
    private SecurityRepository securityDao;

    @Qualifier("locationRepository")
    @Autowired
    private LocationRepository locationDao;

    @Qualifier("moodRepository")
    @Autowired
    private MoodRepository moodDao;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryService entryService;

    /**
     * Get the private list of recent blog entries. If logged in, get the private list otherwise only public entries.
     * /api/entry/{username}/recent
     *
     * @param username username
     * @param session  HttpSession
     * @return list of recent entries
     */
    @GetMapping(value = "{username}/recent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Iterable<RecentEntry>> getRecentEntries(@PathVariable(PARAM_USERNAME) final String username,
                                                                                            final HttpSession session) {
        final Flux<RecentEntry> entries;
        try {
            if (Login.isAuthenticated(session) && Login.isUserName(username)) {
                entries = entryService.getRecentEntries(username);
            } else {
                entries = entryService.getRecentEntriesPublic(username);
            }

            final Iterable<RecentEntry> e = entries.toIterable();

            return ResponseEntity
                    .ok()
                    //     .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                    .eTag(Integer.toString(e.hashCode()))
                    .body(e);
        } catch (final ServiceException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "{username}/size/{size}/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PagedModel<EntryTo> getEntries(@PathVariable(PARAM_USERNAME) final String username,
                                          @PathVariable(PARAM_SIZE) final int size,
                                          @PathVariable(PARAM_PAGE) final int page,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response, final HttpSession session) {
        final Page<Entry> entries;
        final Pageable pageable = PageRequest.of(page, size, Sort.by(
                new Sort.Order(Sort.Direction.DESC, "date")
        ));
        try {
            if (Login.isAuthenticated(session) && Login.isUserName(username)) {
                entries = entryService.getEntries(username, pageable);
            } else {
                entries = entryService.getPublicEntries(username, pageable);
            }

            if (entries == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }

            final PagedModel.PageMetadata metadata =
                    new PagedModel.PageMetadata(entries.getSize(), entries.getNumber(), entries.getTotalElements(),
                            entries.getTotalPages());

            final Link link = new Link(request.getRequestURI());
            return new PagedModel<>(entries.stream().map(Entry::toEntryTo).collect(Collectors.toList()), metadata, link);
        } catch (final ServiceException e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @GetMapping(value = "{username}/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PagedModel<EntryTo> getEntries(@PathVariable(PARAM_USERNAME) final String username,
                                          @PathVariable(PARAM_PAGE) final int page,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response, final HttpSession session) {

        return getEntries(username, DEFAULT_SIZE, page, request, response, session);
    }

    /**
     * Get an individual entry
     *
     * @param id entry id
     * @return entry
     */

    @GetMapping(value = "{username}/eid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public EntryTo getById(@PathVariable(PARAM_USERNAME) final String username,
                           @PathVariable(PARAM_ID) final int id,
                           final HttpServletResponse response,
                           final HttpSession session) {
        try {
            final Entry entry = entryRepository.findById(id).orElse(null);

            if (entry == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }

            if (entry.getUser().getUsername().equalsIgnoreCase(username)) {
                if (entry.getSecurity().getId() == 2) // public
                    return entry.toEntryTo();
                else {
                    if (!Login.isAuthenticated(session)) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    } else if (username.equalsIgnoreCase(Login.currentLoginName(session))) {
                        return entry.toEntryTo();
                    }
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
                return null;
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    /**
     * Get all the blog entries for a user (public) /api/entry/username
     *
     * @param username
     * @param response
     * @return
     */
    @GetMapping(value = "{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<EntryTo> getEntries(@PathVariable(PARAM_USERNAME) final String username,
                                          final HttpServletResponse response) {
        Collection<EntryTo> entries = null;
        try {
            entries = entryService.getPublicEntries(username)
                    .stream()
                    .map(Entry::toEntryTo)
                    .collect(Collectors.toList());

            if (entries.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return Collections.emptyList();
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage(), e);
        }
        return entries;
    }

    @Transactional
    @GetMapping(value = "", params = "username", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<EntryTo> getEntriesByUsername(@RequestParam(PARAM_USERNAME) final String username,
                                                    final HttpServletResponse response) {
        Collection<EntryTo> entries = new ArrayList<>();
        log.warn("in entriesByUsername with " + username);

        if (username == null || username.isEmpty()) {
            log.trace("Username was null or empty ");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return entries;
        }

        try {
            entries = entryService.getPublicEntries(username)
                    .stream()
                    .map(Entry::toEntryTo)
                    .collect(Collectors.toList());

            if (entries.isEmpty()) {
                log.warn("entries is null or empty");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (final Exception e) {
            log.error("Unable to get public entries. " + e.getMessage(), e);
        }

        return entries;
    }


    /**
     * Creates a new entry resource
     *
     * @param entryTo  EntryTo
     * @param session  HttpSession
     * @param response HttpServletResponse
     * @return status ok or error
     */
    @CacheEvict(value = "recentblogs", allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> post(@RequestBody final EntryTo entryTo,
                                    final HttpSession session,
                                    final HttpServletResponse response,
                                    final Model model) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ErrorHandler.modelError( ERR_INVALID_LOGIN);
        }

        final User user = userRepository.findById(Login.currentLoginId(session)).orElse(null);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError( "User not found");
        }

        if (entryTo.getBody() == null || entryTo.getBody().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError( "Entry does not contain a body.");
        }

        final Entry entry = new Entry(entryTo);
        entry.setUser(user);
        entry.setLocation(getLocation(entryTo.getLocation()));
        entry.setSecurity(getSecurity(entryTo.getSecurity()));
        entry.setMood(getMood(entryTo.getMood()));
        entry.setTags(new HashSet<>());

        final Entry saved = entryRepository.saveAndFlush(entry);

        if (saved.getId() < 1) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorHandler.modelError( "Could not save entry");
        }

        entryService.applyTags(saved, entryTo.getTags());

        model.addAttribute("status", "ok");
        model.addAttribute("id", saved.getId());

        final HashMap<String, String> map = new HashMap<>();
        map.put("status", "ok");
        map.put("id", Integer.toString(saved.getId()));
        return map;
    }

    /**
     * PUT generally allows for add or edit in REST.
     *
     * @param entryTo  User's Blog entry
     * @param session  HttpSession
     * @param response HttpServletResponse
     * @return
     */
    @CacheEvict(value = "recentblogs")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> put(@RequestBody EntryTo entryTo, HttpSession session, HttpServletResponse response) {
        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ErrorHandler.modelError(ERR_INVALID_LOGIN);
        }
        final User user = userRepository.findById(Login.currentLoginId(session)).orElse(null);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorHandler.modelError("Invalid user or session.");
        }
        Entry entry = new Entry(entryTo);
        entry.setUser(user);

        entry.setLocation(getLocation(entryTo.getLocation()));
        entry.setSecurity(getSecurity(entryTo.getSecurity()));
        entry.setMood(getMood(entryTo.getMood()));

        final Entry entry2 = entryRepository.findById(entryTo.getEntryId()).orElse(null);

        if (entry2 != null && entry2.getId() > 0 && entry2.getUser().getId() == user.getId()) {
            entry.setId(entry2.getId());
        }

        entry = entryRepository.save(entry);
        entryService.applyTags(entry, entryTo.getTags());

        return Collections.singletonMap("id", Integer.toString(entry.getId()));
    }

    private Location getLocation(int locationId) {
        return locationDao.findById(locationId).orElse(null);
    }

    private Security getSecurity(int securityId) {
        return securityDao.findById(securityId).orElse(null);
    }

    private Mood getMood(int moodId) {
        return moodDao.findById(moodId).orElse(null);
    }

    /**
     * @param entryId  entry id
     * @param session  HttpSession
     * @param response HttpServletResponse
     * @return errors or entry id if success
     */
    @CacheEvict(value = "recentblogs")
    @DeleteMapping(value = "/{entryId}")
    @ResponseBody
    public Map<String, String> delete(@PathVariable(PARAM_ENTRY_ID) final int entryId,
                                      final HttpSession session,
                                      final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ErrorHandler.modelError(ERR_INVALID_LOGIN);
        }

        if (entryId < 1) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ErrorHandler.modelError("The entry id was invalid.");
        }

        try {
            final User user = userRepository.findById(Login.currentLoginId(session)).orElse(null);
            final Entry entry = entryRepository.findById(entryId).orElse(null);

            if (user != null && entry != null && user.getId() == entry.getUser().getId()) {
                final Iterable<Comment> comments = entry.getComments();
                commentDao.deleteAll(comments);
                entryRepository.deleteById(entryId);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ErrorHandler.modelError("Could not delete entry.");
            }

            return Collections.singletonMap("id", Integer.toString(entryId));
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError("Could not delete the comment.");
        }
    }
}
