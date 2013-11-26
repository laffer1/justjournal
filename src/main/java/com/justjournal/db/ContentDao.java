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

import com.sun.istack.internal.NotNull;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * User: laffer1 Date: Aug 15, 2005 Time: 12:00:16 AM
 */
public final class ContentDao {

    /**
     * Add content to the JJ CMS
     *
     * @param content Content to add
     * @return true if no error occurred.
     */
    public static boolean add(final ContentTo content) {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "Insert INTO content (uri_id, userId, mime_type, preferred, datasize, data, metadata) values('"
                        + content.getUriId() + "','"
                        + content.getUserId() + "','"
                        + content.getMimeType() + "','"
                        + content.getPreferred() + "','"
                        + content.getDataSize() + "','"
                        + content.getData() + "','"
                        + content.getMetaData() + "');";

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
     * change a comment on a journal entry.
     * <p/>
     * This does not allow changes to userId or verification at this time.
     *
     * @param content A user comment to change
     * @return true if no error occured.
     */
    public static boolean update(final ContentTo content) {
        boolean noError = true;

        final String sqlStmt = "Update comments SET mimeType='" +
                content.getMimeType()
                + "', preferred='" +
                +(content.getPreferred() ? 1 : 0)
                + "', datasize='"
                + content.getDataSize()
                + "', data='"
                + content.getData()
                + "', metadata='"
                + content.getMetaData()
                + "' WHERE id='" +
                content.getId() + "' LIMIT 1;";
        try {
            SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        return noError;
    }

    public static boolean delete(final int id) {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM content WHERE id='" + id + " LIMIT 1;";

        if (id > 0) {
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

    public @NotNull static ContentTo viewSingle(final int id) {
        ResultSet rs = null;
        final ContentTo content = new ContentTo();
        final String sqlStmt =
                "SELECT * FROM content WHERE id='" + id + " LIMIT 1;";

        try {
            rs = SQLHelper.executeResultSet(sqlStmt);

            if (rs.next()) {
                // set the properites on the bean
                content.setId(rs.getInt("id"));
                content.setUriId(rs.getInt("uri_id"));
                content.setUserId(rs.getInt("userid"));
                content.setMimeType(rs.getString("mime_type"));
                content.setPreferred(rs.getBoolean("preferred"));
                content.setDataSize(rs.getInt("datasize"));
                content.setData(rs.getBytes("data"));
                content.setMetaData(rs.getString("metadata"));
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

        return content;
    }

    public @NotNull
    static ArrayList<ContentTo> view(final int uriId) {
        final ArrayList<ContentTo> contents = new ArrayList<ContentTo>();
        ResultSet rs = null;
        ContentTo content;
        final String sqlStmt =
                "SELECT * from content WHERE uri_id='" + uriId + "' ORDER BY preferred;";

        try {
            rs = SQLHelper.executeResultSet(sqlStmt);

            while (rs.next()) {
                // create a new comment to put in the array list
                content = new ContentTo();

                // set the properites on the bean
                content.setId(rs.getInt("id"));
                content.setUriId(rs.getInt("uri_id"));
                content.setUserId(rs.getInt("userid"));
                content.setMimeType(rs.getString("mime_type"));
                content.setPreferred(rs.getBoolean("preferred"));
                content.setDataSize(rs.getInt("datasize"));
                content.setData(rs.getBytes("data"));
                content.setMetaData(rs.getString("metadata"));

                // add to the array list
                contents.add(content);
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

        return contents;

    }
}
