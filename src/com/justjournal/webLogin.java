/*
Copyright (c) 2005, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package com.justjournal;

import sun.jdbc.rowset.CachedRowSet;

/**
 * Provides authentication and password management services to
 * web applications using the just journal data tier.
 * <p/>
 * Created on March 23, 2003, 2:34 PM
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 */
public final class webLogin {

    /**
     * Authenticate the user using clear text username and password.
     *
     * @param UserName
     * @param Password
     * @return userid of the user who logged in or 0 if the login failed.
     */
    public static final int validate(final String UserName, final String Password) {
        // the password is SHA1 encrypted in the sql server
        int userID = 0;
        String SqlStatement = "SELECT id, username FROM user WHERE username='" + UserName
                + "' AND password=SHA1('" + Password + "') LIMIT 1;";

        if (UserName.length() > 3 &&
                Password.length() > 4 &&
                Password.length() < 16) {
            try {
                CachedRowSet RS = SQLHelper.executeResultSet(SqlStatement);

                if (RS.next()) {
                    // we are comparing the username to help prevent
                    // sql injection mischief.
                    if (RS.getString(2).compareTo(UserName) == 0)
                        userID = RS.getInt(1);  // id field
                }

                RS.close();
            } catch (Exception e) {
                // should we do something here?
            }
        }

        return userID;
    }


    public static final int validateSHA1(final String UserName, final String Password) {
        // the password is SHA1 encrypted in the sql server
        int userID = 0;
        String SqlStatement = "SELECT id, username FROM user WHERE username='" + UserName
                + "' AND password='" + Password + "' LIMIT 1;";

        if (UserName.length() > 3 &&
                Password.length() > 4 &&
                Password.length() < 45) {
            try {
                CachedRowSet RS = SQLHelper.executeResultSet(SqlStatement);

                if (RS.next()) {
                    // we are comparing the username to help prevent
                    // sql injection mischief.
                    if (RS.getString(2).compareTo(UserName) == 0)
                        userID = RS.getInt(1);  // id field
                }

                RS.close();
            } catch (Exception e) {

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
     * @return true if the password change worked.
     */
    public static final boolean changePass(final String userName,
                                           final String password,
                                           final String newPass) {
        boolean result = false;
        int uid;
        int x;
        String sSQL;

        try {
            uid = validate(userName, password);

            if (uid > 0) {
                sSQL = "UPDATE user SET password=SHA1('" + newPass + "') WHERE id='"
                        + uid + "' LIMIT 1;";
                x = SQLHelper.executeNonQuery(sSQL);

                if (x == 1)
                    result = true;
            }
        } catch (Exception e) {

        }

        return result;
    }
}