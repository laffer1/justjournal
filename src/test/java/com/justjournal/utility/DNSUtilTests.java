/*
 * Copyright (c) 2003-2021 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.justjournal.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** @author Lucas Holt */
class DNSUtilTests {

  @Test
  void testUrlDomainValid() {
    boolean result = DNSUtil.isUrlDomainValid("http://justjournal.com/users/jjsite");
    Assertions.assertTrue(result);
  }

  @Test
  void testGetDomainFromEmail() {
    String result = DNSUtil.getDomainFromEmail("test@justjournal.com");
    Assertions.assertEquals("justjournal.com", result);
  }

  @Test
  void testIsEmailDomainValid() {
    boolean result = DNSUtil.isEmailDomainValid("test@justjournal.com");
    Assertions.assertTrue(result);
  }

  @Test
  void testIsEmailDomainInvalid() {
    boolean result = DNSUtil.isDomainValid("test@iamareallylonginvaliddomainname.justjournal.com");
    Assertions.assertFalse(result);
  }

  @Test
  void testIsDomainValid() {
    boolean result = DNSUtil.isDomainValid("justjournal.com");
    Assertions.assertTrue(result);
  }

  @Test
  void testIsDomainInvalid() {
    boolean result = DNSUtil.isDomainValid("iamareallylonginvaliddomainname.justjournal.com");
    Assertions.assertFalse(result);
  }

  @Test
  void testIsDomainNull() {
    boolean result = DNSUtil.isDomainValid(null);
    Assertions.assertFalse(result);
  }
}
