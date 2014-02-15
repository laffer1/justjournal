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

import com.justjournal.model.Statistics;
import com.justjournal.model.UserStatistics;
import com.justjournal.services.ServiceException;
import com.justjournal.services.StatisticsService;
import com.sun.istack.internal.Nullable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

/**
 * Generate Global and per user statistics on blogging entries and comments.
 *
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/statistics")
public class StatisticsController {
    private static final Logger log = Logger.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    /**
     * Get Site statistics
     *
     * @return statistics
     */
    @Transactional(value= Transactional.TxType.REQUIRED)
    @Cacheable("statistics")
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    public
    @ResponseBody
    @Nullable
    Statistics get(HttpServletResponse response) {
        if ((statisticsService == null)) throw new AssertionError();

        try {
            return statisticsService.getStatistics();
        } catch (ServiceException e) {
            log.warn("Statistics not available");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    /**
     * Get statistics for a specific user
     *
     * @param username       username
     * @param response Servlet response
     * @return stats
     */
    @Transactional(value= Transactional.TxType.REQUIRED)
    @Cacheable(value = "userstatistics", key = "username")
    @RequestMapping(value = "{username}", method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public UserStatistics getById(@PathVariable("username") String username, HttpServletResponse response) {
        if (statisticsService == null) throw new AssertionError();

        try {
            if (username == null || username.equals("") || username.length() < 3) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return new UserStatistics();
            }

            UserStatistics us = statisticsService.getUserStatistics(username);
            if (us == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            return us;
        } catch (ServiceException e) {
            log.warn("User Statistics error: (" + username + "), " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new UserStatistics();
        }
    }
}
