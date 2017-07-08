package com.justjournal.services;

import com.justjournal.model.Tag;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class TagService {
    @Autowired
    private EntryService entryService;


    public Observable<Tag> getTags(final String username) throws ServiceException {
        Observable<Tag> tags = entryService.getEntryTags(username);

        final TagWeight tagWeight = new TagWeight();

        return tags
                .observeOn(Schedulers.single())
                .map(new Function<Tag, Tag>() {

                    @Override
                    public Tag apply(final Tag tag) throws Exception {
                        if (tag.getCount() > tagWeight.largest)
                            tagWeight.largest = tag.getCount();

                        if (tag.getCount() < tagWeight.smallest)
                            tagWeight.smallest = tag.getCount();

                        return tag;
                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Function<Tag, Tag>() {
                    @Override
                    public Tag apply(final Tag tag) throws Exception {
                        return tagWeight.calculateType(tag);
                    }
                });
        
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

        Tag calculateType(Tag tag) {
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
