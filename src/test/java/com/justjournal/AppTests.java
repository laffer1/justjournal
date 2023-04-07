/*
 * Copyright (c) 2003-2021 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.justjournal;

import com.justjournal.repository.TrackbackRepository;
import com.justjournal.repository.cache.TrackBackIpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("ClassWithTooManyMethods")
@ActiveProfiles("it")
@SpringBootTest
@AutoConfigureMockMvc
class AppTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired private TrackbackRepository trackbackRepository;

  @Autowired TrackBackIpRepository trackBackIpRepository;

  @BeforeEach
  void setup() {
    // test cleanup
    trackbackRepository.deleteAll(
        trackbackRepository.findByEntryIdAndUrlOrderByDate(33661, "http://justjournal.com/bar"));
    trackbackRepository.deleteAll(
        trackbackRepository.findByEntryIdAndUrlOrderByDate(
            33661, "http://justjournal.com/users/jjsite"));
    trackBackIpRepository.deleteIpAddress("127.0.0.1").block();
  }

  @Test
  void simple() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
  }

  @Test
  void privacy() throws Exception {
    mockMvc.perform(get("/#!/privacy")).andExpect(status().isOk()).andExpect(view().name("index"));
  }

  @Test
  void search() throws Exception {
    mockMvc.perform(get("/#!/search")).andExpect(status().isOk()).andExpect(view().name("index"));
  }

  @Test
  void sitemap() throws Exception {
    mockMvc.perform(get("/#!/sitemap")).andExpect(status().isOk()).andExpect(view().name("index"));
  }

  @Test
  void searchEscape() throws Exception {
    mockMvc
        .perform(get("/?=_escaped_fragment_=sitemap"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
  }

  @Test
  void users() throws Exception {
    mockMvc
        .perform(get("/users/testuser"))
        .andExpect(status().isOk())
        .andExpect(view().name("users"));
  }

  @Test
  void usersSingleEntry() throws Exception {
    mockMvc
        .perform(get("/users/testuser/entry/33661"))
        .andExpect(status().isOk())
        .andExpect(view().name("users"));
  }

  @Test
  void usersFriends() throws Exception {
    mockMvc
        .perform(get("/users/testuser/friends"))
        .andExpect(status().isOk())
        .andExpect(view().name("users"));
  }

  @Test
  void usersPictures() throws Exception {
    mockMvc
        .perform(get("/users/testuser/pictures"))
        .andExpect(status().isOk())
        .andExpect(view().name("users"));
  }

  @Test
  void usersCalendar() throws Exception {
    mockMvc
        .perform(get("/users/testuser/calendar"))
        .andExpect(status().isOk())
        .andExpect(view().name("users"));
  }

  @Test
  void usersCalendarYear() throws Exception {
    mockMvc
        .perform(get("/users/testuser/2014"))
        .andExpect(status().isOk())
        .andExpect(view().name("users"));
  }

  @Test
  void usersCalendarMonth() throws Exception {
    mockMvc
        .perform(get("/users/testuser/2014/03"))
        .andExpect(status().isOk())
        .andExpect(view().name("users"));
  }

  @Test
  void recentBlogs() throws Exception {
    mockMvc
        .perform(get("/RecentBlogs"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/rss+xml"));
  }

  @Test
  void apiMembers() throws Exception {
    mockMvc.perform(get("/api/members")).andExpect(status().isOk());
  }

  @Test
  void apiLocation() throws Exception {
    mockMvc
        .perform(get("/api/location"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiLocationWithId() throws Exception {
    mockMvc
        .perform(get("/api/location/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiMood() throws Exception {
    mockMvc
        .perform(get("/api/mood"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiMoodWithId() throws Exception {
    mockMvc
        .perform(get("/api/mood/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiSecurity() throws Exception {
    mockMvc
        .perform(get("/api/security"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiSecurityWithId() throws Exception {
    mockMvc
        .perform(get("/api/security/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiStatistics() throws Exception {
    mockMvc
        .perform(get("/api/statistics"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiStatisticsUser() throws Exception {
    mockMvc
        .perform(get("/api/statistics/testuser"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiStatisticsBadUser() throws Exception {
    mockMvc.perform(get("/api/statistics/root")).andExpect(status().isNotFound());
  }

  @Test
  void apiStatisticsInvalid() throws Exception {
    mockMvc.perform(get("/api/statistics/r")).andExpect(status().isBadRequest());
  }

  @Test
  void apiTags() throws Exception {
    mockMvc
        .perform(get("/api/tags"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiTagCloud() throws Exception {
    mockMvc.perform(get("/api/tagcloud/testuser")).andExpect(status().isOk());
  }

  @Test
  void apiEntryPostNotLoggedIn() throws Exception {
    mockMvc
        .perform(
            post("/api/entry", "{\"subject\":\"testing\", \"body\":\"test\"}")
                .content("{\"subject\":\"testing\", \"body\":\"test\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
        .andExpect(content().string("{\"error\":\"The login timed out or is invalid.\"}"))
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiEntry() throws Exception {
    mockMvc
        .perform(
            get("/api/entry/testuser/eid/33661")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiEntryRecentWithUser() throws Exception {
    mockMvc
        .perform(
            get("/api/entry/testuser/recent")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiEntryWithUser() throws Exception {
    mockMvc
        .perform(
            get("/api/entry/testuser")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiEntryWithUserParam() throws Exception {
    mockMvc
        .perform(
            get("/api/entry?username=testuser")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiComment() throws Exception {
    mockMvc
        .perform(
            get("/api/comment/1")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void sitemapXml() throws Exception {
    mockMvc
        .perform(get("/sitemap.xml").accept(MediaType.parseMediaType("text/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("text/xml;charset=UTF-8"));
  }

  @Test
  void apiCommentWithEntry() throws Exception {
    mockMvc
        .perform(get("/api/comment?entryId=33661").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiCommentNotFound() throws Exception {
    mockMvc.perform(get("/api/comment/99999")).andExpect(status().isNotFound());
  }

  @Test
  void apiLoginCheck() throws Exception {
    mockMvc
        .perform(
            get("/api/login").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void apiLoginBad() throws Exception {
    mockMvc
        .perform(
            post("/api/login", "{\"username\":\"testuser\", \"password\":\"blah\"}")
                .accept(MediaType.parseMediaType("application/json"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\", \"password\":\"blah\"}"))
        .andExpect(status().is(401))
        .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"));
  }

  @Test
  void trackbackPingInvalid() throws Exception {
    mockMvc.perform(post("/trackback/?entryID=")).andExpect(status().is4xxClientError());
  }

  @Test
  void trackbackPingInvalid2() throws Exception {
    mockMvc.perform(post("/trackback/?url=")).andExpect(status().is4xxClientError());
  }

  @Test
  void trackbackPing() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.put("entryID", Collections.singletonList("33661"));
    requestParams.put("title", Collections.singletonList("my title"));
    requestParams.put("url", Collections.singletonList("http://justjournal.com/users/jjsite"));
    requestParams.put("excerpt", Collections.singletonList("a cool blog"));

    mockMvc
        .perform(
            post("/trackback/")
                .queryParams(requestParams)
                .accept(MediaType.TEXT_XML)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("text/xml;charset=UTF-8"));
  }

  @Test
  void trackbackPingWithIllegalUrl() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.put("entryID", Collections.singletonList("33661"));
    requestParams.put("title", Collections.singletonList("my title"));
    requestParams.put(
        "url",
        Collections.singletonList(
            "http://example.notarealdomainnameatallandshouldntresolve.com/bar"));
    requestParams.put("excerpt", Collections.singletonList("a cool blog"));

    mockMvc
        .perform(
            post("/trackback/")
                .queryParams(requestParams)
                .accept(MediaType.TEXT_XML)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentTypeCompatibleWith("text/xml;charset=UTF-8"));
  }

  @Test
  void trackbackPingPostIt() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.put("entryID", Collections.singletonList("33661"));
    requestParams.put("name", Collections.singletonList("my title"));
    requestParams.put("email", Collections.singletonList("test@example.com"));
    requestParams.put("url", Collections.singletonList("http://justjournal.com/bar"));
    requestParams.put("excerpt", Collections.singletonList("a cool blog"));
    requestParams.put("blog_name", Collections.singletonList("blog_name"));

    mockMvc
        .perform(
            post("/trackback/")
                .queryParams(requestParams)
                .accept(MediaType.TEXT_XML)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("text/xml;charset=UTF-8"));
  }

  @Test
  void trackbackPingPostItWithIllegalUrl() throws Exception {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.put("entryID", Collections.singletonList("33661"));
    requestParams.put("name", Collections.singletonList("my title"));
    requestParams.put("email", Collections.singletonList("test@example.com"));
    requestParams.put(
        "url",
        Collections.singletonList(
            "http://example.notarealdomainnameatallandshouldntresolve.com/bar"));
    requestParams.put("excerpt", Collections.singletonList("a cool blog"));
    requestParams.put("blog_name", Collections.singletonList("blog_name"));

    mockMvc
        .perform(
            post("/trackback/")
                .queryParams(requestParams)
                .accept(MediaType.TEXT_XML)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentTypeCompatibleWith("text/xml;charset=UTF-8"));
  }
}
