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

import com.justjournal.services.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Image viewer servlet to display user pics (avatars)
 * <p/>
 * User: laffer1 Date: Nov 22, 2005 Time: 9:31:28 PM
 *
 * @version $Id$
 */
@Slf4j
@RequestMapping("/image")
@Controller
public class ImageController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/{id}")
    public ResponseEntity<byte[]> getByPath(@PathVariable("id") final int id) throws IOException {
        return get(id);
    }

    @RequestMapping("")
    public ResponseEntity<byte[]> get(@RequestParam("id") final int id) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        DataSource ds;
        Connection conn;
        PreparedStatement stmt = null;

        if (id < 1) {
            return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        }

        try {
            ds = jdbcTemplate.getDataSource();
            conn = ds.getConnection();
            stmt = conn.prepareStatement("CALL getimage(?)");
            stmt.setInt(1, id);
            final ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                final BufferedInputStream img = new BufferedInputStream(rs.getBinaryStream("image"));
                byte[] buf = new byte[4 * 1024]; // 4k buffer
                int len;
                while ((len = img.read(buf, 0, buf.length)) != -1)
                    byteArrayOutputStream.write(buf, 0, len);

                final HttpHeaders headers = new HttpHeaders();
                headers.setExpires(180);

                final String t = rs.getString("mimetype").trim();
                if (t.equalsIgnoreCase(MediaType.IMAGE_GIF_VALUE))
                    headers.setContentType(MediaType.IMAGE_GIF);
                else if (t.equalsIgnoreCase(MediaType.IMAGE_JPEG_VALUE))
                    headers.setContentType(MediaType.IMAGE_JPEG);
                else if (t.equalsIgnoreCase(MediaType.IMAGE_PNG_VALUE))
                    headers.setContentType(MediaType.IMAGE_PNG);

                rs.close();
                stmt.close();
                conn.close();

                return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
            }

            rs.close();
            stmt.close();
            conn.close();

            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.warn("Could not load image: " + e.toString());
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (final SQLException sqlEx) {
                // ignore -- as we can't do anything about it here
                log.error(sqlEx.getMessage(), sqlEx);
            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity upload(@RequestPart("file") MultipartFile file, HttpSession session) throws IOException {
        assert jdbcTemplate != null;
        final int rowsAffected;

        final Integer userIDasi = (Integer) session.getAttribute("auth.uid");
        int userID = 0;
        if (userIDasi != null) {
            userID = userIDasi;
        }

          /* Make sure we are logged in */
        if (userID < 1) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final String contentType = file.getContentType();
        final long sizeInBytes = file.getSize();

        // must be large enough
        if (file.isEmpty() || sizeInBytes < 500) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        BufferedImage avatar = imageService.resizeAvatar(file.getBytes());

        Connection conn = null;
        PreparedStatement stmt = null; // create statement
        PreparedStatement stmtOn = null; // turn on avatar preference.
        PreparedStatement stmtRemove = null; // delete old ones

        try {
            // TODO: make this spring friendly
            conn = jdbcTemplate.getDataSource().getConnection();

            stmtRemove = conn.prepareStatement("DELETE FROM user_pic WHERE id=? LIMIT 1");
            stmtRemove.setInt(1, userID);
            stmtRemove.execute();

            // do the create of the image
            stmt = conn.prepareStatement("INSERT INTO user_pic (id,date_modified,mimetype,image) VALUES(?,now(),?,?)");
            stmt.setInt(1, userID);
            stmt.setString(2, contentType);
            stmt.setBytes(3, imageService.convertBufferedImageToJpeg(avatar));
            stmt.execute();
            rowsAffected = stmt.getUpdateCount();
            stmt.close();

            // turn on avatars.
            stmtOn = conn.prepareStatement("UPDATE user_pref SET show_avatar=? WHERE id=? LIMIT 1");
            stmtOn.setString(1, "Y");
            stmtOn.setInt(2, userID);
            stmtOn.execute();

            if (stmtOn.getUpdateCount() != 1)
                log.debug("error turning on avatar.");
            stmtOn.close();

            conn.close();
            
            log.info("RowsAffected: " + rowsAffected);
            if (rowsAffected == 1)
                return new ResponseEntity(HttpStatus.CREATED);
        } catch (final Exception e) {
            log.error("Error on database connection inserting avatar.", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
             /*
              * Close any JDBC instances here that weren't
              * explicitly closed during normal code path, so
              * that we don't 'leak' resources...
              */
            try {
                if (stmt != null)
                    stmt.close();
            } catch (final SQLException sqlEx) {
                // ignore -- as we can't do anything about it here
                log.error(sqlEx.getMessage(), sqlEx);
            }

            try {
                if (stmtOn != null)
                  stmtOn.close();
            } catch (final SQLException sqlEx) {
                // ignore -- as we can't do anything about it here
                log.debug(sqlEx.getMessage());
            }

            try {
                if (stmtRemove != null)
                   stmtRemove.close();
            } catch (final SQLException sqlEx) {
                // ignore -- as we can't do anything about it here
                log.debug(sqlEx.getMessage());
            }

            try {
                if (conn != null)
                    conn.close();
            } catch (final SQLException sqlEx) {
                // ignore -- as we can't do anything about it here
                log.error(sqlEx.getMessage(), sqlEx);
            }
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") final int id, final HttpSession session) {

        // Retreive user id
        final Integer userIDasi = (Integer) session.getAttribute("auth.uid");
        // convert Integer to int type
        int userID = 0;
        if (userIDasi != null) {
            userID = userIDasi;
        }

        /* Make sure we are logged in */
        if (userID < 1) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        try {
            jdbcTemplate.execute("DELETE FROM user_pic WHERE id='" + id + "';");
            jdbcTemplate.execute("UPDATE user_pref SET show_avatar='N' WHERE id='" + id + "' LIMIT 1");
        } catch (final DataAccessException dae) {
            log.error(dae.getMessage(), dae);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


}