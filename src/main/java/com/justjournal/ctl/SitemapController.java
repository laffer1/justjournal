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
package com.justjournal.ctl;


import com.justjournal.core.Constants;
import com.justjournal.model.*;
import com.justjournal.repository.SettingsRepository;
import com.justjournal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dynamically generate sitemap from
 * <a href="http://stackoverflow.com/questions/12289232/serving-sitemap-xml-and-robots-txt-with-spring-mvc">...</a>
 *
 * @author Lucas Holt
 */
@RestController
public class SitemapController {

  @Autowired private SettingsRepository settingsRepository;

  @Autowired private UserRepository userRepository;

  @GetMapping(value = "/sitemap.xml", produces = "text/xml")
  @ResponseBody
  public UrlSet main() {

    final Settings baseUri = settingsRepository.findByName("baseuri");

    final UrlSet xmlUrlSet = new UrlSet();
    create(xmlUrlSet, baseUri.getValue(), Url.Priority.HIGH, Url.ChangeFreqency.MONTHLY);

    create(
        xmlUrlSet, baseUri.getValue() + "RecentBlogs", Url.Priority.LOW, Url.ChangeFreqency.DAILY);
    create(
        xmlUrlSet, baseUri.getValue() + "#!/sitemap", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
    create(
        xmlUrlSet, baseUri.getValue() + "#!/sitemap", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
    create(
        xmlUrlSet, baseUri.getValue() + "#!/search", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
    create(
        xmlUrlSet, baseUri.getValue() + "#!/privacy", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
    create(
        xmlUrlSet, baseUri.getValue() + "#!/members", Url.Priority.LOW, Url.ChangeFreqency.DAILY);
    create(
        xmlUrlSet, baseUri.getValue() + "#!/support", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
    create(
        xmlUrlSet,
        baseUri.getValue() + "#!/support/bugs",
        Url.Priority.LOW,
        Url.ChangeFreqency.YEARLY);
    create(
        xmlUrlSet,
        baseUri.getValue() + "#!/moodlist",
        Url.Priority.LOW,
        Url.ChangeFreqency.YEARLY);

    for (final User user : userRepository.findAll()) {
      for (final Journal journal : user.getJournals()) {
        if (journal.isAllowSpider() && !journal.isOwnerViewOnly()) {
          final String users = baseUri.getValue() + Constants.PATH_USERS + user.getUsername();

          create(xmlUrlSet, users, Url.Priority.HIGH, Url.ChangeFreqency.DAILY);
          create(xmlUrlSet, users + "/calendar", Url.Priority.MEDIUMLOW, Url.ChangeFreqency.MONTHLY);
          create(xmlUrlSet, users + "/friends", Url.Priority.MEDIUMLOW, Url.ChangeFreqency.DAILY);
          create(xmlUrlSet, users + "/pictures", Url.Priority.LOW, Url.ChangeFreqency.WEEKLY);
          create(xmlUrlSet, users + "/favorites", Url.Priority.LOW, Url.ChangeFreqency.WEEKLY);
          create(xmlUrlSet, users + "/rss", Url.Priority.MEDIUM, Url.ChangeFreqency.DAILY);
          create(xmlUrlSet, users + "/atom", Url.Priority.MEDIUM, Url.ChangeFreqency.DAILY);
        }
      }
    }

    return xmlUrlSet;
  }

  /**
   * Create Link
   *
   * @param xmlUrlSet link set
   * @param link link to add to sitemap
   * @param priority indexing priority
   * @param changeFreqency how often link gets updated/changed
   */
  private void create(
      final UrlSet xmlUrlSet,
      final String link,
      final Url.Priority priority,
      final Url.ChangeFreqency changeFreqency) {
    xmlUrlSet.addUrl(new Url(link, priority, changeFreqency));
  }
}
