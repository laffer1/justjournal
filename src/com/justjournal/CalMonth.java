
package com.justjournal;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

/** Represents a calendar month of journal entries.  Each
 * item stores the number of journal entries on a particular
 * day in the month as an integer.
 * @author Lucas Holt
 */
public final class CalMonth
{
    public int[] storage = null;
    public int monthid = 0;
    private Date baseDate;
    private final GregorianCalendar calendarG = new java.util.GregorianCalendar();
    //private final Calendar calendarB = Calendar.getInstance();

    /**
     * Creates an instance with all the required properties set.
     * @param monthid   the month this object represents
     * @param storage   an integer array of # of entries
     * @param baseDate
     */
    public CalMonth( final int monthid, final int[] storage, final Date baseDate )
    {
        this.storage = storage;
        this.monthid = monthid;
        this.baseDate = baseDate;
        calendarG.setTime( baseDate );
    }

    public int getMonthId()
    {
        return this.monthid;
    }

    public void setMonthId( final int monthid )
    {
        this.monthid = monthid;
    }

    public int[] getStorage()
    {
        return this.storage;
    }

    public void setStorage( final int[] storage )
    {
        this.storage = storage;
    }

    public Date getBaseDate()
    {
        return this.baseDate;
    }

    public void setBaseDate( final Date baseDate )
    {
        this.baseDate = baseDate;
    }

    public int getYear()
    {
        return calendarG.get( Calendar.YEAR );
    }

    public int getFirstDayInWeek()
    {
        return calendarG.get( Calendar.DAY_OF_WEEK );
    }

    public int getMonth()
    {
        return calendarG.get( Calendar.MONTH );
    }

}
