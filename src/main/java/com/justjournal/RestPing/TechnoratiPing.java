package com.justjournal.RestPing;

import org.apache.log4j.Logger;
import java.net.URLConnection;
import java.io.*;

/**
 * @author Lucas Holt
 * @version 1.0 Date: Jun 4, 2007 Time: 1:59:02 AM http://rpc.technorati.com/rpc/ping
 */
public class TechnoratiPing extends BasePing {

    private static final Logger log = Logger.getLogger(TechnoratiPing.class);

    public TechnoratiPing() {
        super("http://rpc.technorati.com/rpc/ping");
    }

    public boolean ping() {
        URLConnection uc;

        try {
            uc = createUrl(getUri()).openConnection();

            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setUseCaches(false);

            uc.setRequestProperty("Content-Type", "text/xml");

            DataOutputStream printout = new DataOutputStream(uc.getOutputStream());

            String output = "<?xml version=\"1.0\"?>\n" +
                    "<methodCall>\n" +
                    "  <methodName>weblogUpdates.ping</methodName>\n" +
                    "    <params>\n" +
                    "      <param>\n" +
                    "        <value>" + name + "</value>\n" +
                    "      </param>\n" +
                    "      <param>\n" +
                    "        <value>" + uri + "</value>\n" +
                    "      </param>\n" +
                    "    </params>\n" +
                    "</methodCall>\n";
            printout.writeBytes(output);
            printout.flush();
            printout.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));

            String inputLine;
            String input = "";
            while ((inputLine = in.readLine()) != null)
                input += inputLine;

            in.close();

            log.debug(uri + "\n" + input);

            return true; // todo: parse result and adjust this as necessary.
        } catch (Exception e) {
            log.debug("IO Error: " + e.getMessage());
            return false;
        }
    }
}

