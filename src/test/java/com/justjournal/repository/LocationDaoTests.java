package com.justjournal.repository;

import com.justjournal.Application;
import com.justjournal.model.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
public class LocationDaoTests {

    @Autowired
    private LocationRepository locationDao;

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