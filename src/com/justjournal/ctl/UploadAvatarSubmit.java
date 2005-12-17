package com.justjournal.ctl;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Category;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * User: laffer1
 * Date: Dec 15, 2005
 * Time: 11:51:21 PM
 */
public class UploadAvatarSubmit extends Protected {
    private static Category log = Category.getInstance(UploadAvatarSubmit.class.getName());

    public String getMyLogin() {
        return this.currentLoginName();
    }

    protected String insidePerform() throws Exception {
        log.debug("Begin inside perform");
        int RowsAffected = 0;
        HttpServletRequest req = this.getCtx().getRequest();
        boolean isMultipart = FileUpload.isMultipartContent(req);

        if (isMultipart) {
            log.debug("we have a multipart file upload");
            DiskFileUpload upload = new DiskFileUpload();

            // set limits
            upload.setSizeMax(10 * 1024);
            upload.setSizeThreshold(10 * 1024);
            upload.setRepositoryPath("/tmp");  // should be changed.

            // process request
            List /*FileItem*/ items = upload.parseRequest(req);

            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    // do stuff here
                    //
                } else { // we're a file
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();

                    byte[] data = item.get();


                    Context ctx;
                    DataSource ds = null;
                    Connection conn = null;
                    PreparedStatement stmt = null; // insert statement
                    PreparedStatement stmtOn = null; // turn on avatar preference.
                    PreparedStatement stmtRemove = null; // delete old ones


                    try {
                        ctx = new InitialContext();
                        ds = (DataSource) ctx.lookup("java:comp/env/jdbc/jjDB");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        conn = ds.getConnection();

                        stmtRemove = conn.prepareStatement("DELETE FROM user_pic WHERE id=? LIMIT 1");
                        stmtRemove.setInt(1,this.currentLoginId());
                        stmtRemove.execute();

                        // do the insert of the image
                        stmt = conn.prepareStatement("INSERT INTO user_pic (id,mimetype,image) VALUES(?,?,?)");
                        stmt.setInt(1, this.currentLoginId());
                        stmt.setString(2, contentType);
                        stmt.setBytes(3, data);
                        stmt.execute();
                        RowsAffected = stmt.getUpdateCount();
                        stmt.close();

                        // turn on avatars.
                        stmtOn = conn.prepareStatement("UPDATE user_pref set show_avatar=? WHERE id=? LIMIT 1");
                        stmtOn.setString(1,"Y");
                        stmtOn.setInt(2, this.currentLoginId());
                        stmtOn.execute();

                        if (stmtOn.getUpdateCount() != 1)
                            log.debug("error turning on avatar.");
                        stmtOn.close();

                        conn.close();
                    } catch (Exception e) {
                        log.debug(e.getMessage());
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
                        }


                        try {
                            conn.close();
                        } catch (SQLException sqlEx) {
                            // ignore -- as we can't do anything about it here
                        }
                    }

                }
            }


        }

        if (RowsAffected == 1)
            return SUCCESS;
        else
            return ERROR;
    }
}
