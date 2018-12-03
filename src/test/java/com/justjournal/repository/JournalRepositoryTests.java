package com.justjournal.repository;

import com.justjournal.Application;
import com.justjournal.model.Journal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;

/**
 * @author Lucas Holt
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
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

    @Test
    public void findBySlug() {
        final Journal journal = journalRepository.findOneBySlug("testuser");
        assertNotNull(journal);
    }
}
