package com.justjournal.db;

import com.justjournal.SQLHelper;

import java.util.Collection;
import java.util.ArrayList;

import sun.jdbc.rowset.CachedRowSet;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Mar 19, 2005
 * Time: 9:17:55 PM
 */
final public class RssSubscriptionsDAO {

        public boolean add( RssSubscriptionsTO rss )
        {
            boolean noError = true;
            int records = 0;

            final String sqlStmt =
                    "INSERT INTO rss_subscriptions (id,uri) values('"
                    + rss.getId() + "','"
                    + rss.getUri()
                    + "');";

            try
            {
                records = SQLHelper.executeNonQuery( sqlStmt );
            }
            catch ( Exception e )
            {
                noError = false;
            }

            if ( records != 1 )
                noError = false;

            return noError;
        }

        public boolean delete( RssSubscriptionsTO rss )
        {
            boolean noError = true;
            int records = 0;

            final String sqlStmt = "DELETE FROM rss_subscriptions WHERE id='" + rss.getId() +
                    "' AND uri='" + rss.getUri() + "' LIMIT 1;";

            try
            {
                records = SQLHelper.executeNonQuery( sqlStmt );
            }
            catch ( Exception e )
            {
                noError = false;
            }

            if ( records != 1 )
                noError = false;

            return noError;
        }

        public Collection view( final int userId )
        {
            ArrayList rssfeeds = new ArrayList( 10 );
            CachedRowSet RS = null;
            RssSubscriptionsTO rss;
            final String sqlStatement = "SELECT uri FROM rss_subscriptions WHERE id='"
                    + userId + "';";

            try
            {
                RS = SQLHelper.executeResultSet( sqlStatement );

                while ( RS.next() )
                {
                    rss = new RssSubscriptionsTO();

                    rss.setId( userId );
                    rss.setUri( RS.getString( "uri" ) );

                    rssfeeds.add( rss );
                }

                RS.close();
            }
            catch ( Exception e1 )
            {
                try {
                    if ( RS != null )
                        RS.close();
                } catch ( java.sql.SQLException e2) {
                     // nothing to do.
                }
            }

            return rssfeeds;
        }

}
