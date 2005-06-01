
package com.justjournal.ctl;

import org.apache.log4j.Category;
import com.justjournal.db.PreferencesDao;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 16, 2004
 * Time: 12:03:26 PM
 * To change this template use Options | File Templates.
 */
public class SecurityPrefSubmit extends Protected
{
    private static Category log = Category.getInstance( SecurityPrefSubmit.class.getName() );

    protected String ownerOnly;

    public String getOwnerOnly()
    {
        return this.ownerOnly;
    }

    public void setOwnerOnly( String ownerOnly )
    {
        this.ownerOnly = ownerOnly;
    }

    protected String insidePerform() throws Exception
    {

        boolean bownerOnly;

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid." );

        if ( log.isDebugEnabled() )
            log.debug( "Loading DAO Objects  " );

        final PreferencesDao pdao = new PreferencesDao();

        if ( this.ownerOnly != null && this.ownerOnly.equals( "checked" ) )
            bownerOnly = true;
        else
            bownerOnly = false;

        if ( this.hasErrors() == false )
        {
            boolean result = pdao.updateSec( this.currentLoginId(), bownerOnly );

            if ( log.isDebugEnabled() )
                log.debug( "Was there an error with data tier?  " + !result );

            if ( result == false )
                addError( "Unknown", "Could not update setting." );
        }

        if ( this.hasErrors() )
            return ERROR;
        else
            return SUCCESS;
    }
}
