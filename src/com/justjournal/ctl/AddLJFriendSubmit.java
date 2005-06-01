
package com.justjournal.ctl;

import com.justjournal.db.LJFriendDao;
import com.justjournal.db.LJFriendTo;
import org.apache.log4j.Category;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 2:16:04 PM
 * To change this template use Options | File Templates.
 */
public class AddLJFriendSubmit extends Protected
{
    private static Category log = Category.getInstance( AddLJFriendSubmit.class.getName() );

    protected String userName; // lj username
    protected String community; // is a community on LJ?

    public String getUserName()
    {
        return userName;
    }


    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getCommunity()
    {
        return community;
    }

    public void setCommunity( String community )
    {
        this.community = community;
    }

    protected String insidePerform() throws Exception
    {
        if ( log.isDebugEnabled() )
            log.debug( "Loading DAO Objects  " );

        LJFriendDao ljdao = new LJFriendDao();
        LJFriendTo lj = new LJFriendTo();
        boolean result;
        boolean bcommunity;


        if ( userName == null || userName.length() < 1 || userName.length() > 15 )
            addError("userName", "The LJ friend username is invalid.");

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid.");

        if ( this.community != null && this.community.equals( "checked" ) )
            bcommunity = true;
        else
            bcommunity = false;

        if ( this.hasErrors() == false )
        {

            lj.setId(this.currentLoginId());
            lj.setUserName(userName);
            lj.setIsCommunity(bcommunity);

            result = ljdao.add( lj );

            if ( log.isDebugEnabled() )
                log.debug( "Was there an error with data tier?  " + !result );

            if ( result == false )
                addError("Unknown", "Could not add friend.");
        }

        if ( this.hasErrors() )
            return ERROR;
         else
            return SUCCESS;
    }
}
