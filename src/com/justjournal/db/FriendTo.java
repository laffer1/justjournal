
package com.justjournal.db;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 19, 2004
 * Time: 12:05:31 PM
 * To change this template use Options | File Templates.
 */
public final class FriendTo
{
    private int id;
    private String userName;
    private int ownerId;
    private String ownerUserName;

    public int getid()
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

    public int getOwnerId()
    {
        return this.ownerId;
    }

    public void setOwnerId( final int ownerId )
    {
        this.ownerId = ownerId;
    }

    public String getOwnerUserName()
    {
        return this.ownerUserName;
    }

    public void setOwnerUserName( final String ownerUserName )
    {
        this.ownerUserName = ownerUserName;
    }

    public String toString()
    {
        return Integer.toString( id ) + "," + userName + ","
                + ownerId + "," + ownerUserName;
    }
}
