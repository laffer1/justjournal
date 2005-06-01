
package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;

import java.util.Collection;
import java.util.ArrayList;

import com.justjournal.SQLHelper;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 4, 2004
 * Time: 7:04:41 PM
 */
public final class LJFriendDao
{
    public boolean add( LJFriendTo lj )
    {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "INSERT INTO friends_lj (id,username,community) values('"
                + lj.getId() + "','"
                + lj.getUserName() + "','"
                + (lj.getIsCommunity() ? 'Y' : 'N')
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

    public boolean delete( LJFriendTo lj )
    {
        boolean noError = true;
        int records = 0;

        final String sqlStmt = "DELETE FROM friends_lj WHERE id='" + lj.getId() +
                "' AND username='" + lj.getUserName() +"' AND community='" +
                (lj.getIsCommunity() ? 'Y' : 'N') + "' LIMIT 1;";

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
        ArrayList friends = new ArrayList( 10 );
        CachedRowSet RS = null;
        LJFriendTo lj;
        final String sqlStatement = "SELECT username, community FROM friends_lj WHERE id='" + userId
                + "' ORDER by community, username;";

        try
        {
            RS = SQLHelper.executeResultSet( sqlStatement );

            while ( RS.next() )
            {
                lj = new LJFriendTo();

                lj.setId( userId );
                lj.setUserName( RS.getString( "username" ) );
                lj.setIsCommunity( RS.getString( "community").compareTo("Y") == 0 ? true : false);

                friends.add( lj );
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

        return friends;
    }
}
