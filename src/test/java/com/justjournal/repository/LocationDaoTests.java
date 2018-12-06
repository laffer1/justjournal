package com.justjournal.repository;

import com.justjournal.Application;
import com.justjournal.model.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
/*@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})*/
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
        Location locationTo = locationDao.findById(1).orElse(null);
        assertNotNull(locationTo);
        assertEquals(1, locationTo.getId());
        assertNotNull(locationTo.getTitle());
    }
}