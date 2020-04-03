package com.justjournal.repository.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.justjournal.core.CacheKeys.TRACKBACK_IP_KEY;

/**
 * @author Lucas Holt
 */
@Component
public class TrackBackIpRepository {

    private static final int IP_CACHE_SECONDS = 5;

    ReactiveRedisTemplate<String, String> reactiveRedisTemplateString;

    private ReactiveValueOperations<String, String> valOperations;

    @Autowired
    public TrackBackIpRepository(ReactiveRedisTemplate<String, String> reactiveRedisTemplateString) {
        this.reactiveRedisTemplateString = reactiveRedisTemplateString;
        this.valOperations = reactiveRedisTemplateString.opsForValue();
    }

    public Mono<Boolean> saveIpAddreess(String ip) {
        return valOperations.set(TRACKBACK_IP_KEY + ip, ip, Duration.ofSeconds(IP_CACHE_SECONDS));
    }

    public Mono<String> getIpAddress(String ip) {
        return valOperations.get(TRACKBACK_IP_KEY + ip);
    }

    public Mono<Boolean> deleteIpAddress(String ip) {
          return valOperations.delete(TRACKBACK_IP_KEY + ip);
    }
}
