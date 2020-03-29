package com.justjournal.utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Lucas Holt
 */
public class HTMLUtilTests {

    @Test
    public void testConvertCharacterEntities() {
        String result = HTMLUtil.convertCharacterEntities("Me &amp; Joe are&nbsp; &lt; &gt;");
        assertEquals("Me & Joe are  < >", result);
    }

    @Test
    public void testStripHtmlTags() {
        String result = HTMLUtil.stripHTMLTags("<html><body><p>I am a redneck</p> <p><a href=\"foo\">foo</a></body></html>");
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
        assertEquals("<a href=\"https://www.justjournal.com/users/jjsite\">https://www.justjournal.com/users/jjsite</a>", result);
    }

    @Test
    public void testUriToLinkHttp() {
        String result = HTMLUtil.uriToLink("http://www.justjournal.com/users/jjsite ");
        assertEquals("<a href=\"http://www.justjournal.com/users/jjsite\">http://www.justjournal.com/users/jjsite</a>", result);
    }

    @Test
    public void testUriToLinkFtp() {
        String result = HTMLUtil.uriToLink("ftp://ftp.midnightbsd.org/ ");
        assertEquals("<a href=\"ftp://ftp.midnightbsd.org/\">ftp://ftp.midnightbsd.org/</a>", result);
    }
}
