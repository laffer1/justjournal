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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justjournal.model.User;
import com.justjournal.repository.UserRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/members")
public class MembersController {

    private final UserRepository userRepository;

    @Autowired
    public MembersController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * All moods usable by blogs
     *
     * @return mood list
     */
    @Transactional
    @Cacheable("members")
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<PublicMember>> list() {

        try {
            final List<User> users = userRepository.getPublicUsers();
            final List<PublicMember> publicMembers = new ArrayList<>();
            for (final User user : users) {
                publicMembers.add(new PublicMember(user.getUsername(), user.getFirstName(), user.getSince()));
            }

            return ResponseEntity.ok().eTag(Integer.toString(publicMembers.hashCode())).body(publicMembers);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }

         return new ResponseEntity<>(Collections.<PublicMember>emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @EqualsAndHashCode
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class PublicMember implements Serializable {

        @JsonIgnore
        private static final long serialVersionUID = -877596710940098083L;

        private String username;
        private String name;
        private int since;
    }
}
