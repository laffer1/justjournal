/*
Copyright (c) 2005, Lucas Holt
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

package com.justjournal.db;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

import javax.sql.rowset.CachedRowSet;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 3, 2004
 * Time: 11:21:11 PM
 */
public final class BioDao {


// create a static client as most installs only need
// a single instance
protected static MemCachedClient mcc = new MemCachedClient();

// set up connection pool once at class load
static {

    // server list and weights
    String[] servers =
        {
          "ds9.midnightbsd.org:1624"
        };

    Integer[] weights = { 3 };

    // grab an instance of our connection pool
    SockIOPool pool = SockIOPool.getInstance();

    // set the servers and the weights
    pool.setServers( servers );
    pool.setWeights( weights );

    // set some basic pool settings
    // 5 initial, 5 min, and 250 max conns
    // and set the max idle time for a conn
    // to 6 hours
    pool.setInitConn( 5 );
    pool.setMinConn( 5 );
    pool.setMaxConn( 250 );
    pool.setMaxIdle( 1000 * 60 * 60 * 6 );

    // set the sleep for the maint thread
    // it will wake up every x seconds and
    // maintain the pool size
    pool.setMaintSleep( 30 );

    // set some TCP settings
    // disable nagle
    // set the read timeout to 3 secs
    // and don't set a connect timeout
    pool.setNagle( false );
    pool.setSocketTO( 3000 );
    pool.setSocketConnectTO( 0 );

    // initialize the connection pool
    pool.initialize();


    // lets set some compression on for the client
    // compress anything larger than 64k
    mcc.setCompressEnable( true );
    mcc.setCompressThreshold( 64 * 1024 );
}


    public static final boolean add(BioTo bio) {
        boolean noError = true;
        int records = 0;

        final String sqlStmt =
                "INSERT INTO user_bio (id,content) values(NULL,'"
                        + bio.getBio() + "');";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    public static final boolean update(BioTo bio) {

        boolean noError = true;

        final String sqlStmt =
                "Update user_bio SET content='" + bio.getBio() + "' WHERE id='" + bio.getUserId() + "' LIMIT 1;";

        try {
            SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
        }

        mcc.delete("bio: " + bio.getUserId());

        return noError;
    }

    public static final boolean delete(int userId) {
        boolean noError = true;

        final String sqlStmt = "DELETE FROM user_bio WHERE id='" + userId + "' LIMIT 1;";

        if (userId > 0) {
            try {
                SQLHelper.executeNonQuery(sqlStmt);
            } catch (Exception e) {
                noError = false;
            }
        } else {
            noError = false;
        }

        mcc.delete("bio: " + userId);

        return noError;
    }


    public static final BioTo view(int userId) {
        BioTo bio;
        CachedRowSet rs = null;
        String sqlStmt = "Select content from user_bio WHERE id='" + userId + "' Limit 1;";

        if ((bio = (BioTo) mcc.get("bio: " + userId)) != null)
                return bio;

        try {
            bio = new BioTo();
            rs = SQLHelper.executeResultSet(sqlStmt);

            if (rs.next()) {
                bio.setUserId(userId);
                bio.setBio(rs.getString(1));
            }

            rs.close();

        } catch (Exception e1) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }

        mcc.set("bio: " + userId, bio);
        return bio;
    }
}
