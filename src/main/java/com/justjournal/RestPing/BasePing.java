package com.justjournal.restping;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * User: laffer1 Date: Jul 27, 2008 Time: 5:50:49 AM
 */
public class BasePing {

    private static final Logger log = Logger.getLogger(BasePing.class);

    protected String pingUri;
    protected String uri;
    protected String name;
    protected String changesURL;

    public BasePing(final String pinguri) {
        pingUri = pinguri;
    }

    protected URL createUrl(final String uri) throws Exception {
        URI tmpuri = new URI(uri);
        return tmpuri.toURL();
    }

    public boolean ping() {
        URLConnection uc;

        try {
            uc = createUrl(getUri() + "?name=" + URLEncoder.encode(getName(), "UTF-8") +
                    "&url=" + URLEncoder.encode(getUri(), "UTF-8") +
                    "&changesUrl=" + URLEncoder.encode(getChangesURL(), "UTF-8")).openConnection();
        } catch (Exception me) {
            log.debug("Couldn't create URL. " + me.getMessage());
            return false;
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));

            String inputLine;
            String input = "";
            while ((inputLine = in.readLine()) != null)
                input += inputLine;

            in.close();

            log.debug(uri + "\n" + input);

            return true; // todo: parse result and adjust this as necessary.

        } catch (IOException e) {
            log.debug("IO Error: " + e.getMessage());
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
