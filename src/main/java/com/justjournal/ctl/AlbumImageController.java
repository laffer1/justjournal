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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Diplay individual images in the user's photo album.
 *
 * @author Lucas Holt
 * @version $Id: AlbumImage.java,v 1.9 2012/07/04 18:48:28 laffer1 Exp $
 */
@RequestMapping("/AlbumImage")
@Controller
public class AlbumImageController {
    private static final Logger log = Logger.getLogger(AlbumImageController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/{id}")
    public ResponseEntity<byte[]> getByPath(@PathVariable("id") final int id) throws IOException {
        return get(id);
    }

    @RequestMapping("")
    public ResponseEntity<byte[]> get(@RequestParam("id") final int id) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        DataSource ds;
        Connection conn;
        PreparedStatement stmt;

        if (id < 1) {
            return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        }

        try {
            ds = jdbcTemplate.getDataSource();
            conn = ds.getConnection();
            stmt = conn.prepareStatement("call getalbumimage(?)");
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

            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.warn("Could not load image: " + e.toString());
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
