/*
Copyright (c) 2007, Lucas Holt
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
 * @version $Id: ServletUtilities.java,v 1.2 2007/12/03 19:05:14 laffer1 Exp $
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