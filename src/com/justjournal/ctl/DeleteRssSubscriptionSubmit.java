package com.justjournal.ctl;

import org.apache.log4j.Category;
import com.justjournal.db.RssSubscriptionsTO;
import com.justjournal.db.RssSubscriptionsDAO;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Mar 28, 2005
 * Time: 5:33:44 PM

 */
public class DeleteRssSubscriptionSubmit extends Protected
{
    private static Category log =
            Category.getInstance( AddRssSubscriptionSubmit.class.getName() );
    protected String uri;  // lj username

    public String getUri()
    {
        return uri;
    }

    public void setUserName( String uri )
    {
        this.uri = uri;
    }

    protected String insidePerform() throws Exception
    {
        if ( log.isDebugEnabled() )
            log.debug( "Loading DAO Objects  " );

        RssSubscriptionsDAO dao = new RssSubscriptionsDAO();
        RssSubscriptionsTO to = new RssSubscriptionsTO();
        boolean result;


        if ( uri == null || uri.length() < 10 || uri.length() > 1024 )
            addError("uri", "The RSS feed URI is invalid.");

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid.");

        if ( this.hasErrors() == false )
        {
            to.setId(this.currentLoginId());
            to.setUri(this.uri);

            result = dao.delete( to );

            if ( result == false )
                addError("Unknown", "Could not delete subscription.");
        }

        if ( this.hasErrors() )
            return ERROR;
        else
            return SUCCESS;
    }

}
