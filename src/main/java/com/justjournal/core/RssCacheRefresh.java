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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import static java.lang.Thread.sleep;

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
                    if (!cache.getActive())
                        continue;

                    getRssDocument(cache);
                    sleep(200);
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
     *
     * @param rss
     * @throws Exception
     */
    void getRssDocument(final RssCache rss) throws Exception {
        final StringBuilder sbx = new StringBuilder();

        if (rss != null && rss.getUri() != null && rss.getUri().length() > 10) {
            final HttpClient client = HttpClientBuilder.create().build();
            final HttpGet request = new HttpGet(rss.getUri());
            final HttpResponse response = client.execute(request);

            final int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                final HttpEntity entity = response.getEntity();
                final byte[] buffer = new byte[1024];
                if (entity != null) {
                    final InputStream inputStream = entity.getContent();
                    try {
                        int bytesRead = 0;
                        final BufferedInputStream bis = new BufferedInputStream(inputStream);
                        while ((bytesRead = bis.read(buffer)) != -1) {
                            final String chunk = new String(buffer, 0, bytesRead);
                            sbx.append(StringUtil.replace(chunk, '\'', "\\\'"));
                        }
                        bis.close();
                    } catch (final Exception e) {
                        log.error(e.getMessage());
                    }
                }

                rss.setLastUpdated(Calendar.getInstance().getTime());
                rss.setContent(sbx.toString().trim());
                // sun can't make their own rss feeds complaint
                if (rss.getContent().startsWith("<rss"))
                    rss.setContent("<?xml version=\"1.0\"?>\n" + rss.getContent());

                // TODO: we should remove it if this keeps happening somehow.
                if (rss.getContent().startsWith("<html") || rss.getContent().startsWith("<!DOCTYPE HTML"))
                    rss.setContent(""); // it's an html page.. bad

                rssCacheDao.saveAndFlush(rss);
            } else if (code == 404 || code == 410) {
                log.warn("URL " + rss.getUri() + " is returning a 404. Removing from list");

                rss.setLastUpdated(Calendar.getInstance().getTime());
                rss.setActive(false);
                rssCacheDao.saveAndFlush(rss);
            } else {
                log.warn("RssCache status code " + code + " for url " + rss.getUri());
            }
        }
    }
}
