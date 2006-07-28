package com.justjournal.weblogs;

import org.apache.log4j.Category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: laffer1
 * Date: Jul 27, 2006
 * Time: 6:43:07 PM
 */
public class RestPing {

    private static Category log = Category.getInstance(RestPing.class.getName());

    private String uri = "http://rpc.weblogs.com/pingSiteForm";
    private String name;
    private String changesURL;

    public boolean ping() {
        URL u;
        URLConnection uc;

        // build uri
        uri = uri + "?name=" + name + "&url=" + uri + "&changesUrl=" + changesURL;

        try {
            u = new URL(getUri());
        } catch (Exception me) {
            return false;
        }

        try {
            uc = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));

            String inputLine;
            String input = "";
            while ((inputLine = in.readLine()) != null)
                input += inputLine;

            in.close();

            if (log.isDebugEnabled())
                log.debug(uri + "\n" + input);

            return true; // todo: parse result and adjust this as necessary.

        } catch (IOException e) {
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
}
