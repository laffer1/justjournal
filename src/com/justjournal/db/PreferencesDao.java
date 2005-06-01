
package com.justjournal.db;

import com.justjournal.SQLHelper;
import sun.jdbc.rowset.CachedRowSet;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 16, 2004
 * Time: 12:07:17 PM
 */
public class PreferencesDao
{
    /**
     * Update the owner view only security feature.
     *
     * @param userId
     * @param ownerOnly
     * @return
     */
    public boolean updateSec( int userId, boolean ownerOnly )
       {
           boolean noError = true;
           int records = 0;
           String ownerview = "N";

           if (ownerOnly)
            ownerview = "Y";

               final String sqlStmt = "Update user_pref SET owner_view_only='" + ownerview
                       + "' WHERE id='" + userId + "' LIMIT 1;";

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

    /**
     * Retrieves the journal preferences for a certain user including
     * style information, and privacy settings.
     * @param userName  the user who needs their settings defined.
     * @return  Preferences in cached rowset.
     */
    public static CachedRowSet ViewJournalPreferences( final String userName )
            throws Exception
    {
        CachedRowSet RS;
        String sqlStatement =
                "SELECT user.name As name, user.id As id, user.since as since, up.style As style, up.allow_spider, " +
                "up.owner_view_only, st.url as cssurl, st.doc as cssdoc " +
                "FROM user, user_pref As up, user_style as st " +
                "WHERE user.username='" + userName + "' AND user.id = up.id AND user.id=st.id;";

        try
        {
            RS = SQLHelper.executeResultSet( sqlStatement );
        }
        catch ( Exception e1 )
        {
            throw new Exception( e1 );
        }

        return RS;
    }
}
