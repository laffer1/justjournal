package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: laffer1
 * Date: Dec 22, 2005
 * Time: 2:58:10 PM
 */
public final class UserLinkDao {
    private BaseDao dao = new BaseDao();

    /**
     * Add a link to the user link list.
     *
     * @param link A new hyperlink to add.
     * @return true if no error occured.
     */
    public boolean add(final UserLinkTo link) {

        final String sqlStmt = "Insert into user_link (id,title,uri) VALUES('"
                + link.getUserId() + "','" + link.getTitle() + "','"
                + link.getUri() + "');";

        return dao.add(sqlStmt);
    }

    public boolean delete(final UserLinkTo link) {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM user_link WHERE id='" + link.getUserId() +
                "' AND uri='" + link.getUri() +
                "' AND title='" + link.getTitle() + "' LIMIT 1;";

        try {
            SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        return noError;
    }

    public Collection view(int userId) {
        ArrayList links = new ArrayList(10);
        String sql = "SELECT * FROM user_link WHERE id='" + userId + "';";

        try {
            CachedRowSet rs = SQLHelper.executeResultSet(sql);

            while (rs.next()) {
                links.add(new UserLinkTo(rs.getInt("id"),
                        rs.getString("title"), rs.getString("uri")));

            }
        } catch (Exception e) {

        }

        return links;
    }

}
