/*
Copyright (c) 2005-2006, Lucas Holt
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

package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Access account information for a specific user or all users
 * of Just Journal.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 */
public final class UserDao {

    /**
     * Add a user to justjournal including their name, username and
     * password.
     *
     * @param user
     * @return True if successful, false otherwise.
     */
    public static final boolean add(UserTo user) {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "Insert INTO user (username,password,name) VALUES('"
                        + user.getUserName() + "',sha1('" + user.getPassword()
                        + "'),'" + user.getName() + "');";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    /**
     * update the user record with a new first name.
     * usernames can not be changed.
     * passwords can be changed via the
     * <code>com.justjournal.ctl.ChangePasswordSubmit</code> class.
     *
     * @param user
     * @return
     * @see com.justjournal.ctl.ChangePasswordSubmit
     */
    public static final boolean update(UserTo user) {
        boolean noError = true;

        final String sqlStmt =
                "Update user SET name='" + user.getName() + "' WHERE id='" + user.getId() + "' LIMIT 1;";

        try {
            SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        return noError;
    }

    /**
     * Deletes a user from the database.  This
     * should not be called by cancel account.  Accounts
     * should be deactivated.
     *
     * @param userId
     * @return true if successful, false otherwise
     */
    public static final boolean delete(int userId) {
        boolean noError = true;

        final String sqlStmt = "DELETE FROM user WHERE id='" + userId + "' LIMIT 1;";

        if (userId > 0) {
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

    /**
     * Retrieve a user given the user id.
     * Does NOT retrieve password or sha1 password.
     *
     * @param userId
     * @return user's info
     */
    public static final UserTo view(int userId) {
        UserTo user = new UserTo();
        CachedRowSet rs = null;
        String sqlStmt = "SELECT username, name, since from user WHERE id='" + userId + "' Limit 1;";

        try {

            rs = SQLHelper.executeResultSet(sqlStmt);

            if (rs.next()) {
                user.setId(userId);
                user.setUserName(rs.getString(1)); // username
                user.setName(rs.getString(2)); // first name
                user.setSince(rs.getInt(3));
            }

            rs.close();

        } catch (Exception e1) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }
        return user;

    }

    /**
     * Retrieve a user give the user name.
     * Does not retrieve the password or
     * sha1 password.
     *
     * @param userName
     * @return user's info
     */
    public static final UserTo view(String userName) {
        UserTo user = new UserTo();
        CachedRowSet rs = null;
        String sqlStmt = "SELECT id, name, since from user WHERE username='" + userName + "' Limit 1;";

        try {

            rs = SQLHelper.executeResultSet(sqlStmt);

            if (rs.next()) {
                user.setId(rs.getInt(1));
                user.setUserName(userName);
                user.setName(rs.getString(2)); // first name
                user.setSince(rs.getInt(3));
            }

            rs.close();

        } catch (Exception e1) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }
        return user;
    }

    /**
     * Retrieve the list of all users including their name,
     * username, sign up year (since), and unique id.
     *
     * @return All users of just journal.
     */
    public static final Collection<UserTo> memberList() {
        ArrayList<UserTo> users = new ArrayList<UserTo>(125);
        UserTo usr;
        final String sqlStatement = "call memberlist();";

        try {
            final CachedRowSet RS = SQLHelper.executeResultSet(sqlStatement);

            while (RS.next()) {
                usr = new UserTo();
                usr.setId(RS.getInt(1));
                usr.setUserName(RS.getString(2));
                usr.setName(RS.getString(3));
                usr.setSince(RS.getInt(4));
                users.add(usr);
            }

            RS.close();
        } catch (Exception e1) {

        }

        return users;
    }

}
