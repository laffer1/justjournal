package com.justjournal.utility;

import com.sun.istack.internal.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * User: laffer1
 * Date: Dec 25, 2007
 * Time: 11:52:55 PM
 *
 * @author Lucas Holt
 * @version $Id: DateConvert.java,v 1.4 2011/05/29 22:32:59 laffer1 Exp $
 */
public final class DateConvert {
    private static final SimpleDateFormat df822 = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
    private static final SimpleDateFormat df3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public @NotNull
    static String encode822() {
        df822.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df822.format(new Date());
    }

    public @NotNull static String encode822(Date data) {
        df822.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df822.format(data);
    }

    public @NotNull static String encode822(Date data, TimeZone tz) {
        df822.setTimeZone(tz);
        return df822.format(data);
    }

    public @NotNull static String encode3339() {
        df3339.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df3339.format(new Date());
    }

    public @NotNull static String encode3339(Date data) {
        df3339.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df3339.format(data);
    }
}