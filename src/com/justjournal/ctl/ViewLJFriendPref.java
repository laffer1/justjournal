
package com.justjournal.ctl;

import com.justjournal.db.LJFriendDao;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 2:17:23 PM

 */
public class ViewLJFriendPref extends Protected
{
    private Collection ljfriends;

    public String getMyLogin() {
        return this.currentLoginName();
    }

    public Collection getljfriends()
    {
        return this.ljfriends;
    }

    protected String insidePerform() throws Exception
    {
        LJFriendDao ljdao = new LJFriendDao();

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid.");

        if ( this.hasErrors() == false )
        {
            try {
                ljfriends = ljdao.view(this.currentLoginId());

            } catch ( Exception e) {
                return ERROR;
            }
        } else {
            return ERROR;
        }

        return SUCCESS;
    }
}