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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.SortOrder;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.*;


/**
 * Data access for User Link list
 *
 * @author Lucas Holt
 * @version $Id: UserLinkDao.java,v 1.13 2012/06/23 18:15:31 laffer1 Exp $
 * @see UserLinkTo
 */
public final class UserLinkDao {

    private static final Logger log = Logger.getLogger(UserLinkDao.class.getName());

    /**
     * Delete a link from the userlink list.
     *
     * @param link user link to delete
     * @return true on success
     * @see UserLinkTo
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
     *
     * @param userId journal owner
     * @return Collection of UserLinkTo's
     * @see UserLinkTo
     */
    public
    @NotNull
    @Deprecated
    static Collection<UserLinkTo> view(int userId) {
        ArrayList<UserLinkTo> links = new ArrayList<UserLinkTo>(10);
        final String sql = "SELECT * FROM user_link WHERE id='" + userId + "';";

        try {
            ResultSet rs = SQLHelper.executeResultSet(sql);

            while (rs.next()) {
                links.add(new UserLinkTo(rs.getInt("linkid"), rs.getInt("id"),
                        rs.getString("title"), rs.getString("uri")));

            }
            rs.close();

        } catch (Exception e) {
            log.error("UserLinkDao.get():" + e.getMessage());
        }

        return links;
    }

    public
    @Nullable
    static UserLinkTo get(int linkId) {
        UserLinkTo link = null;

        try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            com.justjournal.model.UserLink linkItem =
                    Cayenne.objectForPK(dataContext, com.justjournal.model.UserLink.class, linkId);
            link = new UserLinkTo();
            link.setId(linkId);
            link.setTitle(linkItem.getTitle());
            link.setUri(linkItem.getUri());
            link.setUserId(Cayenne.intPKForObject(linkItem.getUserLinkToUser()));
        } catch (Exception e1) {
            log.error(e1);
        }

        return link;
    }

    public
    @Nullable
    static List<UserLinkTo> list(String username) {

        List<UserLinkTo> userLinkToList = new ArrayList<UserLinkTo>();
        ObjectContext dataContext = DataContext.getThreadObjectContext();

        UserLinkTo userLinkTo;
        Expression exp = Expression.fromString("userLinkToUser.username = $user");

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", username);
            exp = exp.expWithParameters(map);
            SelectQuery query = new SelectQuery(com.justjournal.model.UserLink.class, exp);
            List<Ordering> orderings = new ArrayList<Ordering>();
            orderings.add(new Ordering("title", SortOrder.ASCENDING_INSENSITIVE));
            query.addOrderings(orderings);
            @SuppressWarnings("unchecked")
            List<com.justjournal.model.UserLink> list = dataContext.performQuery(query);

            for (com.justjournal.model.UserLink linkItem : list) {
                userLinkTo = new UserLinkTo();
                userLinkTo.setId(Cayenne.intPKForObject(linkItem));
                userLinkTo.setTitle(linkItem.getTitle());
                userLinkTo.setUri(linkItem.getUri());
                userLinkTo.setUserId(Cayenne.intPKForObject(linkItem.getUserLinkToUser()));
                userLinkToList.add(userLinkTo);
            }
        } catch (Exception e1) {
            log.error(e1);
        }

        return userLinkToList;
    }

    /**
     * Add a link to the user link list.
     *
     * @param link A new hyperlink to add.
     * @return true if no error occured.
     * @see UserLinkTo
     */
    public boolean add(final UserLinkTo link) {

        final String sqlStmt = "Insert into user_link (linkid,id,title,uri) VALUES('"
                + link.getId() + "','" + link.getUserId() + "','" + link.getTitle() + "','"
                + link.getUri() + "');";

        return BaseDao.add(sqlStmt);
    }
}
