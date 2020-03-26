package com.justjournal.services;

import com.justjournal.Application;
import com.justjournal.model.api.PublicMember;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Lucas Holt
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void testPublicMembers() {
        final List<PublicMember> members = userService.getPublicMembers();
        assertNotNull(members);
        assertFalse(members.isEmpty());

        for (final PublicMember member : members) {
            assertNotNull(member.getName());
            assertNotNull(member.getUsername());
            assertTrue(member.getSince() > 2002);
        }
    }
}
