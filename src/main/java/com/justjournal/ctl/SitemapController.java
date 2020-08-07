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
 * Dynamically generate sitemap from http://stackoverflow.com/questions/12289232/serving-sitemap-xml-and-robots-txt-with-spring-mvc
 *
 * @author Lucas Holt
 */
@RestController
public class SitemapController {

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/sitemap.xml", produces = "text/xml")
    @ResponseBody
    public UrlSet main() {

        final Settings baseUri = settingsRepository.findByName("baseuri");

        final UrlSet xmlUrlSet = new UrlSet();
        create(xmlUrlSet, baseUri.getValue(), Url.Priority.HIGH, Url.ChangeFreqency.MONTHLY);

        create(xmlUrlSet, baseUri.getValue() + "/RecentBlogs", Url.Priority.LOW, Url.ChangeFreqency.DAILY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/sitemap", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/sitemap", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/search", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/privacy", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/members", Url.Priority.LOW, Url.ChangeFreqency.DAILY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/support", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/support/bugs", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/moodlist", Url.Priority.LOW, Url.ChangeFreqency.YEARLY);

        for (final User user : userRepository.findAll()) {
            for (final Journal journal : user.getJournals()) {
                if (journal.isAllowSpider()) {
                    final String users = baseUri.getValue() + Constants.PATH_USERS + user.getUsername();

                    create(xmlUrlSet, users, Url.Priority.HIGH, Url.ChangeFreqency.DAILY);
                    create(xmlUrlSet, users + "/calendar", Url.Priority.MEDIUM, Url.ChangeFreqency.MONTHLY);
                    create(xmlUrlSet, users + "/friends", Url.Priority.MEDIUM, Url.ChangeFreqency.DAILY);
                    create(xmlUrlSet, users + "/pictures", Url.Priority.MEDIUM, Url.ChangeFreqency.WEEKLY);
                    create(xmlUrlSet, users + "/favorites", Url.Priority.MEDIUM, Url.ChangeFreqency.WEEKLY);
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
     * @param xmlUrlSet
     * @param link
     * @param priority
     * @param changeFreqency
     */
    private void create(final UrlSet xmlUrlSet, final String link,
                        final Url.Priority priority, final Url.ChangeFreqency changeFreqency) {
        xmlUrlSet.addUrl(new Url(link, priority, changeFreqency));
    }

}

