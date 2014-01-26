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

import com.justjournal.db.Statistics;
import com.justjournal.db.UserStatistics;
import com.justjournal.db.UserStatisticsImpl;
import com.justjournal.services.ServiceException;
import com.justjournal.services.StatisticsService;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Generate Global and per user statistics on blogging entries and comments.
 *
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/statistics")
final public class StatisticsController {
    private static final Logger log = Logger.getLogger(StatisticsController.class);

    private StatisticsService statisticsService = null;

    public void setStatisticsService(@NotNull StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Get Site statistics
     *
     * @return statistics
     */
    @Cacheable("statistics")
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    public
    @ResponseBody
    @Nullable
    Statistics get(HttpServletResponse response) {
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
     * @param id       username
     * @param response Servlet response
     * @return stats
     */
    @Cacheable(value = "userstatistics", key = "id")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public UserStatistics getById(@PathVariable String id, HttpServletResponse response) {
        try {
            if (id == null || id.equals("") || id.length() < 3) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return new UserStatisticsImpl();
            }

            UserStatistics us = statisticsService.getUserStatistics(id);
            if (us == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            return us;
        } catch (ServiceException e) {
            log.warn("User Statistics error: (" + id + "), " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new UserStatisticsImpl();
        }
    }
}
