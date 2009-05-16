package com.justjournal.ctl;

import com.justjournal.JustJournalBaseServlet;
import com.justjournal.User;
import com.justjournal.WebError;
import com.justjournal.utility.FileIO;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: laffer1
 * Date: Mar 31, 2007
 * Time: 11:34:00 AM
 */
public class DeletePicture extends JustJournalBaseServlet {
    private static final Logger log = Logger.getLogger(AddPicture.class.getName());

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {
        boolean blnError = false;
        int RowsAffected = 0;

        // Retreive username
        String userName;
        userName = (String) session.getAttribute("auth.user");

        // Retreive user id
        Integer userIDasi = (Integer) session.getAttribute("auth.uid");
        // convert Integer to int type
        int userID = 0;
        if (userIDasi != null) {
            userID = userIDasi.intValue();
        }

        /* Make sure we are logged in */
        if (userID < 1) {
            WebError.Display("Error", "You must be logged in to upload a picture.", sb);
            return;
        }

        String picIDstr = request.getParameter("id");
        if (picIDstr == null) {
            WebError.Display("Error", "A picture id must be provided.", sb);
            return;
        }

        Integer picID = Integer.parseInt(picIDstr);
        if (picID.intValue() < 1) {
            WebError.Display("Error", "Invalid picture id.", sb);
            return;
        }

        Context ctx;
        DataSource ds = null;
        Connection conn = null;
        PreparedStatement stmt = null; // insert statement

        try {
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/jjDB");
        } catch (Exception e) {
            log.error(e.getMessage());
            blnError = true;
            WebError.Display("Database", "Could not retrieve database resources.", sb);
        }

        if (!blnError) {
            try {
                conn = ds.getConnection();

                // do the insert of the image
                stmt = conn.prepareStatement("DELETE from user_images where id=? and owner=?;");
                stmt.setInt(1, picID.intValue());
                stmt.setInt(2, userID);
                stmt.execute();
                RowsAffected = stmt.getUpdateCount();
                stmt.close();

                conn.close();

                if (log.isDebugEnabled())
                    log.debug("RowsAffected: " + RowsAffected);
            } catch (Exception e) {
                log.error(e.getMessage());
                // throw new Exception("Error getting connect or executing it", e);
            } finally {
                /*
                * Close any JDBC instances here that weren't
                * explicitly closed during normal code path, so
                * that we don't 'leak' resources...
                */

                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException sqlEx) {
                    // ignore -- as we can't do anything about it here
                    log.error(sqlEx.getMessage());
                }

                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException sqlEx) {
                    // ignore -- as we can't do anything about it here
                    log.error(sqlEx.getMessage());
                }
            }
        }


        if (!blnError && RowsAffected != 1) {
            WebError.Display("Database",
                    "Database Error.  Please try again later.",
                    sb);
            blnError = true;
        }

        if (log.isDebugEnabled())
            log.debug("Write out Response  ");

        if (!blnError)
            htmlOutput(sb, userName);
    }

    private void htmlOutput(StringBuffer sb, String userName) {

        try {
            String template = FileIO.ReadTextFile("/home/jj/docs/journal_template.inc");
            String loginMenu;
            StringBuilder content = new StringBuilder();
            User user = new User(userName);

            content.append("\t\t<h2>Preferences</h2>");
            content.append(endl);
            content.append("\t<p>You are logged in as <a href=\"/users/").append(userName).append("\"><img src=\"/images/userclass_16.png\" alt=\"user\" />").append(userName).append("</a>.</p>");
            content.append(endl);
            content.append("\t<h3>Remove Picture</h3>");
            content.append(endl);
            content.append("\t<p>Picture Deleted.</p>");
            content.append(endl);
            content.append("\t<p><a href=\"/prefs/index.jsp\">Return to preferences.</a></p>");
            content.append(endl);

            // User is logged in.. give them the option to log out.
            loginMenu = ("\t\t<a href=\"/prefs/index.jsp\">Preferences</a><br />");

            loginMenu += ("\t\t<a href=\"/logout.jsp\">Log Out</a>");

            template = template.replaceAll("\\$JOURNAL_TITLE\\$", user.getJournalName());
            template = template.replaceAll("\\$USER\\$", userName);
            template = template.replaceAll("\\$USER_STYLESHEET\\$", user.getStyleId() + ".css");
            template = template.replaceAll("\\$USER_STYLESHEET_ADD\\$", user.getStyleDoc());
            if (user.showAvatar()) {
                String av = "\t\t<img alt=\"avatar\" src=\"/image?id="
                        + user.getUserId() + "\"/>";
                template = template.replaceAll("\\$USER_AVATAR\\$", av);
            } else
                template = template.replaceAll("\\$USER_AVATAR\\$", "");
            template = template.replaceAll("\\$RECENT_ENTRIES\\$", "");
            template = template.replaceAll("\\$LOGIN_MENU\\$", loginMenu);
            template = template.replaceAll("\\$CONTENT\\$", content.toString());
            sb.append(template);
        } catch (Exception e) {
            WebError.Display("Internal Error", "Error dislaying page. " + e.getMessage(), sb);
        }
    }

    public String getServletInfo() {
        return "Remove Picture";
    }
}
