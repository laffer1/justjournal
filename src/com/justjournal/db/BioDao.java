
package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;
import com.justjournal.SQLHelper;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 3, 2004
 * Time: 11:21:11 PM
 * To change this template use Options | File Templates.
 */
public class BioDao
{
    public boolean add(BioTo bio)
    {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "INSERT INTO user_bio (id,content) values(NULL,'"
                + bio.getBio() + "');";

        try {
           records = SQLHelper.executeNonQuery( sqlStmt );
        } catch ( Exception e ) {
            noError = false;
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    public boolean update( BioTo bio )
    {

        boolean noError = true;

        final String sqlStmt =
                "Update user_bio SET content='" + bio.getBio() + "' WHERE id='" + bio.getUserId() + "' LIMIT 1;";

        try {
           SQLHelper.executeNonQuery( sqlStmt );
        } catch ( Exception e ) {
            noError = false;
        }

        return noError;
    }

    public boolean delete( int userId )
    {
        boolean noError = true;

        final String sqlStmt = "DELETE FROM user_bio WHERE id='" + userId + "' LIMIT 1;";

        if ( userId > 0)
        {
            try {
                SQLHelper.executeNonQuery(sqlStmt);
            } catch (Exception e) {
                noError = false;
            }
        } else {
            noError = false;
        }

        return noError;
    }


    public BioTo view( int userId )
    {
        BioTo bio = new BioTo();
        CachedRowSet rs = null;
        String sqlStmt = "Select content from user_bio WHERE id='" + userId + "' Limit 1;";

        try
        {

            rs = SQLHelper.executeResultSet( sqlStmt );

            if ( rs.next() )
            {
                bio.setUserId( userId );
                bio.setBio( rs.getString( 1 ) );
            }

            rs.close();

        }
        catch ( Exception e1 )
        {
            if ( rs != null )
            {
                try
                {
                    rs.close();
                }
                catch ( Exception e )
                {
                    // NOTHING TO DO
                }
            }
        }


        return bio;
    }
}
