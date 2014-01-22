/*
 * Copyright (c) 2009, 2011 Lucas Holt
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

package com.justjournal.core;

import com.justjournal.utility.StringUtil;
import com.justjournal.db.RssCacheDao;
import com.justjournal.db.RssCacheTo;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;

/**
 * Update the RSS cache
 *
 * @author Lucas Holt
 */
final public class RssCacheContextListener extends Thread {

    public void run() {
        System.out.println("RssCache: Init");

        final String DbEnv = "java:comp/env/jdbc/jjDB";
        final String sqlSelect = "SELECT uri FROM rss_cache WHERE lastupdated <= DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY);";

        try {
            System.out.println("RssCache: Lookup context");
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(DbEnv);

            Connection conn = ds.getConnection();
            System.out.println("MailSender: DB Connection up");

            while (true) {
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sqlSelect);
                    System.out.println("RssCache: Recordset loaded.");

                    while (rs.next()) {
                        getRssDocument(rs.getString("uri"));
                        yield();  // be nice to others... we are in a servlet container...
                    }

                    rs.close();
                    stmt.close();
                    sleep(1000 * 60 * 60); // 60 minutes?
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    System.out.println("MailSender: Exception - " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("RssCache: Quit");
    }

    protected void getRssDocument(final String uri)
            throws Exception {

        URL u;
        RssCacheTo rss;
        InputStreamReader ir;
        StringBuilder sbx = new StringBuilder();
        BufferedReader buff;

        rss = RssCacheDao.get(uri);

        if (rss != null && rss.getUri() != null && rss.getUri().length() > 10) {

            u = new URL(uri);
            ir = new InputStreamReader(u.openStream(), "UTF-8");
            buff = new BufferedReader(ir);
            String input;
            while ((input = buff.readLine()) != null) {
                sbx.append(StringUtil.replace(input, '\'', "\\\'"));
            }
            buff.close();

            rss.setContent(sbx.toString().trim());
            // sun can't make their own rss feeds complaint
            if (rss.getContent().startsWith("<rss"))
                rss.setContent("<?xml version=\"1.0\"?>\n" + rss.getContent());

            if (rss.getContent().startsWith("<html") || rss.getContent().startsWith("<!DOCTYPE HTML"))
                rss.setContent(""); // it's an html page.. bad
            RssCacheDao.update(rss);

        }
    }

}
