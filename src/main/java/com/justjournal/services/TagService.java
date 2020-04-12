package com.justjournal.services;

import com.justjournal.exception.ServiceException;
import com.justjournal.model.Tag;
import com.justjournal.repository.EntryTagsRepository;
import com.justjournal.repository.TagRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.justjournal.core.CacheKeys.TAG_KEY;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class TagService {

    private final EntryService entryService;

    private final TagRepository tagDao;

    private final EntryTagsRepository entryTagsRepository;

    private ReactiveRedisTemplate<String, Tag> reactiveRedisTemplateTag;

    @Autowired
    public TagService(final TagRepository tagDao, final EntryTagsRepository entryTagsRepository,
                      ReactiveRedisTemplate<String, Tag> reactiveRedisTemplateTag, final EntryService entryService) {
        this.tagDao = tagDao;
        this.entryTagsRepository = entryTagsRepository;
        this.reactiveRedisTemplateTag = reactiveRedisTemplateTag;
        this.entryService = entryService;
    }

    public Optional<Tag> getTag(@NonNull Integer id) {
        Optional<Tag> tag = reactiveRedisTemplateTag.opsForValue().get(TAG_KEY + id)
                .blockOptional();

        if (tag.isPresent())
            return tag;

        tag = tagDao.findById(id);

        if (tag.isPresent()) {
            reactiveRedisTemplateTag.opsForValue().set(TAG_KEY + id, tag.get(), Duration.ofMinutes(10)).subscribe();
            return tag;
        }

        return Optional.empty();
    }

    public Flux<Tag> getTags() {
        final Map<String, Tag> tags = new HashMap<>();

        final Iterable<Tag> tagList = tagDao.findAll();
        for (final Tag t : tagList) {
            if (!tags.containsKey(t.getName())) {
                final long count = entryTagsRepository.countByTag(t);
                t.setCount(count);
                tags.put(t.getName(), t);
            }
        }
        
        return compute(Flux.fromIterable(tags.values()))
                .sorted(Comparator.comparingLong(Tag::getCount));
    }

    public ParallelFlux<Tag> getTags(final String username) throws ServiceException {
        return compute(entryService.getEntryTags(username));
    }

    private ParallelFlux<Tag> compute(Flux<Tag> tags) {
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

    public void deleteTag(final int tagId) {
        tagDao.deleteById(tagId);
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
