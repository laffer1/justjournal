package com.justjournal.db;

import com.justjournal.SQLHelper;

import sun.jdbc.rowset.CachedRowSet;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Apr 27, 2005
 * Time: 9:46:51 PM

 */
public class RssCacheDao
 {
    public boolean add( RssCacheTo rss )
    {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "INSERT INTO rss_cache (interval, lastupdated, uri, content) values('"
                + rss.getInterval() + "', now(),'"
                + rss.getUri() + "','"
                + rss.getContent()
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

    public boolean update( RssCacheTo rss )
    {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "UPDATE rss_cache SET lastupdated=now()," +
                 " content='" + rss.getContent() + "';";

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

    public boolean delete( RssCacheTo rss )
    {
        boolean noError = true;
        int records = 0;

        final String sqlStmt = "DELETE FROM rss_cache WHERE id='" + rss.getId() +
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

    public RssCacheTo view( final String uri )
    {
        CachedRowSet RS = null;
        DateTimeBean dt = new DateTimeBean();
        RssCacheTo rss = new RssCacheTo();
        final String sqlStatement = "SELECT id, interval, lastupdated, uri, content FROM rss_subscriptions WHERE uri='"
                + uri + "';";

        try
        {
            RS = SQLHelper.executeResultSet( sqlStatement );

            if ( RS.next() )
            {

                rss.setId( RS.getInt("id") );
                rss.setInterval( RS.getInt("interval"));
                dt.set("lastupdated");
                rss.setLastUpdated(dt);
                rss.setUri( RS.getString( "uri" ) );
                rss.setContent( RS.getString("content"));
            }
            else
                rss = null;

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

        return rss;
    }

}
