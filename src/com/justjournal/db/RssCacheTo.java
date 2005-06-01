package com.justjournal.db;

/** Rss file record.
 *
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Apr 27, 2005
 * Time: 9:47:00 PM
 */
public final class RssCacheTo
 {
    int id;
    int interval;
    DateTimeBean lastUpdated;
    String uri;
    String content;

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getInterval()
    {
        return interval;
    }

    public void setInterval( int interval )
    {
        this.interval = interval;
    }

    public DateTimeBean getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( DateTimeBean lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri( String uri )
    {
        this.uri = uri;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }
}
