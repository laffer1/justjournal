package com.justjournal.db;

import com.justjournal.Util;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LocationDaoTests {
    @BeforeClass
    public static void setup() throws Exception {
        Util.setupDb();
    }

    @Test
    public void list() throws Exception {
        Collection<LocationTo> list = LocationDao.list();
        assertNotNull(list);
        assertEquals(5, list.size());
    }

    @Test
    public void get() {
        LocationTo locationTo = LocationDao.get(1);
        assertNotNull(locationTo);
        assertEquals(1, locationTo.getId());
        assertNotNull(locationTo.getName());
    }
}