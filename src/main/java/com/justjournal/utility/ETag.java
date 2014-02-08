/*
Copyright (c) 2009 Lucas Holt
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


package com.justjournal.utility;

import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ETag HTTP Header implmentation.  Hashes input to generate a unique ETag.
 *
 * Format must be ETag: "mytag"  (including quotes)
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
 * 
 * @author Lucas Holt
 * @version $Id: ETag.java,v 1.1 2009/05/30 18:22:21 laffer1 Exp $
 */
public class ETag {
    private org.slf4j.Logger log = LoggerFactory.getLogger(ETag.class);
    protected HttpServletResponse response;

    public ETag(HttpServletResponse httpResponse) {
        this.response = httpResponse;
    }

    public void writeFromString(String input) {
        if (input == null)
            throw new IllegalArgumentException("Input cannot be null");
        
        writeFromByteArray(input.getBytes());
    }

    public void writeFromByteArray(byte[] input) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(input, 0, input.length);
            String result = new BigInteger(1, digest.digest()).toString(16);
            write(result);
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 hash algorithm is not available.  ETag will not function.");
        }
    }

    public void write(String hash) {
        if (hash != null) {
            if (hash.length() == 31) {
                hash = "0" + hash;     // keep padding like other langs do
            }
            response.setHeader("ETag", '"' + hash + '"');
        } else {
            throw new IllegalArgumentException("Hash value must be set for ETag");
        }
    }
}
