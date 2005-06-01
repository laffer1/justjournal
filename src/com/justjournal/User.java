
package com.justjournal;

/**
 * Represents a user's basic credentals including userId and
 * userName.  Designed to be lightweight.
 * @author Lucas Holt
 * @version 1.0
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 4, 2004
 * Time: 9:59:35 PM
 */
public final class User
{
    private String userName = "";
    private int userId = 0;

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName( String userName )
    {
        if ( userName == null )
            userName = "";

        this.userName = userName;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public void setUserId( int userId )
    {

        this.userId = userId;
    }

    public void setUserId( Integer userId )
    {
        if ( userId != null )
        {
            this.userId = userId.intValue();
        }
    }

    public String toString()
    {
        return Integer.toString(userId) + "," + userName;
    }
}
