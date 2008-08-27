package com.justjournal.db;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

import java.util.Calendar;
import java.util.Date;

/**
 * User: laffer1
 * Date: Aug 15, 2008
 * Time: 10:30:24 AM
 */
public class Caching {

    // create a static client as most installs only need
// a single instance
protected static MemCachedClient mcc = new MemCachedClient();

    // TODO: Don't hard code the host
    
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

    public Object get(String key) {
        return mcc.get(key);
    }

    public void set(String key, Object data) {
        mcc.set(key, data);
    }

    public void set(String key, Object data, Date expires) {
        mcc.set(key, data, expires);
    }

    public void set60(String key, Object data) {
        set(key, data, 60);
    }

    
    public void set(String key, Object data, int seconds) {
        Calendar cal = Calendar.getInstance();
        set(key, data, addTime(cal.getTime(), seconds));  // TODO: Verify accuracy
    }

    public void delete(String hash) {
        mcc.delete(hash);
    }

    public static Date addTime( Date startDate, int seconds )
	{
		Calendar startCal = Calendar.getInstance( );
		Date firstDate = startDate;
		startCal.setTime( firstDate );
		startCal.add( Calendar.SECOND, seconds );

		return startCal.getTime( );
	}
}
