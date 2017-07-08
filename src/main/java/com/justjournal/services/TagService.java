package com.justjournal.services;

import com.justjournal.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class TagService {
    @Autowired
    private EntryService entryService;


    public Collection<Tag> getTags(final String username) {
        Collection<Tag> tags = null;
        try {
            tags = entryService.getEntryTags(username);
        } catch (final ServiceException se) {
            log.error(se.getMessage(), se);
            tags = Collections.emptyList();
        }

        long largest = 0;
        long smallest = 10;

        for (final Tag tag : tags) {
            if (tag.getCount() > largest)
                largest = tag.getCount();

            if (tag.getCount() < smallest)
                smallest = tag.getCount();
        }

        final long cutSmall = largest / 3;
        final long cutLarge = cutSmall * 2;

        for (final Tag tag : tags) {
            if (tag.getCount() > cutLarge)
                tag.setType("TagCloudLarge");
            else if (tag.getCount() < cutSmall)
                tag.setType("TagCloudSmall");
            else
                tag.setType("TagCloudMedium");
        }

        return tags;
    }
}
