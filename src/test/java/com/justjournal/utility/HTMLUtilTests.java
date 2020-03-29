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
}
