package com.justjournal.technorati;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import java.net.URL;
import java.net.URLConnection;
import java.net.URI;
import java.io.*;

/**
 * @author Lucas Holt
 * @version 1.0
 * Date: Jun 4, 2007
 * Time: 1:59:02 AM
 * http://rpc.technorati.com/rpc/ping
 */
public class RestPing {

        private static final Logger log = Logger.getLogger(RestPing.class);

        private String pingUri = "http://rpc.technorati.com/rpc/ping";
        private String uri;
        private String name;

        public boolean ping() {
            URL u;
            URLConnection uc;
            String address;

            try {
                // build uri
                address = pingUri;

                /*+ "?name=" + URLEncoder.encode(name, "UTF-8") +
                        "&url=" + URLEncoder.encode(uri, "UTF-8") +
                        "&changesUrl=" + URLEncoder.encode(changesURL, "UTF-8");
                */
                URI tmpuri = new URI(address);
                u = tmpuri.toURL();
            } catch (Exception me) {
                log.debug("Couldn't create URL. " + me.getMessage());
                return false;
            }

            log.debug(u.toExternalForm());

            try {
                uc = u.openConnection();

                uc.setDoInput(true);
                uc.setDoOutput(true);
                uc.setUseCaches(false);

                uc.setRequestProperty("Content-Type", "text/xml");

                DataOutputStream printout = new DataOutputStream (uc.getOutputStream ());

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
                printout.writeBytes (output);
                printout.flush ();
                printout.close ();

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
    }

