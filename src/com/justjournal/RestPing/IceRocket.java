package com.justjournal.RestPing;

import org.apache.log4j.Category;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author Lucas Holt
 * @version $Id: IceRocket.java,v 1.1 2008/07/27 10:10:02 laffer1 Exp $
 * Date: July 27 2008
 * Time: 6:00:00 AM
 */
public class IceRocket {

        private static Category log = Category.getInstance(IceRocket.class.getName());

        private String pingUri = "http://rpc.icerocket.com:10080";
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

