/*
 * Copyright (c) 2007, 2014 Lucas Holt
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

import com.sun.istack.internal.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date format utilities
 *
 * @author Lucas Holt
 */
public final class DateConvert {
    private static final int TZ_8601_OFFSET = 6;

    private static SimpleDateFormat getDateFormat822() {
        return new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
    }

    private static SimpleDateFormat getDateFormat3339() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    private static SimpleDateFormat getDateFormat8601() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
    }

    public
    @NotNull
    static String encode822() {
        SimpleDateFormat df822 = getDateFormat822();
        df822.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df822.format(new Date());
    }

    public
    @NotNull
    static String encode822(Date data) {
        SimpleDateFormat df822 = getDateFormat822();
        df822.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df822.format(data);
    }

    public
    @NotNull
    static String encode822(Date data, TimeZone tz) {
        SimpleDateFormat df822 = getDateFormat822();

        df822.setTimeZone(tz);
        return df822.format(data);
    }

    public
    @NotNull
    static String encode3339() {
        SimpleDateFormat df3339 = getDateFormat3339();
        df3339.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df3339.format(new Date());
    }

    public
    @NotNull
    static String encode3339(Date data) {
        SimpleDateFormat df3339 = getDateFormat3339();
        df3339.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df3339.format(data);
    }

    @NotNull
    public static Date decode8601(String input) throws java.text.ParseException {
        SimpleDateFormat df8601 = getDateFormat8601();
        String date = input;
        if (date.endsWith("Z")) {
            date = date.substring(0, date.length() - 1) + "GMT-00:00";
        } else {
            date = date.substring(0, date.length() - TZ_8601_OFFSET) + "GMT" + date.substring(date.length() - TZ_8601_OFFSET, date.length());
        }

        return df8601.parse(date);

    }

    @NotNull
    public static String encode8601() {
        return encode8601(new Date());
    }

    @NotNull
    public static String encode8601(Date data) {
        SimpleDateFormat df8601 = getDateFormat8601();
        df8601.setTimeZone(TimeZone.getTimeZone("UTC"));
        String s = df8601.format(data);

        String result = s.substring(0, s.length() - 9) + s.substring(s.length() - TZ_8601_OFFSET, s.length());
        result = result.replaceAll("UTC", "+00:00");

        return result;

    }
}