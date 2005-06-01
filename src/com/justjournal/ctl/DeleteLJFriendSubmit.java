
package com.justjournal.ctl;

import com.justjournal.db.LJFriendDao;
import com.justjournal.db.LJFriendTo;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 2:16:17 PM
 * To change this template use Options | File Templates.
 */
public class DeleteLJFriendSubmit extends Protected
{
    protected String userName;  // lj username

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    protected String insidePerform() throws Exception
    {
        LJFriendDao ljdao = new LJFriendDao();
        LJFriendTo lj = new LJFriendTo();
        boolean result;


        if ( userName == null || userName.length() < 1 || userName.length() > 15 )
            addError("userName", "The LJ friend username is invalid.");

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid.");

        if ( this.hasErrors() == false )
        {

            lj.setId(this.currentLoginId());
            lj.setUserName(userName);

            result = ljdao.delete( lj );

            if ( result == false )
                addError("Unknown", "Could not delete friend.");
        }

        if ( this.hasErrors() )
            return ERROR;
         else
            return SUCCESS;
    }

}
