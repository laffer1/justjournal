/*
Copyright (c) 2003-2021, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/
package com.justjournal.repository.cache;


import com.justjournal.core.CacheKeys;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/** @author Lucas Holt */
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
    return valOperations.set(
        CacheKeys.RECENT_BLOGS_KEY, blogs, Duration.ofMinutes(BLOG_CACHE_MINUTES));
  }

  public Mono<Boolean> delete() {
    return valOperations.delete(CacheKeys.RECENT_BLOGS_KEY);
  }
}
