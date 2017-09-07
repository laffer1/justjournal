/*
 * Copyright (c) 2006 - 2011 Lucas Holt
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

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Display individual images in the user's photo album.
 *
 * @author Lucas Holt
 * @version $Id: AlbumImage.java,v 1.9 2012/07/04 18:48:28 laffer1 Exp $
 */
@Slf4j
@RequestMapping("/AlbumImage")
@Controller
public class AlbumImageController {

    private final JdbcTemplate jdbcTemplate;
    private final ImageService imageService;

    @Autowired
    public AlbumImageController(final JdbcTemplate jdbcTemplate, ImageService imageService) {
        this.jdbcTemplate = jdbcTemplate;
        this.imageService = imageService;
    }

    @RequestMapping(value = "/{id}/thumbnail", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getThumbnail(@PathVariable("id") final int id) throws IOException {

        // TODO: refactor into service
        ResponseEntity<byte[]> out = get(id);
        BufferedImage image = imageService.resizeAvatar(out.getBody());

        final HttpHeaders headers = new HttpHeaders();
        headers.setExpires(180);
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<byte[]>(imageService.convertBufferedImageToJpeg(image), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getByPath(@PathVariable("id") final int id) throws IOException {
        return get(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<byte[]> get(@RequestParam("id") final int id) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final DataSource ds;
        Connection conn = null;
        PreparedStatement stmt = null;

        if (id < 1) {
            return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        }

        try {
            ds = jdbcTemplate.getDataSource();
            conn = ds.getConnection();
            stmt = conn.prepareStatement("CALL getalbumimage(?)");
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

                return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
            }

            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        } catch (final Exception e) {
            log.warn("Could not load image: ", e);
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (final Exception e) {
                log.error("Error closing statement or connection", e);
            }
        }
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity upload(@RequestPart("file") MultipartFile file, @RequestParam(value = "title", defaultValue = "untitled") String title, HttpSession session) throws IOException {
        assert jdbcTemplate != null;
        final int rowsAffected;

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

        final String contentType = file.getContentType();
        final long sizeInBytes = file.getSize();

        // must be large enough
        if (file.isEmpty() || sizeInBytes < 500) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        byte[] data = file.getBytes();

        Connection conn = null;
        PreparedStatement stmt = null; // create statement

        try {
            // TODO: make this spring friendly
            conn = jdbcTemplate.getDataSource().getConnection();

            // do the create of the image
            stmt = conn.prepareStatement("INSERT INTO user_images (owner,title,modified,mimetype,image) VALUES(?,?,now(),?,?)");
            stmt.setInt(1, userID);
            stmt.setString(2, title);
            stmt.setString(3, contentType);
            stmt.setBytes(4, data);
            stmt.execute();
            rowsAffected = stmt.getUpdateCount();
            stmt.close();

            conn.close();

            log.info("RowsAffected: " + rowsAffected);
            if (rowsAffected == 1)
                return new ResponseEntity(HttpStatus.CREATED);
        } catch (final Exception e) {
            log.error("Error on database connection inserting image.", e);
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
            jdbcTemplate.execute("DELETE FROM user_images WHERE id='" + id + "' AND owner='" + userID + "';");
        } catch (final DataAccessException dae) {
            log.error(dae.getMessage(), dae);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}

