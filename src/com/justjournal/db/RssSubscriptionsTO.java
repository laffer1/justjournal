package com.justjournal.db;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Mar 19, 2005
 * Time: 9:19:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class RssSubscriptionsTO {
    private int id;
    private String uri;

    public int getId()
    {
        return this.id;
    }

    public void setId( final int id )
    {
        this.id = id;
    }

    public String getUri()
    {
        return this.uri;
    }

    public void setUri( final String uri )
    {
        this.uri = uri;
    }


    public String toString()
    {
        return Integer.toString(id) + "," + uri;
    }
}
