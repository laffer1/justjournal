package com.justjournal.utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Lucas Holt
 */
public class ThumbnailTests {

    private Thumbnail thumbnail = new Thumbnail();

    @Test
    public void testCalcThumbRatio() {
        Double result = thumbnail.calcThumbRatio();
        assertEquals(Double.valueOf("1.0"), result);
    }
}
