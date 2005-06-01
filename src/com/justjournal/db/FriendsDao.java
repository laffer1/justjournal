
package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;

import java.util.Collection;
import java.util.ArrayList;

import com.justjournal.SQLHelper;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 19, 2004
 * Time: 12:00:37 PM
 * To change this template use Options | File Templates.
 */
public final class FriendsDao
{
    public Collection view( final int userId )
    {
        ArrayList friends = new ArrayList( 10 );
        CachedRowSet RS;
        FriendTo fr;
        final String sqlStatement = "SELECT friends.friendid, user.username FROM friends, user WHERE friends.id='"
                + userId + "' AND friends.friendid=user.id;";

        try
        {
            RS = SQLHelper.executeResultSet( sqlStatement );

            while ( RS.next() )
            {
                fr = new FriendTo();

                fr.setId( RS.getInt("friendid") );
                fr.setUserName( RS.getString( "username" ) );
                fr.setOwnerId( userId );
                //fr.setOwnerUserName( RS.getString("owneruname"));

                friends.add( fr );
            }
            RS.close();
        }
        catch ( Exception e1 )
        {

        }

        return friends;
    }
}
