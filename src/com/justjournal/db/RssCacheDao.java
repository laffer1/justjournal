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

import org.apache.log4j.Category;
import sun.jdbc.rowset.CachedRowSet;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Apr 27, 2005
 * Time: 9:46:51 PM
 *
 * @author Lucas Holt
 * @version 1.0
 */
public final class RssCacheDao {

    private static final Category log = Category.getInstance(RssCacheDao.class.getName());

    public static final boolean add(RssCacheTo rss) {

        if (log.isDebugEnabled())
            log.debug("Starting add()");

        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "INSERT INTO rss_cache (`interval`, lastupdated, uri, content) values('"
                        + rss.getInterval() + "', NOW(),'"
                        + rss.getUri() + "','"
                        + rss.getContent()
                        + "');";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;

            if (log.isDebugEnabled())
                log.debug("SQL Query is: " + sqlStmt);
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    public static final boolean update(RssCacheTo rss) {

        if (log.isDebugEnabled())
            log.debug("Starting update()");

        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "UPDATE rss_cache SET lastupdated=now()," +
                        " content='" + rss.getContent() + "' WHERE uri='" +
                        rss.getUri() + "';";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;

            if (log.isDebugEnabled())
                log.debug("SQL Query is: " + sqlStmt);
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    public static final boolean delete(RssCacheTo rss) {

        if (log.isDebugEnabled())
            log.debug("Starting delete()");

        boolean noError = true;
        int records = 0;

        final String sqlStmt = "DELETE FROM rss_cache WHERE id='" + rss.getId() +
                "' AND uri='" + rss.getUri() + "' LIMIT 1;";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;

            if (log.isDebugEnabled())
                log.debug("SQL Query is: " + sqlStmt);
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    public static final RssCacheTo view(final String uri) {

        if (log.isDebugEnabled())
            log.debug("Starting view()");

        CachedRowSet RS = null;
        DateTime dt = new DateTimeBean();
        RssCacheTo rss = null;
        final String sqlStatement = "SELECT id, `interval`, lastupdated, uri, content FROM rss_cache WHERE uri='"
                + uri + "';";

        try {
            RS = SQLHelper.executeResultSet(sqlStatement);

            if (RS.next()) {
                rss = new RssCacheTo();

                rss.setId(RS.getInt("id"));
                rss.setInterval(RS.getInt("interval"));
                dt.set(RS.getString("lastupdated"));
                rss.setLastUpdated(dt);
                rss.setUri(RS.getString("uri"));
                rss.setContent(RS.getString("content"));
            }

            RS.close();
        } catch (Exception e1) {
            if (log.isDebugEnabled())
                log.debug("SQL Query is: " + sqlStatement);

            try {
                if (RS != null)
                    RS.close();
            } catch (java.sql.SQLException e2) {
                // nothing to do.
            }
        }

        return rss;
    }

}
