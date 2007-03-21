/*
Copyright (c) 2006, Lucas Holt
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

package com.justjournal.core;

import com.justjournal.db.SQLHelper;
import sun.jdbc.rowset.CachedRowSet;
import org.apache.log4j.Category;

/**
 * Track the number of users, entry and comment statistics, and
 * other information.
 *
 * @author Lucas Holt
 * @version $Id: Statistics.java,v 1.3 2007/03/21 01:39:11 laffer1 Exp $
 */
public class Statistics {

    private static Category log = Category.getInstance(Statistics.class.getName());

    /**
     * Determine the number of users registered.
     *
     * @return The number of users or -1 on an error.
     */
    public int users() {
        int count = -1;
        String sql = "SELECT count(*) FROM user;";

        try {
            CachedRowSet rs = SQLHelper.executeResultSet(sql);

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
        } catch (Exception e) {
            log.error("users(): " + e.getMessage());
        }

        return count;
    }

    /**
     * Determine the number of journal entries posted.
     *
     * @return The number of entries or -1 on error.
     */
    public int entries() {
        String sql = "SELECT count(*) FROM entry;";

        return sqlCount(sql);
    }

    /**
     * Percentage of public entries as compared to total.
     * @return public entries as %
     */
    public int publicEntries() {
        int percent;
        String sql = "SELECT count(*) FROM entry WHERE security='2';";

        percent = (int)(((float)sqlCount(sql) / (float)entries()) * 100);

        if (log.isDebugEnabled())
            log.debug("publicEntries(): percent is " + percent);

        return percent;
    }

    /**
     * Percentage of friends entries as compared to total.
     * @return friends entries as %
     */
    public int friendsEntries() {
        int percent;
        String sql = "SELECT count(*) FROM entry WHERE security='1';";
        percent = (int) (((float)sqlCount(sql) / (float)entries()) * 100);

        if (log.isDebugEnabled())
            log.debug("friendsEntries(): percent is " + percent);

        return percent;
    }

    /**
     * Percentage of private entries as compared to total.
     * @return private entries as %
     */
    public int privateEntries() {
        int percent;
        String sql = "SELECT count(*) FROM entry WHERE security='0';";
        percent = (int) (((float)sqlCount(sql) / (float)entries()) * 100);

        if (log.isDebugEnabled())
            log.debug("privateEntries(): percent is " + percent);

        return percent;
    }

    /**
     * Determine the number of comments posted.
     *
     * @return The number of comments or -1 on error.
     */
    public int comments() {
        String sql = "SELECT count(*) FROM comments;";

        return sqlCount(sql);
    }

    /**
     * Determine the number of journal styles posted.
     *
     * @return The number of styles or -1 on error.
     */
    public int styles() {
        String sql = "SELECT count(*) FROM style;";

        return sqlCount(sql);
    }

    /**
     * Perform a sql query returning a scalar int value
     * typically form a count(*)
     * @param sql
     * @return int scalar from sql query
     */
    private int sqlCount(String sql) {
        int count = -1;

        if (log.isDebugEnabled())
            log.debug("sqlCount(): sql is " + sql);

        try {
            CachedRowSet rs = SQLHelper.executeResultSet(sql);

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
        } catch (Exception e) {
            log.error("sqlCount(): " + e.getMessage());
        }

        if (log.isDebugEnabled())
            log.debug("sqlCount(): count is " + count);

        return count;
    }

}
