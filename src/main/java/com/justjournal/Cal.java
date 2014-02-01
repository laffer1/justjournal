/*
Copyright (c) 2005-2009, Lucas Holt
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

package com.justjournal;

import com.justjournal.db.EntryTo;
import org.apache.log4j.Logger;

import java.lang.reflect.Array;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Storage for calendar months.
 *
 * @author Lucas Holt
 * @version $Id: Cal.java,v 1.12 2012/07/04 18:48:53 laffer1 Exp $
 * @see CalMonth
 */
public final class Cal {
    private static final Logger log = Logger.getLogger(Cal.class);
    public static final int MONTHS_IN_YEAR = 12;

    private Collection<EntryTo> entries;
    private final List<CalMonth> Months = new ArrayList<CalMonth>(MONTHS_IN_YEAR);
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final SimpleDateFormat shortDate = new SimpleDateFormat("yyyy-MM-dd");

    private final String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private final String[] daysSmall = {"S", "M", "T", "W", "R", "F", "S"};

    private final String[] months = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    private String baseUrl = null;

    public Cal(final Collection<EntryTo> entries) {
        this.entries = entries;
        this.calculateEntryCounts();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private void calculateEntryCounts() {
        final java.util.GregorianCalendar calendarg = new java.util.GregorianCalendar();
        int month = -1; // first time through we want to fire the change code, this is 0 based.
        int day;
        int year = 0;
        int[] monthPostCt = null;

        try {
            for (EntryTo entryTo : entries) {
                final java.util.Date currentDate = entryTo.getDate();

                calendarg.setTime(currentDate);
                year = calendarg.get(java.util.Calendar.YEAR);

                if (month == calendarg.get(java.util.Calendar.MONTH)) {
                    // month didn't change
                    day = calendarg.get(java.util.Calendar.DAY_OF_MONTH);
                    if (monthPostCt != null)
                        monthPostCt[day - 1]++;
                } else {
                    if (monthPostCt != null) {
                        // get first day of month (falls on)
                        final ParsePosition pos2 = new ParsePosition(0);
                        // "yyyy-MM-dd"
                        final java.util.Date baseDate = shortDate.parse(year + "-" + (month + 1) + "-01", pos2);

                        Months.add(new CalMonth(month, monthPostCt, baseDate));
                    }

                    monthPostCt = new int[calendarg.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)];

                    day = calendarg.get(java.util.Calendar.DAY_OF_MONTH);
                    monthPostCt[day - 1]++;

                    month = calendarg.get(java.util.Calendar.MONTH);
                }
            }

            final ParsePosition pos2 = new ParsePosition(0);
            // "yyyy-MM-dd"
            final java.util.Date baseDate = shortDate.parse(year + "-" + (month + 1) + "-01", pos2);
            final int[] n = monthPostCt;
            Months.add(new CalMonth(month, n, baseDate));
        } catch (Exception e) {
            log.debug("Exception raised in calculateEntryCounts... " + e.toString());
        }
    }

    public String render() {

        final StringBuilder sb = new StringBuilder();
        CalMonth o;
        final Iterator<CalMonth> itr = Months.listIterator();

        sb.append("<!-- Calendar Output -->\n");

        for (int i = 0, n = Months.size(); i < n; i++) {
            o = itr.next();
            sb.append("<table class=\"fullcalendar\" cellpadding=\"1\" cellspacing=\"1\">\n");

            sb.append("<caption>");
            sb.append(months[o.monthid]);
            sb.append(" ");
            sb.append(o.getYear());
            sb.append("</caption>\n");

            sb.append("<thead>\n");
            sb.append("<tr>\n");
            for (int x = 0; x < 7; x++) {
                sb.append("\t<th class=\"fullcalendarth\">");
                sb.append(days[x]);
                sb.append("</th>\n");
            }
            sb.append("</tr>\n");
            sb.append("</thead>\n");

            sb.append("<tbody>\n");

            int dayinweek;
            boolean blnFirstTime = true; // first time through
            sb.append("<tr>\n");

            if (o.getFirstDayInWeek() > 1)
                sb.append("\t<td class=\"fullcalendaroffrow\" colspan=\"").append(o.getFirstDayInWeek() - 1).append("\"></td>");

            dayinweek = o.getFirstDayInWeek() - 1;

            for (int y = 0; y < java.lang.reflect.Array.getLength(o.storage); y++) {
                if (dayinweek == 0 && !blnFirstTime) {
                    sb.append("<tr>\n");
                }

                sb.append("\t<td class=\"fullcalendarrow\"><strong>");
                sb.append(y + 1);
                sb.append("</strong><br /><span style=\"float: right;\">");
                if (o.storage[y] == 0) {
                    sb.append("&nbsp;");
                } else {
                    sb.append("<a href=\"");

                    // year
                    sb.append(o.getYear());
                    sb.append("/");

                    // month
                    if ((o.monthid + 1) < 10) {
                        sb.append("0");
                    }
                    sb.append(o.monthid + 1);
                    sb.append("/");

                    // day
                    if ((y + 1) < 10) {
                        sb.append("0");
                    }
                    sb.append(y + 1);
                    sb.append("\">");
                    sb.append(o.storage[y]);
                    sb.append("</a>");
                }
                sb.append("</span></td>\n");

                if (dayinweek == 6) {
                    sb.append("</tr>\n");
                    dayinweek = 0;
                    blnFirstTime = false; // hiding this here makes it execute less.
                } else {
                    dayinweek++;
                }

            }

            if (dayinweek <= 6 && dayinweek != 0) {
                // this is seven because colspan is 1 based.  why do the
                // extra addition +1
                sb.append("\t<td class=\"fullcalendaroffrow\" colspan=\"").append(7 - dayinweek).append(" \"></td>");
                sb.append("</tr>\n");
            }

            sb.append("<tr>\n");
            sb.append("\t<td class=\"fullcalendarsub\" colspan=\"7\"><a href=\"");
            sb.append(o.getYear());
            sb.append("/");
            if ((o.monthid + 1) < 10) {
                sb.append("0");
            }
            sb.append(o.monthid + 1);
            sb.append("\">View Subjects</a></td>\n");
            sb.append("</tr>\n");
            sb.append("</tbody>\n");
            sb.append("</table>\n\n");

        }
        return sb.toString();
    }

    public String renderMini() {
        final StringBuilder sb = new StringBuilder();

        sb.append("\t<!-- Calendar Output -->\n");

        for (CalMonth o : Months) {
            sb.append("\t<table class=\"minicalendar\" cellpadding=\"1\" cellspacing=\"1\">\n");

            sb.append("\t\t<caption>");
            sb.append(months[o.monthid]);
            sb.append(" ");
            sb.append(o.getYear());
            sb.append("</caption>\n");

            sb.append("\t\t<thead>\n\t\t<tr>\n");
            for (int x = 0; x < 7; x++) {
                sb.append("\t\t\t<th class=\"minicalendarth\">");
                sb.append(daysSmall[x]);
                sb.append("</th>\n");
            }
            sb.append("\t\t</tr>\n\t\t</thead>\n");

            int dayinweek;
            boolean blnFirstTime = true; // first time through
            sb.append("\t\t<tbody>\n\t\t<tr>\n");

            if (o.getFirstDayInWeek() > 1)
                sb.append("\t\t<td class=\"minicalendaroffrow\" colspan=\"").append(o.getFirstDayInWeek() - 1).append("\"></td>\n");

            dayinweek = o.getFirstDayInWeek() - 1;

            for (int y = 0; y < Array.getLength(o.storage); y++) {
                if (dayinweek == 0 && !blnFirstTime) {
                    sb.append("\t\t<tr>\n");
                }

                sb.append("\t\t<td class=\"minicalendarrow\">");

                if (o.storage[y] == 0) {
                    sb.append(y + 1);
                } else {
                    sb.append("<a href=\"");
                    sb.append(baseUrl);

                    // year
                    sb.append(o.getYear());
                    sb.append("/");

                    // month
                    if ((o.monthid + 1) < 10) {
                        sb.append("0");
                    }
                    sb.append(o.monthid + 1);
                    sb.append("/");

                    // day
                    if ((y + 1) < 10) {
                        sb.append("0");
                    }
                    sb.append(y + 1);
                    sb.append("\">");
                    sb.append(y + 1);
                    sb.append("</a>");
                }
                sb.append("</td>\n");

                if (dayinweek == 6) {
                    sb.append("\t\t</tr>\n");
                    dayinweek = 0;
                    blnFirstTime = false; // hiding this here makes it execute less.
                } else {
                    dayinweek++;
                }

            }

            if (dayinweek <= 6 && dayinweek != 0) {
                // this is seven because colspan is 1 based.  why do the
                // extra addition +1
                sb.append("\t\t<td class=\"minicalendaroffrow\" colspan=\"").append(7 - dayinweek).append(" \"></td>");
                sb.append("\t\t</tr>\n");
            }

            sb.append("\t\t<tr>\n");
            sb.append("\t\t<td class=\"minicalendarsub\" colspan=\"7\"><a href=\"");
            sb.append(baseUrl);
            sb.append(o.getYear());
            sb.append("/");
            if ((o.monthid + 1) < 10)
                sb.append("0");
            sb.append(o.monthid + 1);
            sb.append("\">View Subjects</a></td>\n");
            sb.append("\t\t</tr>\n\t\t</tbody>\n");
            sb.append("\t</table>\n\n");

        }
        return sb.toString();
    }
}
