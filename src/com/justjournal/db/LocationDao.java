
package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;

import java.util.Collection;
import java.util.ArrayList;

import com.justjournal.SQLHelper;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 1:54:42 PM
 * To change this template use Options | File Templates.
 */
public final class LocationDao
{
    public Collection view()
    {
        ArrayList location = new ArrayList( 4 );
        CachedRowSet RS;
        LocationTo loc;
        final String sqlStatement = "SELECT * FROM location ORDER BY title ASC;";

        try
        {
            RS = SQLHelper.executeResultSet( sqlStatement );

            while ( RS.next() )
            {
                loc = new LocationTo();

                loc.setId( RS.getInt("id") );
                loc.setName( RS.getString( "title" ) );

                location.add( loc );
            }
        }
        catch ( Exception e1 )
        {

        }

        return location;
    }
}
