/*
Copyright (c) 2005, Lucas Holt
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Represents a calendar month of journal entries.  Each
 * item stores the number of journal entries on a particular
 * day in the month as an integer.
 *
 * @author Lucas Holt
 * @version $Id: CalMonth.java,v 1.3 2006/05/07 21:22:42 laffer1 Exp $
 */
public final class CalMonth {
    public int[] storage = null;
    public int monthid = 0;
    private Date baseDate;
    private final GregorianCalendar calendarG = new java.util.GregorianCalendar();
    //private final Calendar calendarB = Calendar.getInstance();

    /**
     * Creates an instance with all the required properties set.
     *
     * @param monthid  the month this object represents
     * @param storage  an integer array of # of entries
     * @param baseDate
     */
    public CalMonth(final int monthid, final int[] storage, final Date baseDate) {
        this.storage = storage;
        this.monthid = monthid;
        this.baseDate = baseDate;
        calendarG.setTime(baseDate);
    }

    public int getMonthId() {
        return this.monthid;
    }

    public void setMonthId(final int monthid) {
        this.monthid = monthid;
    }

    public int[] getStorage() {
        return this.storage;
    }

    public void setStorage(final int[] storage) {
        this.storage = storage;
    }

    public Date getBaseDate() {
        return this.baseDate;
    }

    public void setBaseDate(final Date baseDate) {
        this.baseDate = baseDate;
    }

    public int getYear() {
        return calendarG.get(Calendar.YEAR);
    }

    public int getFirstDayInWeek() {
        return calendarG.get(Calendar.DAY_OF_WEEK);
    }

    public int getMonth() {
        return calendarG.get(Calendar.MONTH);
    }

}
