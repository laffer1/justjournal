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

package com.justjournal.ctl.api;

import com.justjournal.model.CalendarCount;
import com.justjournal.model.User;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * List blog entries by year, month and day for display on the site
 *
 * @author Lucas Holt
 */
@Slf4j
@Controller
@RequestMapping("/api/calendar")
public class CalendarController {

    private static final int MONTHS = 12;

    @Autowired
    private UserRepository userRepository;
    
    @Qualifier("entryRepository")
    @Autowired
    private EntryRepository entryRepository;

    @RequestMapping(value = "/counts/{username}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public
    Collection<CalendarCount> getYearCounts(@PathVariable("username") String username, HttpServletResponse response) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        final GregorianCalendar calendarg = new GregorianCalendar();
        int yearNow = calendarg.get(Calendar.YEAR);
        Collection<CalendarCount> counts = new ArrayList<CalendarCount>();

        for (int i = yearNow; i >= user.getSince(); i--) {
            CalendarCount count = new CalendarCount();
            try {
                count.setCount(entryRepository.calendarCount(i, username));
            } catch (Exception e) {
                count.setCount(0);
                log.error(e.getMessage());
            }
            count.setYear(i);

            counts.add(count);

            // just in case!
            if (i == 2002)
                break;
        }

        return counts;
    }

    @RequestMapping(value = "/counts/{username}/{year}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public
    Collection<CalendarCount> getMonthCounts(@PathVariable("username") String username, @PathVariable("year") int year, HttpServletResponse response) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Collection<CalendarCount> counts = new ArrayList<CalendarCount>();

        for (int i = 1; i < MONTHS; i++) {
            CalendarCount count = new CalendarCount();
            try {
                count.setCount(entryRepository.calendarCount(year, i, username));
            } catch (Exception e) {
                count.setCount(0);
                log.error(e.getMessage());
            }
            count.setYear(year);
            count.setMonth(i);

            counts.add(count);
        }

        return counts;
    }
}
