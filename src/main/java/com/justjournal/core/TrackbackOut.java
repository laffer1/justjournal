/*
 * Copyright (c) 2011 Lucas Holt
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

import lombok.extern.slf4j.Slf4j;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: laffer1
 * Date: May 5, 2008
 * Time: 5:04:47 AM
 */
@Slf4j
public final class TrackbackOut {
    
    private String entryUrl;
    private String targetUrl;
    private String title;
    private String excerpt;
    private String blogName;

    /*
    title=Foo+Bar
&url=http://www.bar.com/
&excerpt=My+Excerpt
&blog_name=Foo
*/

    public TrackbackOut(final String targetUrl, final String entryUrl, final String title,
                        final String excerpt, final String blogName) {
        this.targetUrl = targetUrl;
        this.title = title;
        this.excerpt = excerpt;
        this.blogName = blogName;
        this.entryUrl = entryUrl;
    }

    public String getEntryUrl() {
        return entryUrl;
    }

    public void setEntryUrl(final String entryUrl) {
        this.entryUrl = entryUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(final String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(final String excerpt) {
        this.excerpt = excerpt;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(final String blogName) {
        this.blogName = blogName;
    }

    public boolean ping() {
        URL u;
        URLConnection uc;
        String address;

        // TODO: switch to POST request!
        try {
            // build uri
            address = targetUrl + "?title=" + ESAPI.encoder().encodeForURL(title) +
                    "&url=" + ESAPI.encoder().encodeForURL(entryUrl) +
                    "&blog_name=" + ESAPI.encoder().encodeForURL(blogName) +
                    "&excerpt=" + ESAPI.encoder().encodeForURL(excerpt);

            final URI tmpuri = new URI(address);

            Encoder enc = ESAPI.encoder();
            enc.canonicalize(tmpuri.getQuery());
            u = tmpuri.toURL();
        } catch (final Exception me) {
            log.error("Couldn't create URL. ", me);
            return false;
        }

        log.debug(u.toExternalForm());

        try {
            uc = u.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));

            String inputLine;
            final StringBuilder input = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                input.append(inputLine);

            in.close();

            if (log.isDebugEnabled())
                log.debug("{}\n{}", entryUrl, input.toString());

            return true; // todo: parse result and adjust this as necessary.
        } catch (final IOException e) {
            log.warn("Could not send trackback out for {}", u.toString(), e);
            return false;
        }
    }

}
