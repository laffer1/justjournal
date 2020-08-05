package com.justjournal.ctl;

import com.justjournal.core.Constants;
import com.justjournal.model.*;
import com.justjournal.repository.SettingsRepository;
import com.justjournal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Dynamically generate sitemap from http://stackoverflow.com/questions/12289232/serving-sitemap-xml-and-robots-txt-with-spring-mvc
 *
 * @author Lucas Holt
 */
@Controller
public class SitemapController {

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/sitemap.xml", produces = "text/xml")
    @ResponseBody
    public XmlUrlSet main() {

        final Settings baseUri = settingsRepository.findByName("baseuri");

        final XmlUrlSet xmlUrlSet = new XmlUrlSet();
        create(xmlUrlSet, baseUri.getValue(), XmlUrl.Priority.HIGH, XmlUrl.ChangeFreqency.MONTHLY);

        create(xmlUrlSet, baseUri.getValue() + "/RecentBlogs", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.DAILY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/sitemap", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/sitemap", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/search", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/privacy", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/members", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.DAILY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/support", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/support/bugs", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.YEARLY);
        create(xmlUrlSet, baseUri.getValue() + "/#!/moodlist", XmlUrl.Priority.LOW, XmlUrl.ChangeFreqency.YEARLY);

        for (final User user : userRepository.findAll()) {
            for (final Journal journal : user.getJournals()) {
                if (journal.isAllowSpider()) {
                    final String users = baseUri.getValue() + Constants.PATH_USERS + user.getUsername();

                    create(xmlUrlSet, users, XmlUrl.Priority.HIGH, XmlUrl.ChangeFreqency.DAILY);
                    create(xmlUrlSet, users + "/calendar", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.MONTHLY);
                    create(xmlUrlSet, users + "/friends", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.DAILY);
                    create(xmlUrlSet, users + "/pictures", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.WEEKLY);
                    create(xmlUrlSet, users + "/favorites", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.WEEKLY);
                    create(xmlUrlSet, users + "/rss", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.DAILY);
                    create(xmlUrlSet, users + "/atom", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.DAILY);
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
    private void create(final XmlUrlSet xmlUrlSet, final String link,
                        final XmlUrl.Priority priority, final XmlUrl.ChangeFreqency changeFreqency) {
        xmlUrlSet.addUrl(new XmlUrl(link, priority, changeFreqency));
    }

}

