
package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;

import java.util.Collection;
import java.util.ArrayList;

import com.justjournal.SQLHelper;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 1:55:20 PM
 * To change this template use Options | File Templates.
 */
public final class MoodDao
{
    /**
     * Retrieve the moods from the data store including
     * the title, id and parent moods.
     * @return
     */
    public Collection view()
    {
        ArrayList moods = new ArrayList( 125 );
        CachedRowSet RS;
        MoodTo mood;
        final String sqlStatement = "SELECT id,parentmood,title FROM mood ORDER BY title ASC;";

        try
        {
            RS = SQLHelper.executeResultSet( sqlStatement );

            while ( RS.next() )
            {
                mood = new MoodTo();

                mood.setId( RS.getInt("id") );
                mood.setParent( RS.getInt("parentmood"));
                mood.setName( RS.getString( "title" ) );


                moods.add( mood );
            }
        }
        catch ( Exception e1 )
        {

        }

        return moods;
    }
}
