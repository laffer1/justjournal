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
import com.justjournal.repository.RssCacheRepository;
import com.justjournal.utility.StringUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Update the RSS cache
 *
 * @author Lucas Holt
 */
@Slf4j
@Component
@Profile("!test")
public class RssCacheRefresh {

    @Autowired
    private RssCacheRepository rssCacheDao;

    @Scheduled(fixedDelay = 1000 * 60 * 30, initialDelay = 60000)
    public void run() {
        log.info("RssCache: Init");

        try {
            final Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);

            final List<RssCache> items = rssCacheDao.findByLastUpdatedBefore(yesterday.getTime());


            Observable.fromIterable(items)
                    .subscribeOn(Schedulers.computation())
                    .map(new Function<RssCache, Object>() {

                        @Override
                        public Object apply(final RssCache cache) throws Exception {
                            try {
                                if (cache.getActive() != null && cache.getActive())
                                    getRssDocument(cache);
                            } catch (final Exception e) {
                                log.error(e.getMessage(), e);
                            }
                            return cache;
                        }
                    })
                    .subscribe();

        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("RssCache: Quit");
    }

    /**
     * Fetch the RSS feed.
     *
     * @param rss
     * @throws IOException
     */
    void getRssDocument(final RssCache rss) throws IOException {
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
                        int bytesRead;
                        final BufferedInputStream bis = new BufferedInputStream(inputStream);
                        while ((bytesRead = bis.read(buffer)) != -1) {
                            final String chunk = new String(buffer, 0, bytesRead);
                            sbx.append(StringUtil.replace(chunk, '\'', "\\\'"));
                        }
                        bis.close();
                    } catch (final Exception e) {
                        log.error(e.getMessage(), e);
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
                if (log.isWarnEnabled())
                    log.warn("URL " + rss.getUri() + " is returning a 404. Removing from list");

                rss.setLastUpdated(Calendar.getInstance().getTime());
                rss.setActive(false);
                rssCacheDao.saveAndFlush(rss);
            } else {
                if (log.isWarnEnabled())
                    log.warn(String.format("RssCache status code %d for url %s", code, rss.getUri()));
            }
        }
    }
}
