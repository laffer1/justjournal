package com.justjournal.db;

/**
 * User: laffer1
 * Date: Mar 24, 2006
 * Time: 11:22:14 PM
 */
public interface DateTime {
    int getDay();

    void setDay(int day);

    int getMonth();

    void setMonth(int month);

    int getYear();

    void setYear(int year);

    int getHour();

    void setHour(int hour);

    int getMinutes();

    void setMinutes(int minutes);

    void set(String mysqlDate)
            throws java.text.ParseException;

    void set(java.util.Date date);

    String toPubDate();

    String toString();
}
