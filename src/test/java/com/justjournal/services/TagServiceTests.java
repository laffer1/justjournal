package com.justjournal.services;

import com.justjournal.Application;
import com.justjournal.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.thymeleaf.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Lucas Holt
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class TagServiceTests {

    @Autowired
    private TagService tagService;

    @Test
    public void testGetTags() {
         List<Tag> tags = tagService.getTags().toStream().collect(Collectors.toList());

         assertFalse(tags.isEmpty());

         for (Tag tag : tags) {
             assertTrue(tag.getId() > 0);
             assertFalse(StringUtils.isEmpty(tag.getName()));
             assertFalse(StringUtils.isEmpty(tag.getType()));
             assertTrue(tag.getCount() > 0);
         }
    }
}
