package com.justjournal.db;

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

public class LocationDaoTest {
    @BeforeClass
       public static void setUpClass() throws Exception {
           try {
               // Create initial context
               System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                   "org.apache.naming.java.javaURLContextFactory");
               System.setProperty(Context.URL_PKG_PREFIXES,
                   "org.apache.naming");
               InitialContext ic = new InitialContext();

               ic.createSubcontext("java:");
               ic.createSubcontext("java:comp");
               ic.createSubcontext("java:comp/env");
               ic.createSubcontext("java:comp/env/jdbc");

               // Construct DataSource
               MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
               ds.setURL("jdbc:mysql://ds9.midnightbsd.org:3306/jj");
               ds.setUser("jj");
               ds.setPassword("");

               ic.bind("java:comp/env/jdbc/jjDB", ds);

               ServerRuntime cayenneRuntime = new ServerRuntime("cayenne-JustJournalDomain.xml");
               DataContext.bindThreadObjectContext(cayenneRuntime.getContext());
           } catch (NamingException ex) {
                System.err.println(ex.getMessage());
           }

       }

    @Test
    public void testView() throws Exception {
        assertEquals(5, LocationDao.view().size());
    }
}