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

import com.justjournal.model.Journal;
import com.justjournal.model.PrefBool;
import com.justjournal.model.User;
import com.justjournal.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/members")
public class MembersController {
    private static final Logger log = Logger.getLogger(MembersController.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * All moods usable by blogs
     *
     * @return mood list
     */
    @Transactional
    @Cacheable("members")
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    public
    @ResponseBody
    Collection<User> list() {
        if ((userRepository == null)) throw new AssertionError();

        Collection<User> publicMembers = new ArrayList<User>();

        try {
            Iterable<User> members = userRepository.findAll();

            if ((members == null)) throw new AssertionError();

            for (User member : members) {
                boolean stealth = false;
                for (Journal journal : member.getJournals()) {
                   if (journal.isOwnerViewOnly()) {
                       stealth = true;
                       break;
                   }
                }
                if (!stealth) {
                    publicMembers.add(member);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return publicMembers;
    }
}
