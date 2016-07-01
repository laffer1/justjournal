/*-
 * Copyright (c) 2006-2011 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.justjournal.ctl;

import com.justjournal.utility.ServletUtilities;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * Image viewer servlet to display user pics and other images
 * from the database.
 * <p/>
 * User: laffer1
 * Date: Nov 22, 2005
 * Time: 9:31:28 PM
 *
 * @version $Id: Image.java,v 1.12 2011/07/02 01:30:53 laffer1 Exp $
 */
public final class Image extends HttpServlet {

    private static final Logger log = Logger.getLogger(Image.class);

    // processes get requests
    protected final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Integer id;
        Context ctx;
        DataSource ds = null;
        Connection conn;
        PreparedStatement stmt;

        try {
            id = new Integer(request.getParameter("id"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (id < 1) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            response.reset();
            response.setHeader("Expires", ServletUtilities.createExpiresHeader(180));
            try {
                ctx = new InitialContext();
                ds = (DataSource) ctx.lookup("java:comp/env/jdbc/jjDB");
            } catch (Exception e) {
                log.debug(e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            }

            conn = ds.getConnection();
            stmt = conn.prepareStatement("call getimage(?)");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                response.setContentType(rs.getString("mimetype").trim());
                BufferedInputStream img = new BufferedInputStream(rs.getBinaryStream("image"));
                byte[] buf = new byte[4 * 1024]; // 4k buffer
                int len;
                while ((len = img.read(buf, 0, buf.length)) != -1)
                    byteArrayOutputStream.write(buf, 0, len);

                response.setContentLength(byteArrayOutputStream.size());
                final ServletOutputStream outputStream = response.getOutputStream();
                byteArrayOutputStream.writeTo(outputStream);
                outputStream.flush();
                outputStream.close();
            } else
                response.sendError(HttpServletResponse.SC_NOT_FOUND);

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            log.warn("Could not load image: " + e.toString());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}