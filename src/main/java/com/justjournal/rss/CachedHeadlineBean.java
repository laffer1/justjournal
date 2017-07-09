/*
Copyright (c) 2005, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package com.justjournal.rss;

import com.justjournal.model.DateTime;
import com.justjournal.model.DateTimeBean;
import com.justjournal.model.RssCache;
import com.justjournal.repository.RssCacheRepository;
import com.justjournal.utility.StringUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Calendar;


/**
 * Stores RSS content collected from the internet into a datastore,
 * retrieves stored versions, and spits out HTML to render them.
 *
 * @author Lucas Holt
 * @version $Id: CachedHeadlineBean.java,v 1.7 2011/05/29 22:32:59 laffer1 Exp $
 *          <p/>   Previously version was incremented manually.
 *          1.4 Attempt to catch case were RSS record content is empty in database.
 *          1.3 Added sun fix to correct sun.com rss feeds (rss .92 issue?)
 *          xml declaration was missing in feed and whitespace at front.
 *          1.2 Fixed bug where records might not get updated.
 *          1.1 optimized code
 *          1.0 Initial release
 */
@Slf4j
@Component
public class CachedHeadlineBean extends HeadlineBean {

    private RssCacheRepository rssCacheRepository;

    @Autowired
    public CachedHeadlineBean(final RssCacheRepository rssCacheRepository) {
        this.rssCacheRepository = rssCacheRepository;
    }

    @Override
    protected HeadlineContext getRssDocument(@NonNull final String uri) throws Exception {
        
        log.info("Starting getRssDocument()");
        final HeadlineContext hc = new HeadlineContext();
        
        InputStreamReader ir;
        final StringBuilder sbx = new StringBuilder();
        BufferedReader buff;
        final java.util.GregorianCalendar calendarg = new java.util.GregorianCalendar();

        log.info("looking up rss cache repo uri");
        RssCache rss = rssCacheRepository.findByUri(uri);
        log.info("rss cache lookup done");

        if (rss != null && rss.getUri() != null && rss.getUri().length() > 10) {
            log.info("Record found with uri: " + uri);

            final DateTime dt = new DateTimeBean(rss.getLastUpdated());

            if (dt.getDay() != calendarg.get(java.util.Calendar.DATE)) {

                    log.info("getRssDocument() Day doesn't match: " + uri);
                hc.u = new URL(uri);
                ir = new InputStreamReader(hc.u.openStream(), "UTF-8");
                buff = new BufferedReader(ir);
                String input;
                while ((input = buff.readLine()) != null) {
                    sbx.append(StringUtil.replace(input, '\'', "\\\'"));
                }
                buff.close();

                rss.setContent(sbx.toString().trim());
                // sun can't make their own rss feeds complaint
                if (rss.getContent().startsWith("<rss"))
                    rss.setContent("<?xml version=\"1.0\"?>\n" + rss.getContent());
                rssCacheRepository.saveAndFlush(rss);
            } else if (rss.getContent() == null || rss.getContent().isEmpty()) {
                // empty rss record in database.  error?
               
                log.info("getRssDocument() Empty RSS data: " + uri);
                hc.u = new URL(uri);
                ir = new InputStreamReader(hc.u.openStream(), "UTF-8");
                buff = new BufferedReader(ir);
                String input;
                while ((input = buff.readLine()) != null) {
                    sbx.append(StringUtil.replace(input, '\'', "\\\'"));
                }
                buff.close();

                rss.setContent(sbx.toString().trim());
                // sun can't make their own rss feeds complaint
                if (rss.getContent().startsWith("<rss"))
                    rss.setContent("<?xml version=\"1.0\"?>\n" + rss.getContent());
                rssCacheRepository.saveAndFlush(rss);
            } else {
                    log.info("Hit end.. no date change.");
            }

        } else {
            log.info("Fetch uri: " + uri);
            rss = new RssCache();

            CloseableHttpClient httpclient = null;

            try {
                httpclient = HttpClients.createDefault();
                final HttpGet httpGet = new HttpGet(uri);
                final CloseableHttpResponse response1 = httpclient.execute(httpGet);

                try {
                    final HttpEntity entity1 = response1.getEntity();

                    ir = new InputStreamReader(entity1.getContent(), "UTF-8");
                    buff = new BufferedReader(ir);
                    String input;
                    while ((input = buff.readLine()) != null) {
                        sbx.append(StringUtil.replace(input, '\'', "\\\'"));
                    }
                    buff.close();
                    ir.close();
                    log.debug(sbx.toString());

                } finally {
                    try {
                        response1.close();
                    } catch (final IOException io) {
                        log.error(io.getMessage(), io);
                    }
                    try {
                        httpclient.close();
                    } catch (final IOException io) {
                        log.error(io.getMessage(), io);
                    }
                }
            } catch (final IOException e) {
                log.error(e.getMessage(), e);
                try {
                        httpclient.close();
                } catch (final IOException io) {
                    log.error(io.getMessage(), io);
                }
            }

            try {
                rss.setUri(uri);
                rss.setInterval(24);
                rss.setContent(sbx.toString().trim());
                rss.setLastUpdated(Calendar.getInstance().getTime());
                // sun can't make their own rss feeds complaint
                if (rss.getContent().startsWith("<rss"))
                    rss.setContent("<?xml version=\"1.0\"?>\n" + rss.getContent());
                rssCacheRepository.saveAndFlush(rss);
            } catch (final java.lang.NullPointerException n) {
                log.error("Null pointer exception creating/adding rss cache object to db.", n);
            }
        }

        log.info("getRssDocument() Retrieved uri from database: " + uri);

        log.debug(sbx.toString());

        final StringReader sr = new StringReader(rss.getContent());

        final org.xml.sax.InputSource saxy = new org.xml.sax.InputSource(sr);
        hc.factory.setValidating(false);
        hc.builder = hc.factory.newDocumentBuilder();
        hc.document = hc.builder.parse(saxy);

        log.debug("Hit end of getRssDocument() for " + uri);
        return hc;
    }

}
