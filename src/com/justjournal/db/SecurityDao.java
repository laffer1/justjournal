
package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;

import java.util.Collection;
import java.util.ArrayList;

import com.justjournal.SQLHelper;

/**
 * Entry Security!
 *
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 1:55:49 PM
 * To change this template use Options | File Templates.
 */
public class SecurityDao
{
    public Collection view()
    {
        ArrayList security = new ArrayList( 4 );
        CachedRowSet RS;
        SecurityTo sec;
        final String sqlStatement = "SELECT * FROM entry_security ORDER BY id ASC;";

        try
        {
            RS = SQLHelper.executeResultSet( sqlStatement );

            while ( RS.next() )
            {
                sec = new SecurityTo();

                sec.setId( RS.getInt("id") );
                sec.setName( RS.getString( "title" ) );

                security.add( sec );
            }
        }
        catch ( Exception e1 )
        {

        }

        return security;
    }
}
