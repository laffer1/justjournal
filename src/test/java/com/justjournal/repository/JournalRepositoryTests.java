package com.justjournal.repository;

import com.justjournal.ApplicationTest;
import com.justjournal.model.EntryTag;
import com.justjournal.model.Journal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;

/**
 * @author Lucas Holt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
public class JournalRepositoryTests {
    @Autowired
    private JournalRepository journalRepository;

    @Test
       public void list() throws Exception {
           final Iterable<Journal> list = journalRepository.findAll();
           assertNotNull(list);
       }

       @Test
       public void findByUsername() {
           final Iterable<Journal> list = journalRepository.findByUsername("testuser");
           assertNotNull(list);
       }
}
