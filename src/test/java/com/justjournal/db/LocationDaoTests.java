package com.justjournal.db;

import com.justjournal.Util;
import com.justjournal.db.model.Location;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/resources/mvc-dispatcher-servlet.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
       })
public class LocationDaoTests {

    @Autowired
    private LocationDao locationDao;

    @BeforeClass
    public static void setup() throws Exception {
        Util.setupDb();
    }

    @Test
    public void list() throws Exception {
        Iterable<Location> list = locationDao.findAll();
        assertNotNull(list);
        assertEquals(5, locationDao.count());
    }

    @Test
    public void get() {
        Location locationTo = locationDao.findOne(1);
        assertNotNull(locationTo);
        assertEquals(1, locationTo.getId());
        assertNotNull(locationTo.getTitle());
    }
}