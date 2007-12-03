/*
Copyright (c) 2006, Lucas Holt
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

package com.justjournal.google;

import org.apache.log4j.Category;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.URI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * @author Lucas Holt
 * @version $Id: RestPing.java,v 1.2 2007/12/03 19:04:27 laffer1 Exp $
 */
public class RestPing {


        private static Category log = Category.getInstance(RestPing.class.getName());

        private String pingUri = "http://blogsearch.google.com/ping";
        private String uri;
        private String name;
        private String changesURL;

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
    }

