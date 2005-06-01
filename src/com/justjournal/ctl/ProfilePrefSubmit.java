
package com.justjournal.ctl;

import org.apache.log4j.Category;
import com.justjournal.db.UserDao;
import com.justjournal.db.UserContactDao;
import com.justjournal.db.UserTo;
import com.justjournal.db.UserContactTo;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 16, 2004
 * Time: 11:42:24 PM
 */
public class ProfilePrefSubmit extends Protected
{
    private static Category log = Category.getInstance(ProfilePrefSubmit.class.getName());

    protected String name;
    protected String email;
    protected String icq;
    protected String aim;
    protected String yahoo;
    protected String msn;
    protected String phone;
    protected String hptitle;
    protected String hpuri;

    public void setName( String name )
    {
        this.name = name;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public void setIcq( String icq )
    {
        this.icq = icq;
    }

    public void setAim( String aim )
    {
        this.aim = aim;
    }

    public void setYahoo( String yahoo )
    {
        this.yahoo = yahoo;
    }

    public void setMsn( String msn )
    {
        this.msn = msn;
    }

    public void setPhone( String phone )
    {
        this.phone = phone;
    }

    public void setHpuri( String hpuri )
    {
        this.hpuri = hpuri;
    }

    public void setHptitle( String hptitle )
    {
        this.hptitle = hptitle;
    }

      protected String insidePerform() throws Exception
    {
         if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid." );

        if (log.isDebugEnabled())
			log.debug("Loading DAO Objects  " );

        // we need the user and user contact
        UserDao udao = new UserDao(); // to set name
        UserContactDao ucd = new UserContactDao();

        UserTo user = new UserTo();
        UserContactTo contact = new UserContactTo();

        user.setId(this.currentLoginId());
        user.setName( this.name );

         if ( this.hasErrors() == false )
        {
            boolean result = false; //ucd.update(contact);

            if ( log.isDebugEnabled() )
                log.debug( "Was there an error with data tier?  " + !result );

            //if ( result == false )
                addError( "Unknown", "Could not update setting." );
        }


        if ( this.hasErrors() )
            return ERROR;
        else
            return SUCCESS;
    }
}
