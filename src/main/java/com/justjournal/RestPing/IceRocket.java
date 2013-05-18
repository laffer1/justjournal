package com.justjournal.restping;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author Lucas Holt
 * @version $Id: IceRocket.java,v 1.4 2011/06/12 06:24:38 laffer1 Exp $
 */
public class IceRocket extends BasePing {

    private static final Logger log = Logger.getLogger(IceRocket.class);

    public IceRocket() {
        super("http://rpc.icerocket.com:10080");
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

            String output = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<methodCall>\n" +
                    "  <methodName>ping</methodName>\n" +
                    "    <params>\n" +
                    "      <param>\n" +
                    "        <value>" + URLEncoder.encode(name, "UTF-8") + "</value>\n" +
                    "      </param>\n" +
                    "      <param>\n" +
                    "        <value>" + URLEncoder.encode(uri, "UTF-8") + "</value>\n" +
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