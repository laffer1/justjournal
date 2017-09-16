/*
 * Copyright (c) 2014 Lucas Holt
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

package com.justjournal.services;

import com.justjournal.model.Statistics;
import com.justjournal.model.StatisticsImpl;
import com.justjournal.model.User;
import com.justjournal.model.UserStatistics;
import com.justjournal.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Provide Statistics Services for Just Journal
 *
 * @author Lucas Holt
 */
@Slf4j
@Service
@Transactional(Transactional.TxType.REQUIRED)
public class StatisticsService {

    @Autowired
    private EntryRepository entryRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TagRepository tagDao;

    @Autowired
    private SecurityRepository securityDao;

    @Autowired
    private StyleRepository styleRepository;

    @Transactional(value = Transactional.TxType.REQUIRED)
    public UserStatistics getUserStatistics(final String username) throws ServiceException {

        try {
            final UserStatistics userStatistics = new UserStatistics();

            if (username == null) {
                return null;
            }
            final User user = userRepository.findByUsername(username);
            if (user == null)
                return null;

            userStatistics.setUsername(username);
            
            userStatistics.setEntryCount(entryRepository.countByUsername(username).intValue());
            userStatistics.setCommentCount(commentRepository.countByUsername(username).intValue());

            return userStatistics;
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }


    public Statistics getStatistics() throws ServiceException {
        try {
            final Statistics statistics = new StatisticsImpl();

            statistics.setComments(commentRepository.count());
            statistics.setEntries(entryRepository.count());
            statistics.setUsers(userRepository.count());
            statistics.setTags(tagDao.count());

            statistics.setStyles(styleRepository.count());

            statistics.setPublicEntries(entryRepository.countBySecurity(securityDao.findOne(2)));
            statistics.setPrivateEntries(entryRepository.countBySecurity(securityDao.findOne(0)));
            statistics.setFriendsEntries(entryRepository.countBySecurity(securityDao.findOne(1)));

            return statistics;
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }
}
