package com.justjournal.db;

import com.justjournal.SQLHelper;
import org.apache.log4j.Category;
import sun.jdbc.rowset.CachedRowSet;

/**
 * User: laffer1
 * Date: Jul 24, 2005
 * Time: 10:45:48 AM
 */
public class ResourcesDAO {

    private static Category log = Category.getInstance(ResourcesDAO.class.getName());

    public boolean add(ResourceTo res) {

        if (log.isDebugEnabled())
            log.debug("Starting add()");

        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "INSERT INTO resources (name, active, security) values('"
                + res.getName() + "','" + ((res.getActive() == true) ? "1" : "0") + "','"
                + res.getSecurityLevel()
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

    public boolean update(ResourceTo res) {

        if (log.isDebugEnabled())
            log.debug("Starting update()");

        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "UPDATE resources SET active='" + res.getActive() + "'," +
                " security='" + ((res.getActive() == true) ? "1" : "0") + "'" +
                " WHERE id='" + res.getId() + "' LIMIT 1;";

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

    public boolean delete(ResourceTo res) {

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

    public ResourceTo view(final String uri) {

        if (log.isDebugEnabled())
            log.debug("Starting view()");

        CachedRowSet RS = null;
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
