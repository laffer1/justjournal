package com.justjournal.restping;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * User: laffer1 Date: Jul 27, 2008 Time: 5:50:49 AM
 */
@Slf4j
public class BasePing {
    
    protected String pingUri;
    protected String uri;
    protected String name;
    protected String changesURL;

    public BasePing(final String pinguri) {
        pingUri = pinguri;
    }

    protected URL createUrl(final String uri) throws Exception {
        final URI tmpuri = new URI(uri);
        return tmpuri.toURL();
    }

    public boolean ping() {
        final URLConnection uc;

        try {
            uc = createUrl(getUri() + "?name=" + URLEncoder.encode(getName(), StandardCharsets.UTF_8.displayName()) +
                    "&url=" + URLEncoder.encode(getUri(), StandardCharsets.UTF_8.displayName()) +
                    "&changesUrl=" + URLEncoder.encode(getChangesURL(), StandardCharsets.UTF_8.displayName())).openConnection();
        } catch (final Exception me) {
            log.debug("Couldn't create URL.", me);
            return false;
        }

        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));

            String inputLine;
            final StringBuilder input = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                input.append(inputLine);

            in.close();

            log.debug(uri + "\n" + input.toString());

            return true; // todo: parse result and adjust this as necessary.
        } catch (final IOException e) {
            log.debug("IO Error", e);
            return false;
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
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
        this.changesURL = changesURL;
    }

    public String getPingUri() {
        return pingUri;
    }

    public void setPingUri(final String pingUri) {
        this.pingUri = pingUri;
    }
}
