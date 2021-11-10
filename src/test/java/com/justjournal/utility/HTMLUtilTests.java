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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import org.junit.Test;

/** @author Lucas Holt */
public class HTMLUtilTests {

  @Test
  public void testConvertCharacterEntities() {
    String result = HTMLUtil.convertCharacterEntities("Me &amp; Joe are&nbsp; &lt; &gt;");
    assertEquals("Me & Joe are  < >", result);
  }

  @Test
  public void testStripHtmlTags() {
    String result =
        HTMLUtil.stripHTMLTags(
            "<html><body><p>I am a redneck</p> <p><a" + " href=\"foo\">foo</a></body></html>");
    assertEquals("I am a redneck foo", result);
  }

  @Test
  public void testTextFromHTML() {
    String result = HTMLUtil.textFromHTML("<p>Me &amp; Joe are&nbsp; &lt; &gt;</p>");
    assertEquals("Me & Joe are  < >", result);
  }

  @Test
  public void testUriToLinkHTTPS() {
    String result = HTMLUtil.uriToLink("https://www.justjournal.com/users/jjsite ");
    assertEquals(
        "<a href=\"https://www.justjournal.com/users/jjsite\">https://www.justjournal.com/users/jjsite</a>",
        result);
  }

  @Test
  public void testUriToLinkHttp() {
    String result = HTMLUtil.uriToLink("http://www.justjournal.com/users/jjsite ");
    assertEquals(
        "<a href=\"http://www.justjournal.com/users/jjsite\">http://www.justjournal.com/users/jjsite</a>",
        result);
  }

  @Test
  public void testUriToLinkFtp() {
    String result = HTMLUtil.uriToLink("ftp://ftp.midnightbsd.org/ ");
    assertEquals("<a href=\"ftp://ftp.midnightbsd.org/\">ftp://ftp.midnightbsd.org/</a>", result);
  }

  @Test
  public void testGetURIsSingle() {
    List<String> result =
        HTMLUtil.getURIs(
            "I am a url pattern https://www.justjournal.com/users/jjsite and i like" + " it.");
    assertFalse(result.isEmpty());
    assertEquals("https://www.justjournal.com/users/jjsite", result.get(0));
  }
}
