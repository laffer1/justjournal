/*
Copyright (c) 2005, 2006, 2008 Lucas Holt
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

import org.apache.log4j.Category;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Data access for User Link list
 * @see UserLinkTo
 * @version $Id: UserLinkDao.java,v 1.10 2008/08/31 20:55:24 laffer1 Exp $
 * @author Lucas Holt
 */
public final class UserLinkDao {
    
     private static final Category log = Category.getInstance(UserLinkDao.class.getName());

    /**
     * Add a link to the user link list.
     *
     * @param link A new hyperlink to add.
     * @see UserLinkTo
     * @return true if no error occured.
     */
    public boolean add(final UserLinkTo link) {

        final String sqlStmt = "Insert into user_link (linkid,id,title,uri) VALUES('"
                + link.getId() + "','" + link.getUserId() + "','" + link.getTitle() + "','"
                + link.getUri() + "');";

        return BaseDao.add(sqlStmt);
    }

    /**
     * Delete a link from the userlink list.
     * @param link user link to delete
     * @see UserLinkTo
     * @return true on success
     */
    public static boolean delete(final UserLinkTo link) {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM user_link WHERE id='" + link.getUserId() +
                "' AND linkid='" + link.getId() +
                "' LIMIT 1;";

        try {
            SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
             log.error("UserLinkDao.delete():" + e.getMessage());
            noError = false;
        }

        return noError;
    }

    /**
     * Get a list of links
     * @param userId journal owner
     * @see UserLinkTo
     * @return  Collection of UserLinkTo's
     */
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
             log.error("UserLinkDao.view():" + e.getMessage());
        }

        return links;
    }

}
