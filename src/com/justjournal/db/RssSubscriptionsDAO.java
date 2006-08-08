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

package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Mar 19, 2005
 * Time: 9:17:55 PM
 */
public final class RssSubscriptionsDAO {

    public static boolean add(RssSubscriptionsTO rss) {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "INSERT INTO rss_subscriptions (id,uri) values('"
                        + rss.getId() + "','"
                        + rss.getUri()
                        + "');";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    public static boolean delete(RssSubscriptionsTO rss) {
        boolean noError = true;
        int records = 0;

        final String sqlStmt = "DELETE FROM rss_subscriptions WHERE subid='" + rss.getSubscriptionId() +
                "' AND id='" + rss.getId() + "' LIMIT 1;";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    public static Collection<RssSubscriptionsTO> view(final int userId) {
        ArrayList<RssSubscriptionsTO> rssfeeds = new ArrayList<RssSubscriptionsTO>(10);
        CachedRowSet RS = null;
        RssSubscriptionsTO rss;
        final String sqlStatement = "SELECT uri, subid FROM rss_subscriptions WHERE id='"
                + userId + "';";

        try {
            RS = SQLHelper.executeResultSet(sqlStatement);

            while (RS.next()) {
                rss = new RssSubscriptionsTO();

                rss.setId(userId);
                rss.setUri(RS.getString("uri"));
                rss.setSubscriptionId(RS.getInt("subid"));

                rssfeeds.add(rss);
            }

            RS.close();
        } catch (Exception e1) {
            try {
                if (RS != null)
                    RS.close();
            } catch (java.sql.SQLException e2) {
                // nothing to do.
            }
        }

        return rssfeeds;
    }

}
