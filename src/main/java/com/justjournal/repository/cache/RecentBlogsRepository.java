package com.justjournal.repository.cache;

import com.justjournal.core.CacheKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author Lucas Holt
 */
@Component
public class RecentBlogsRepository {
    private static final int BLOG_CACHE_MINUTES = 5;

    private ReactiveRedisTemplate<String, String> reactiveRedisTemplateString;
    private ReactiveValueOperations<String, String> valOperations;

    @Autowired
    public RecentBlogsRepository(ReactiveRedisTemplate<String, String> reactiveRedisTemplateString) {
        this.reactiveRedisTemplateString = reactiveRedisTemplateString;
        this.valOperations = reactiveRedisTemplateString.opsForValue();
    }

    public Mono<String> getBlogs() {
        return valOperations.get(CacheKeys.RECENT_BLOGS_KEY);
    }

    public Mono<Boolean> setBlogs(String blogs) {
        return valOperations.set(CacheKeys.RECENT_BLOGS_KEY, blogs, Duration.ofMinutes(BLOG_CACHE_MINUTES));
    }

    public Mono<Boolean> delete() {
        return valOperations.delete(CacheKeys.RECENT_BLOGS_KEY);
    }
}
