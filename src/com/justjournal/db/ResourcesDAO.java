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

import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;  import java.sql.ResultSet;


/**
 * User: laffer1
 * Date: Jul 24, 2005
 * Time: 10:45:48 AM
 */
public final class ResourcesDAO {

    private static final Logger log = Logger.getLogger(ResourcesDAO.class.getName());

    public static final boolean add(ResourceTo res) {

        if (log.isDebugEnabled())
            log.debug("Starting add()");

        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                new StringBuilder().append("INSERT INTO resources (name, active, security) values('").append(res.getName()).append("','").append((res.getActive()) ? "1" : "0").append("','").append(res.getSecurityLevel()).append("');").toString();

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

    public static final boolean update(ResourceTo res) {

        if (log.isDebugEnabled())
            log.debug("Starting edit()");

        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                new StringBuilder().append("UPDATE resources SET active='").append(res.getActive()).append("',").append(" security='").append((res.getActive()) ? "1" : "0").append("'").append(" WHERE id='").append(res.getId()).append("' LIMIT 1;").toString();

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

    public static final boolean delete(ResourceTo res) {

        if (log.isDebugEnabled())
            log.debug("Starting delete()");

        boolean noError = true;
        int records = 0;

        final String sqlStmt = "DELETE FROM resources WHERE id='" + res.getId() +
                "' AND name='" + res.getName() + "' LIMIT 1;";

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

    public static final ResourceTo view(final String uri) {

        if (log.isDebugEnabled())
            log.debug("Starting view()");

        ResultSet RS = null;
        ResourceTo res = null;
        final String sqlStatement = "SELECT id, active, security FROM resources WHERE name='"
                + uri + "';";

        try {
            RS = SQLHelper.executeResultSet(sqlStatement);

            if (RS.next()) {
                res = new ResourceTo();

                res.setId(RS.getInt("id"));
                res.setName(uri);
                res.setActive(RS.getBoolean("active"));
                res.setSecurityLevel(RS.getInt("security"));
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

        return res;
    }

}
