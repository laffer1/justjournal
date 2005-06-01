package com.justjournal.ctl;

import org.apache.log4j.Category;
import com.justjournal.webLogin;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Mar 30, 2005
 * Time: 4:37:00 PM
 */
public class ChangePasswordSubmit
        extends Protected
{
    private static Category log = Category.getInstance( ChangePasswordSubmit.class.getName() );

    protected String passCurrent;
    protected String passNew;

    public String getPassCurrent()
    {
        return passCurrent;
    }

    public void setPassCurrent( final String passCurrent )
    {
        this.passCurrent = passCurrent;
    }

    public String getPassNew()
    {
        return passNew;
    }

    public void setPassNew( final String passNew )
    {
        this.passNew = passNew;
    }

    protected String insidePerform() throws Exception
    {
        if ( log.isDebugEnabled() )
            log.debug( "Loading DAO Objects  " );

        boolean result;

        if ( passCurrent == null ||
                passCurrent.length() < 5 ||
                passCurrent.length() > 15 )
            addError("passCurrent", "The current password is invalid.");

        if ( passNew == null ||
                passNew.length() < 5 ||
                passNew.length() > 15 )
            addError("passNew", "The new password is invalid.");

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid.");

        if ( this.hasErrors() == false )
        {
            result = webLogin.changePass(currentLoginName(), passCurrent, passNew );

            if ( log.isDebugEnabled() )
                log.debug( "Was there an error with data tier?  " + !result );

            if ( result == false )
                addError("Unknown", "Error changing password.  Did you type in your old password correctly?");
        }

        if ( this.hasErrors() )
            return ERROR;
         else
            return SUCCESS;
    }
}
