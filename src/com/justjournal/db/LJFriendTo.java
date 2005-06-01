
package com.justjournal.db;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 4, 2004
 * Time: 7:04:26 PM
 * To change this template use Options | File Templates.
 */
public final class LJFriendTo
{
    private int id;
    private String userName;
    private boolean isCommunity;

    public int getId()
    {
        return this.id;
    }

    public void setId( final int id )
    {
        this.id = id;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName( final String userName )
    {
        this.userName = userName;
    }

    public boolean getIsCommunity()
    {
        return this.isCommunity;
    }

    public void setIsCommunity( final boolean isCommunity )
    {
        this.isCommunity = isCommunity;
    }

    public String toString()
    {
        return Integer.toString(id) + "," + userName + "," + isCommunity;
    }
}
