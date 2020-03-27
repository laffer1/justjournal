package com.justjournal.services;

import com.justjournal.model.User;
import com.justjournal.model.UserLink;
import com.justjournal.model.api.UserLinkTo;
import com.justjournal.repository.UserLinkRepository;
import com.justjournal.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Lucas Holt
 */
@RunWith(MockitoJUnitRunner.class)
public class UserLinkServiceTests {

    @Mock
    private UserLinkRepository userLinkRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLinkService userLinkService;

    @Test
    public void testCreate() {
        UserLinkTo link = UserLinkTo.builder()
                .id(1)
                .title("foo")
                .uri("http://mysite.com")
                .userId(1)
                .build();

        UserLink item = UserLink.builder()
                .id(1)
                .title("foo")
                .uri("http://mysite.com")
                .user(new User())
                .build();

        when(userLinkRepository.save(any())).thenReturn(item);
        UserLinkTo result = userLinkService.create(link);
        assertNotNull(result);
        verify(userLinkRepository, times(1)).save(any(UserLink.class));
    }

    @Test
    public void testGet() {
        UserLink item = UserLink.builder()
                .id(1)
                .title("foo")
                .uri("http://mysite.com")
                .user(new User())
                .build();
        when(userLinkRepository.findById(anyInt())).thenReturn(Optional.of(item));

        Optional<UserLinkTo> result = userLinkService.get(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("foo", result.get().getTitle());
        assertEquals("http://mysite.com", result.get().getUri());
        verify(userLinkRepository, times(1)).findById(1);
    }

    @Test
    public void testGetByUser() {
        UserLink item = UserLink.builder()
                .id(1)
                .title("foo")
                .uri("http://mysite.com")
                .user(new User())
                .build();
        when(userLinkRepository.findByUsernameOrderByTitleTitleAsc(anyString())).thenReturn(Collections.singletonList(item));

        List<UserLinkTo> result = userLinkService.getByUser("user");
        assertNotNull(result);
        assertEquals(1, result.get(0).getId());
        assertEquals("foo", result.get(0).getTitle());
        assertEquals("http://mysite.com", result.get(0).getUri());
        verify(userLinkRepository, times(1)).findByUsernameOrderByTitleTitleAsc(anyString());
    }

    @Test
    public void testDelete() {
        userLinkService.delete(1);
        verify(userLinkRepository, times(1)).deleteById(anyInt());
    }
}
