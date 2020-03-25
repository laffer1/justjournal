package com.justjournal.services;

import com.justjournal.exception.ServiceException;
import com.justjournal.model.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class TagService {

    @Autowired
    private EntryService entryService;

    public ParallelFlux<Tag> getTags(final String username) throws ServiceException {
        Flux<Tag> tags = entryService.getEntryTags(username);

        final TagWeight tagWeight = new TagWeight();
        
        return tags
                .map(tag -> {
                    if (tag.getCount() > tagWeight.largest)
                        tagWeight.largest = tag.getCount();

                    if (tag.getCount() < tagWeight.smallest)
                        tagWeight.smallest = tag.getCount();

                    return tag;
                })
                .parallel()
                .runOn(Schedulers.parallel())
                .map(tagWeight::calculateType);
    }

    @Getter
    @Setter
    class TagWeight {
        long largest = 0;
        long smallest = 10;

        long calculateSmallestCutoff() {
            return largest / 3;
        }

        long calculateLargestCutoff() {
            return calculateSmallestCutoff() * 2;
        }

        Tag calculateType(final Tag tag) {
            if (tag.getCount() > calculateLargestCutoff())
                tag.setType("TagCloudLarge");
            else if (tag.getCount() < calculateSmallestCutoff())
                tag.setType("TagCloudSmall");
            else
                tag.setType("TagCloudMedium");

            return tag;
        }
    }
}
