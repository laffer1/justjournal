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

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * User: laffer1
 * Date: May 5, 2008
 * Time: 5:04:47 AM
 */
public final class TrackbackOut {
    private static Logger log = Logger.getLogger(TrackbackOut.class.getName());

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

    public TrackbackOut(String targetUrl, String entryUrl, String title, String excerpt, String blogName) {
        this.targetUrl = targetUrl;
        this.title = title;
        this.excerpt = excerpt;
        this.blogName = blogName;
        this.entryUrl = entryUrl;
    }

    public String getEntryUrl() {
        return entryUrl;
    }

    public void setEntryUrl(String entryUrl) {
        this.entryUrl = entryUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public boolean ping() {
        URL u;
        URLConnection uc;
        String address;

        // TODO: switch to POST request!
        try {
            // build uri
            address = targetUrl + "?title=" + URLEncoder.encode(title, "UTF-8") +
                    "&url=" + URLEncoder.encode(entryUrl, "UTF-8") +
                    "&blog_name=" + URLEncoder.encode(blogName, "UTF-8") +
                    "&excerpt=" + URLEncoder.encode(excerpt, "UTF-8");

            URI tmpuri = new URI(address);
            u = tmpuri.toURL();
        } catch (Exception me) {
            log.error("Couldn't create URL. " + me.getMessage());
            return false;
        }

        log.debug(u.toExternalForm());

        try {
            uc = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));

            String inputLine;
            String input = "";
            while ((inputLine = in.readLine()) != null)
                input += inputLine;

            in.close();

            log.debug(entryUrl + "\n" + input);

            return true; // todo: parse result and adjust this as necessary.
        } catch (IOException e) {
            log.debug("IO Error: " + e.getMessage());
            return false;
        }
    }

}
