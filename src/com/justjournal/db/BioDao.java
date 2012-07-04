/*
Copyright (c) 2005, 2008 Lucas Holt
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

import java.sql.ResultSet;


public final class BioDao {

    public static boolean add(BioTo bio) {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "INSERT INTO user_bio (id,content) values(NULL,'"
                        + bio.getBio() + "');";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    public static boolean update(BioTo bio) {

        boolean noError = true;


        final String sqlStmt =
                "Update user_bio SET content='" + bio.getBio() + "' WHERE id='" + bio.getUserId() + "' LIMIT 1;";

        try {
            SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        return noError;
    }

    public static boolean delete(int userId) {
        boolean noError = true;

        final String sqlStmt = "DELETE FROM user_bio WHERE id='" + userId + "' LIMIT 1;";

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


    public static BioTo view(int userId) {
        BioTo bio = new BioTo();
        ResultSet rs = null;
        String sqlStmt = "Select content from user_bio WHERE id='" + userId + "' Limit 1;";

        try {
            rs = SQLHelper.executeResultSet(sqlStmt);

            if (rs.next()) {
                bio.setUserId(userId);
                bio.setBio(rs.getString(1));
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

        return bio;
    }
}
