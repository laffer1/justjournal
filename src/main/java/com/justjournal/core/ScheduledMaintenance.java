package com.justjournal.core;

import com.justjournal.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Performs cleanup operations.
 * @author Lucas Holt
 */
@Component
public class ScheduledMaintenance {

    @Autowired
    private TagService tagService;

    /**
     * Remove old tags that are no longer connected to any entries.
     */
    @Scheduled(fixedDelay = 1000 * 60 * 30, initialDelay = 120000)
    public void tagCleanup() {
        tagService.getTags()
                .filter(t -> t.getCount() == 0)
                .subscribe(tag -> tagService.deleteTag(tag.getId()));
    }
}
