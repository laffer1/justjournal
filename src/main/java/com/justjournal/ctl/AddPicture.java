package com.justjournal.ctl;

import com.justjournal.UserImpl;
import com.justjournal.WebError;
import com.justjournal.utility.FileIO;
import com.justjournal.utility.StringUtil;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
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
import java.util.List;

/**
 * User: laffer1 Date: Mar 11, 2007 Time: 4:56:26 PM
 */
public class AddPicture extends JustJournalBaseServlet {
    private static final Logger log = Logger.getLogger(AddPicture.class.getName());

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {
        boolean blnError = false;
        int RowsAffected = 0;
        String title = "untitled";

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

        boolean isMultipart = FileUpload.isMultipartContent(request);
        log.debug("Multipart: " + isMultipart);

        if (isMultipart) {
            log.debug("we have a multipart file upload");
            DiskFileUpload upload = new DiskFileUpload();

            // set limits
            upload.setSizeMax(1000 * 1024 * 10);
            upload.setSizeThreshold(1000 * 1024 * 10);
            upload.setRepositoryPath("/tmp");  // should be changed.

            // process request
            List /* FileItem */ items;
            FileItem fi = null;
            try {
                log.debug("Try to parse request");
                items = upload.parseRequest(request);

                for (Object item1 : items) {
                    log.debug("inside for each loop");
                    FileItem item = (FileItem) item1;

                    if (item.getFieldName().equals("title")) {
                        if (StringUtil.lengthCheck(item.getString(), 2, 150))
                            title = item.getString();
                        else {
                            WebError.Display("Input Error", "Title must be 2 to 150 characters.", sb);
                            return;
                        }
                    } else {
                        if (!item.isFormField() && item.getFieldName().equals("pic"))
                            fi = item;
                    }
                }


                if (fi != null) {
                    // we're a file
                    //String fieldName = item.getFieldName();
                    //String fileName = item.getName();
                    String contentType = fi.getContentType();
                    //boolean isInMemory = item.isInMemory();
                    long sizeInBytes = fi.getSize();

                    // must be large enough
                    if (sizeInBytes > 500) {
                        byte[] data = fi.get();

                        Context ctx;
                        DataSource ds = null;
                        Connection conn = null;
                        PreparedStatement stmt = null; // create statement

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

                                // do the create of the image
                                stmt = conn.prepareStatement("INSERT INTO user_images (owner,title,modified,mimetype,image) VALUES(?,?,now(),?,?)");
                                stmt.setInt(1, userID);
                                stmt.setString(2, title);
                                stmt.setString(3, contentType);
                                stmt.setBytes(4, data);
                                stmt.execute();
                                RowsAffected = stmt.getUpdateCount();
                                stmt.close();

                                conn.close();

                                if (log.isDebugEnabled())
                                    log.debug("RowsAffected: " + RowsAffected);
                            } catch (Exception e) {
                                log.error(e.getMessage());
                                throw new Exception("Error getting connect or executing it", e);
                            } finally {
                                    /*
                                    * Close any JDBC instances here that weren't
                                    * explicitly closed during normal code path, so
                                    * that we don't 'leak' resources...
                                    */

                                try {
                                    stmt.close();
                                } catch (SQLException sqlEx) {
                                    // ignore -- as we can't do anything about it here
                                    log.error(sqlEx.getMessage());
                                }

                                try {
                                    conn.close();
                                } catch (SQLException sqlEx) {
                                    // ignore -- as we can't do anything about it here
                                    log.error(sqlEx.getMessage());
                                }
                            }
                        }
                    } else {
                        log.error("File size is too small");
                        WebError.Display("File", "File size is too small.", sb);
                        blnError = true;
                    }
                }
            } catch (Exception e) {
                blnError = true;
                log.error(e.getMessage());
                WebError.Display("Error",
                        "Unknown",
                        sb);
            }

        } /* is multipart */

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
            UserImpl user = new UserImpl(userName);

            content.append("\t\t<h2>Preferences</h2>");
            content.append(endl);
            content.append("\t<p>You are logged in as <a href=\"/users/").append(userName).append("\"><img src=\"/images/userclass_16.png\" alt=\"user\" />").append(userName).append("</a>.</p>");
            content.append(endl);
            content.append("\t<h3>Add Picture</h3>");
            content.append(endl);
            content.append("\t<p>Picture Uploaded.</p>");
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
        return "Add Picture to Blog";
    }
}
