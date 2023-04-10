package com.justjournal.utility;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DateConvertTests {

    @Test
    void testEncode3339() throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "2014-02-11";
        Date dateObject = sdf.parse(dateString);

        var result = DateConvert.encode3339(dateObject);
        assertEquals("2014-02-11T05:00:00.000Z", result);
    }

    @Test
    void testDecode3339() throws ParseException {
        var result = DateConvert.decode3339("2014-02-11T05:00:00.000Z");
        assertNotNull(result);
    }

    @Test
    void testEncode822() throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "2014-02-11";
        Date dateObject = sdf.parse(dateString);

        var result = DateConvert.encode822(dateObject);
        assertEquals("Tue, 11 Feb 2014 05:00:00 +0000", result);
    }

    @Test
    void testEncode822Tz() throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "2014-02-11";
        Date dateObject = sdf.parse(dateString);

        var result = DateConvert.encode822(dateObject, TimeZone.getTimeZone("EST"));
        assertEquals("Tue, 11 Feb 2014 00:00:00 -0500", result);
    }
}
