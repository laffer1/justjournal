/*
Copyright (c) 2005-2007, Lucas Holt
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

package com.justjournal.ctl;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Lucas Holt
 * @version $Id: UploadAvatarSubmit.java,v 1.9 2012/06/23 18:15:31 laffer1 Exp $
 * 
 * User: laffer1
 * Date: Dec 15, 2005
 * Time: 11:51:21 PM
 */
public class UploadAvatarSubmit extends Protected {
    private static final Logger log = Logger.getLogger(UploadAvatarSubmit.class.getName());

    public String getMyLogin() {
        return currentLoginName();
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
            upload.setSizeMax(15 * 1024);
            upload.setSizeThreshold(15 * 1024);
            upload.setRepositoryPath("/tmp");  // should be changed.

            // process request
            List /*FileItem*/ items = upload.parseRequest(req);

            for (Object item1 : items) {
                FileItem item = (FileItem) item1;

                if (item.isFormField()) {
                    // do stuff here... ignore for now
                    //
                } else { // we're a file
                    //String fieldName = item.getFieldName();
                    //String fileName = item.getName();
                    String contentType = item.getContentType();
                    //boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();

                    // must be large enough
                    if (sizeInBytes > 500) {
                        byte[] data = item.get();

                        Context ctx;
                        DataSource ds = null;
                        Connection conn = null;
                        PreparedStatement stmt = null; // create statement
                        PreparedStatement stmtOn = null; // turn on avatar preference.
                        PreparedStatement stmtRemove = null; // delete old ones

                        try {
                            ctx = new InitialContext();
                            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/jjDB");
                        } catch (Exception e) {
                            log.debug(e.getMessage());
                            this.addError("Database", "Could not retrieve database resources.");
                        }

                        try {
                            conn = ds.getConnection();

                            stmtRemove = conn.prepareStatement("DELETE FROM user_pic WHERE id=? LIMIT 1");
                            stmtRemove.setInt(1, this.currentLoginId());
                            stmtRemove.execute();

                            // do the create of the image
                            stmt = conn.prepareStatement("INSERT INTO user_pic (id, date_modified, mimetype, image) VALUES(?,Now(),?,?)");
                            stmt.setInt(1, this.currentLoginId());
                            stmt.setString(2, contentType);
                            stmt.setBytes(3, data);
                            stmt.execute();
                            RowsAffected = stmt.getUpdateCount();
                            stmt.close();

                            // turn on avatars.
                            stmtOn = conn.prepareStatement("UPDATE user_pref set show_avatar=? WHERE id=? LIMIT 1");
                            stmtOn.setString(1, "Y");
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
                                log.debug(sqlEx.getMessage());
                            }

                            try {
                                stmtOn.close();
                            } catch (SQLException sqlEx) {
                                // ignore -- as we can't do anything about it here
                                log.debug(sqlEx.getMessage());
                            }

                            try {
                                stmtRemove.close();
                            } catch (SQLException sqlEx) {
                                // ignore -- as we can't do anything about it here
                                log.debug(sqlEx.getMessage());
                            }

                            try {
                                conn.close();
                            } catch (SQLException sqlEx) {
                                // ignore -- as we can't do anything about it here
                                log.debug(sqlEx.getMessage());
                            }
                        }
                    } else {
                        log.debug("File size is too small");
                        addError("File", "File size is too small.");
                    }
                }
            }
        }

        if (hasErrors() || RowsAffected != 1)
            return ERROR;
        else
            return SUCCESS;
    }
}
