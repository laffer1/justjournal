/*
 * Copyright (c) 2009, 2011 Lucas Holt
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

package com.justjournal.core;

import com.justjournal.model.RssCache;
import com.justjournal.repository.RssCacheDao;
import com.justjournal.utility.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.yield;

/**
 * Update the RSS cache
 *
 * @author Lucas Holt
 */
@Component
public class RssCacheRefresh {
    private Logger log = LoggerFactory.getLogger(RssCacheRefresh.class);

    @Autowired
    private RssCacheDao rssCacheDao;

    @Scheduled(fixedDelay = 1000 * 60 * 30, initialDelay = 60000)
    public void run() {
        log.trace("RssCache: Init");

        try {
            final Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);

            final List<RssCache> items = rssCacheDao.findByLastUpdatedBefore(yesterday.getTime());

             for (final RssCache cache : items) {
                 try {
                     getRssDocument(cache);
                 } catch (final Exception e) {
                    log.error(e.getMessage());
                 }
             }

        } catch (final Exception e) {
          log.error(e.getMessage());
        }

       log.trace("RssCache: Quit");
    }

    /**
     * Fetch the RSS feed.
     * @param rss
     * @throws Exception
     */
    void getRssDocument(final RssCache rss) throws Exception {

        URL u;
        InputStreamReader ir;
        StringBuilder sbx = new StringBuilder();
        BufferedReader buff;

        if (rss != null && rss.getUri() != null && rss.getUri().length() > 10) {

            u = new URL(rss.getUri());
            ir = new InputStreamReader(u.openStream(), "UTF-8");
            buff = new BufferedReader(ir);
            String input;
            while ((input = buff.readLine()) != null) {
                sbx.append(StringUtil.replace(input, '\'', "\\\'"));
            }
            buff.close();

            rss.setLastUpdated(Calendar.getInstance().getTime());
            rss.setContent(sbx.toString().trim());
            // sun can't make their own rss feeds complaint
            if (rss.getContent().startsWith("<rss"))
                rss.setContent("<?xml version=\"1.0\"?>\n" + rss.getContent());

            // TODO: we should remove it if this keeps happening somehow.
            if (rss.getContent().startsWith("<html") || rss.getContent().startsWith("<!DOCTYPE HTML"))
                rss.setContent(""); // it's an html page.. bad
            rssCacheDao.saveAndFlush(rss);
        }
    }

}
