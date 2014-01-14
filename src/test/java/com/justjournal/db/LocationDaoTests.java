package com.justjournal.db;

import com.justjournal.Util;
import org.junit.BeforeClass;
import org.junit.Test;

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