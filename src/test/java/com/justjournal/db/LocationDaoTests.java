package com.justjournal.db;

import com.justjournal.Util;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.junit.Assert.assertEquals;

public class LocationDaoTests {
    @BeforeClass
    public static void setup() throws Exception {
        Util.setupDb();
    }

    @Test
    public void testView() throws Exception {
        assertEquals(5, LocationDao.view().size());
    }
}