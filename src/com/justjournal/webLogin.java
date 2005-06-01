
package com.justjournal;

import sun.jdbc.rowset.CachedRowSet;

/**
 * Provides authentication and password management services to
 * web applications using the just journal data tier.
 *
 * Created on March 23, 2003, 2:34 PM
 * @author  Lucas Holt
 * @version 1.0
 * @since 1.0
 */
public final class webLogin
{

    /**
     * Authenticate the user using clear text username and password.
     * @param UserName
     * @param Password
     * @return
     */
    public static final int validate( final String UserName, final String Password )
    {
        // the password is SHA1 encrypted in the sql server
        int userID = 0;
        String SqlStatement = "SELECT id, username FROM user WHERE username='" + UserName
                + "' AND password=SHA1('" + Password + "') LIMIT 1;";

        if ( UserName.length() > 3 &&
             Password.length() > 4 &&
             Password.length() < 16 )
        {
            try {
                CachedRowSet RS = SQLHelper.executeResultSet( SqlStatement );

                if ( RS.next() )
                {
                    // we are comparing the username to help prevent
                    // sql injection mischief.
                    if ( RS.getString(2).compareTo(UserName) == 0)
                        userID = RS.getInt(1);  // id field
                }

                RS.close();
            } catch ( Exception e ) {
                // should we do something here?
            }
        }

        return userID;
    }


    public static final int validateSHA1( final String UserName, final String Password )
    {
         // the password is SHA1 encrypted in the sql server
        int userID = 0;
        String SqlStatement = "SELECT id, username FROM user WHERE username='" + UserName
                + "' AND password='" + Password + "' LIMIT 1;";

        if ( UserName.length() > 3 &&
             Password.length() > 4 &&
             Password.length() < 45 )
        {
            try {
                CachedRowSet RS = SQLHelper.executeResultSet( SqlStatement );

                if ( RS.next() ) {
                    // we are comparing the username to help prevent
                    // sql injection mischief.
                    if ( RS.getString(2).compareTo(UserName) == 0)
                        userID = RS.getInt(1);  // id field
                }

                RS.close();
            } catch ( Exception e ) {

            }
        }

        return userID;
    }


    /**
     * Change the password for an account given the username, old and new
     * passwords.
     *
     * @param userName
     * @param password
     * @param newPass
     * @return
     */
    public static final boolean changePass( final String userName,
                                     final String password,
                                     final String newPass )
    {
        boolean result = false;
        int uid;
        int x;
        String sSQL;

        try {
            uid = validate( userName, password );

            if ( uid > 0 )
            {
                sSQL = "UPDATE user SET password=SHA1('" + newPass + "') WHERE id='"
                        + uid + "' LIMIT 1;";
                x = SQLHelper.executeNonQuery(sSQL);

                if ( x == 1 )
                    result = true;
            }
        } catch ( Exception e ) {

        }

        return result;
    }
}