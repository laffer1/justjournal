/*
Copyright (c) 2003-2021, Lucas Holt
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
package com.justjournal.services;


import com.justjournal.utility.DNSUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import lombok.extern.slf4j.Slf4j;
import org.owasp.esapi.ESAPI;

/** User: laffer1 Date: Jul 27, 2008 Time: 5:50:49 AM */
@Slf4j
public class RestPing {

  protected String pingUri;
  protected String uri;
  protected String name;
  protected String changesURL;

  public RestPing(final String pingUri) {
    setPingUri(pingUri);
  }

  protected URL createUrl(final String uri) throws URISyntaxException, MalformedURLException {
    final URI tmpuri = new URI(uri);
    return tmpuri.toURL();
  }

  public boolean ping() {
    final URLConnection uc;

    try {
      final String cleanName = ESAPI.encoder().encodeForURL(getName());
      final String cleanUrl = ESAPI.encoder().encodeForURL(getUri());
      final String changesUrl = ESAPI.encoder().encodeForURL(getChangesURL());

      uc =
          createUrl(
                  getUri()
                      + "?name="
                      + cleanName
                      + "&url="
                      + cleanUrl
                      + "&changesUrl="
                      + changesUrl
                      + "&rssUrl="
                      + changesUrl)
              .openConnection();
    } catch (final Exception me) {
      log.error("Couldn't create URL for rest ping", me);
      return false;
    }

    try {
      final BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

      String inputLine;
      final StringBuilder input = new StringBuilder();
      while ((inputLine = in.readLine()) != null) input.append(inputLine);

      in.close();

      log.debug(uri + "\n" + input.toString());

      return true; // todo: parse result and adjust this as necessary.
    } catch (final IOException e) {
      log.error("Could read response from rest ping from {}", uc.getURL().toString(), e);
      return false;
    }
  }

  public String getUri() {
    return uri;
  }

  public void setUri(final String uri) {
    if (!DNSUtil.isUrlDomainValid(uri)) {
      throw new IllegalArgumentException("uri");
    }
    this.uri = uri;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getChangesURL() {
    return changesURL;
  }

  public void setChangesURL(final String changesURL) {
    if (!DNSUtil.isUrlDomainValid(changesURL)) {
      throw new IllegalArgumentException("changesURL");
    }
    this.changesURL = changesURL;
  }

  public String getPingUri() {
    return pingUri;
  }

  public void setPingUri(final String pingUri) {
    if (!DNSUtil.isUrlDomainValid(pingUri)) {
      throw new IllegalArgumentException("pingUri");
    }
    this.pingUri = pingUri;
  }
}
