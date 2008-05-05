/*
Copyright (c) 2008 Lucas Holt
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

import com.justjournal.utility.StringUtil;
import sun.jdbc.rowset.CachedRowSet;

import java.util.ArrayList;

/**
 * Manipulate trackback storage
 *
 * @author Lucas Holt
 * @version $Id: TrackbackDao.java,v 1.1 2008/05/05 07:55:22 laffer1 Exp $
 *          User: laffer1
 *          Date: May 5, 2008
 *          Time: 3:06:12 AM
 */
public final class TrackbackDao {

    /**
     * Add a trackback on a journal entry
     *
     * @param tb A user comment to change
     * @return true if no error occured.
     */
    public boolean add(final TrackbackTo tb) {
        /*
        id  int 10  unsigned auto  (trackback unit id)
        eid (entry it refers to)
        date datetime
        subject  (title, name)   varchar 150
        body   (comment, excert)
        email (author email)
        name  (author name)
        type  (trackback, pingback, post-it)
        */
        final String sqlStmt =
                "Insert INTO trackback (id,eid,date,subject,body,author_email,author_name,type) values(NULL,'"
                        + tb.getEntryId() + "','"
                        + tb.getDate() + "','"
                        + StringUtil.replace(tb.getSubject(), '\'', "\\\'") + "','"
                        + StringUtil.replace(tb.getBody(), '\'', "\\\'") + "','"
                        + tb.getAuthorEmail() + "','"
                        + tb.getAuthorName() + "','"
                        + tb.getType().toString()
                        + "');";

        return BaseDao.add(sqlStmt);
    }

    /**
     * change a trackback on a journal entry.
     *
     * @param tb A user trackback to change
     * @return true if no error occured.
     */
    public boolean update(final TrackbackTo tb) {
        // TODO: Consider allowing alter of type?
        final String sqlStmt = "Update trackback SET subject='" +
                StringUtil.replace(tb.getSubject(), '\'', "\\\'")
                + "', body='" +
                StringUtil.replace(tb.getBody(), '\'', "\\\'")
                + "', author_email='" + tb.getAuthorEmail()
                + "', author_name='" + tb.getAuthorName()
                + "' WHERE id='" +
                tb.getId() + "' AND eid='" +
                tb.getEntryId()
                + "' LIMIT 1;";

        return BaseDao.edit(sqlStmt);
    }

    /**
     * Delete a trackback by the unique ID
     *
     * @param trackbackId user trackback to delete
     * @return true if no errors occured
     */
    public static boolean delete(final int trackbackId) {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM trackback WHERE id='" + trackbackId + "' LIMIT 1;";

        if (trackbackId > 0) {
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
     * Deletes trackbacks belonging to a specific entry id.
     * used by the delete entry logic.  Not recommended for
     * direct calls by Users.
     *
     * @param entryId blog entry id
     * @return true on success, false on any error.
     */
    public static boolean deleteByEntry(final int entryId) {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM trackback WHERE eid='" + entryId + "' LIMIT 1;";

        if (entryId > 0) {
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
     * Fetch details about a trackback entry
     *
     * @param trackbackId unique identity for trackback
     * @return A trackback
     */
    public static TrackbackTo viewSingle(final int trackbackId) {
        CachedRowSet rs = null;
        final TrackbackTo tb = new TrackbackTo();
        final String sqlStmt =
                "Select trackback.date, trackback.subject, trackback.body, trackback.id, trackback.author_email, trackback.author_name, trackback.type, trackback.eid FROM trackback WHERE trackback.id='"
                        + trackbackId + "';";

        try {
            rs = SQLHelper.executeResultSet(sqlStmt);

            if (rs.next()) {
                // set the properites on the bean
                tb.setId(rs.getInt("trackback.id"));
                tb.setDate(rs.getString("date"));
                tb.setSubject(rs.getString("subject"));
                tb.setBody(rs.getString("body"));
                tb.setEntryId(rs.getInt("eid"));
                tb.setAuthorName(rs.getString("author_name"));
                tb.setAuthorEmail(rs.getString("author_email"));

                String type = rs.getString("type");

                if (type.compareTo("trackback") == 0)
                    tb.setType(TrackbackType.trackback);
                else if (type.compareTo("pingback") == 0)
                    tb.setType(TrackbackType.pingback);
                else if (type.compareTo("postit") == 0)
                    tb.setType(TrackbackType.postit);
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

        return tb;
    }

    /**
     * Get all the trackbacks on a single blog entry
     *
     * @param entryId entry to get trackbacks
     * @return List of trackbacks
     */
    public static ArrayList<TrackbackTo> view(final int entryId) {
        final ArrayList<TrackbackTo> tbs = new ArrayList<TrackbackTo>(5);  // 5 is average comments on entry?
        CachedRowSet rs = null;
        TrackbackTo tb;
        final String sqlStmt =
                "Select trackback.date, trackback.subject, trackback.body, trackback.id, trackback.author_email, trackback.author_name, trackback.type, trackback.eid FROM trackback WHERE trackback.eid='"
                        + entryId + "';";

        try {
            rs = SQLHelper.executeResultSet(sqlStmt);

            while (rs.next()) {
                tb = new TrackbackTo();

                // set the properites on the bean
                tb.setId(rs.getInt("trackback.id"));
                tb.setDate(rs.getString("date"));
                tb.setSubject(rs.getString("subject"));
                tb.setBody(rs.getString("body"));
                tb.setEntryId(rs.getInt("eid"));
                tb.setAuthorName(rs.getString("author_name"));
                tb.setAuthorEmail(rs.getString("author_email"));

                String type = rs.getString("type");

                if (type.compareTo("trackback") == 0)
                    tb.setType(TrackbackType.trackback);
                else if (type.compareTo("pingback") == 0)
                    tb.setType(TrackbackType.pingback);
                else if (type.compareTo("postit") == 0)
                    tb.setType(TrackbackType.postit);

                // add to the array list
                tbs.add(tb);
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

        return tbs;

    }
}
