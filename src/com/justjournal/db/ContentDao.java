package com.justjournal.db;

import com.justjournal.SQLHelper;
import sun.jdbc.rowset.CachedRowSet;

import java.util.ArrayList;

/**
 * User: laffer1
 * Date: Aug 15, 2005
 * Time: 12:00:16 AM
 */
public class ContentDao {

    /**
     * Add content to the JJ CMS
     *
     * @param content  Content to add
     * @return true if no error occured.
     */
    public boolean add(final ContentTo content) {
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
     *
     * This does not allow changes to userId or
     * verification at this time.
     *
     * @param content A user comment to change
     * @return true if no error occured.
     */
    public boolean update(final ContentTo content) {
        boolean noError = true;

        final String sqlStmt = "Update comments SET mimeType='" +
                content.getMimeType()
                + "', preferred='" +
                + (content.getPreferred() ? 1:0)
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

    public boolean delete(final int id) {
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

    public ContentTo viewSingle(final int id) {
        CachedRowSet rs = null;
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

    public ArrayList view(final int uriId) {
        final ArrayList contents = new ArrayList(5);  // 5 is average comments on entry?
        CachedRowSet rs = null;
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
