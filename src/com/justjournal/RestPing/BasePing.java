package com.justjournal.RestPing;

import org.apache.log4j.Category;
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
 * Date: Jul 27, 2008
 * Time: 5:50:49 AM
 */
public class BasePing {

    private static final Logger log = Logger.getLogger(BasePing.class);

    private String pingUri;
    private String uri;
    private String name;
    private String changesURL;

    public BasePing()
    {

    }

    public BasePing(String pinguri) {
        pingUri = pinguri;
    }

    public boolean ping() {
        URL u;
        URLConnection uc;
        String address;

        try {
            // build uri
            address = pingUri + "?name=" + URLEncoder.encode(name, "UTF-8") +
                    "&url=" + URLEncoder.encode(uri, "UTF-8") +
                    "&changesUrl=" + URLEncoder.encode(changesURL, "UTF-8");

            URI tmpuri = new URI(address);
            u = tmpuri.toURL();
        } catch (Exception me) {
            log.debug("Couldn't create URL. " + me.getMessage());
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

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChangesURL() {
        return changesURL;
    }

    public void setChangesURL(String changesURL) {
        this.changesURL = changesURL;
    }

    public String getPingUri() {
        return pingUri;
    }

    public void setPingUri(String pingUri) {
        this.pingUri = pingUri;
    }
}
