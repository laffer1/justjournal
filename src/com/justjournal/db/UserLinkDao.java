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

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: laffer1
 * Date: Dec 22, 2005
 * Time: 2:58:10 PM
 */
public final class UserLinkDao {
    private final BaseDao dao = new BaseDao();

    /**
     * Add a link to the user link list.
     *
     * @param link A new hyperlink to add.
     * @return true if no error occured.
     */
    public boolean add(final UserLinkTo link) {

        final String sqlStmt = "Insert into user_link (linkid,id,title,uri) VALUES('"
                + link.getId() + "','" + link.getUserId() + "','" + link.getTitle() + "','"
                + link.getUri() + "');";

        return dao.add(sqlStmt);
    }

    public static boolean delete(final UserLinkTo link) {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM user_link WHERE id='" + link.getUserId() +
                "' AND linkid='" + link.getId() +
                "' LIMIT 1;";

        try {
            SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        return noError;
    }

    public static Collection<UserLinkTo> view(int userId) {
        ArrayList<UserLinkTo> links = new ArrayList<UserLinkTo>(10);
        String sql = "SELECT * FROM user_link WHERE id='" + userId + "';";

        try {
            CachedRowSet rs = SQLHelper.executeResultSet(sql);

            while (rs.next()) {
                links.add(new UserLinkTo(rs.getInt("linkid"), rs.getInt("id"),
                        rs.getString("title"), rs.getString("uri")));

            }
        } catch (Exception e) {

        }

        return links;
    }

}
