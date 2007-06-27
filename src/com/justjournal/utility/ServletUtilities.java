package com.justjournal.utility;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Some simple time savers.
 * Part of tutorial on servlets and JSP that appears at
 * http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/
 * 1999 Marty Hall; may be freely used or adapted.
 *
 * @author Lucas Holt
 * @version $Id: ServletUtilities.java,v 1.1 2007/06/27 20:14:36 laffer1 Exp $
 */

public class ServletUtilities {
    public static final String DOCTYPE =
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";

    public static String headWithTitle(String title) {
        return (DOCTYPE + "\n" +
                "<html>\n" +
                "<head>\n<title>" + title +
                "</title>\n</head>\n");
    }

    /**
     * Read a parameter with the specified name, convert it to an int,
     * and return it. Return the designated default value if the parameter
     * doesn't exist or if it is an illegal integer format.
     */

    public static int getIntParameter(HttpServletRequest request,
                                      String paramName,
                                      int defaultValue) {
        String paramString = request.getParameter(paramName);
        int paramValue;
        try {
            paramValue = Integer.parseInt(paramString);
        } catch (NumberFormatException nfe) { // Handles null and bad format
            paramValue = defaultValue;
        }
        return (paramValue);
    }

    public static String getCookieValue(Cookie[] cookies,
                                        String cookieName,
                                        String defaultValue) {
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName()))
                return (cookie.getValue());
        }
        return (defaultValue);
    }

    /**
     * Create a String in the format EEE, d MMM yyyy HH:mm:ss z"
     * sutable for use in an HTTP Expires header.
     * Example: Fri, 4 Aug 2006 09:07:44 CEST
     *
     * @param minutes add n minutes to current date
     * @return expires header
     */
    public static String createExpiresHeader(int minutes) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, minutes);

        long millis = cal.getTimeInMillis();
        Date d = new Date(millis);

        return sdf.format(d);
    }

    // Approximate values are fine.
    public static final int SECONDS_PER_MONTH = 60 * 60 * 24 * 30;
    public static final int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;
}