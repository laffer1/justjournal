package com.justjournal.ctl;

import com.justjournal.model.*;
import com.justjournal.repository.SettingsRepository;
import com.justjournal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET)
    @ResponseBody
    public XmlUrlSet main() {

        final Settings baseUri = settingsRepository.findByName("baseuri");

        final XmlUrlSet xmlUrlSet = new XmlUrlSet();
        create(xmlUrlSet, baseUri.getValue(), XmlUrl.Priority.HIGH, XmlUrl.ChangeFreqency.MONTHLY);

        for (final User user : userRepository.findAll()) {
            if (user.getUserPref().getAllowSpider().equals(PrefBool.Y)) {
                final String users = baseUri.getValue() + "users/" + user.getUsername();

                create(xmlUrlSet, users, XmlUrl.Priority.HIGH, XmlUrl.ChangeFreqency.DAILY);
                create(xmlUrlSet, users + "/calendar", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.MONTHLY);
                create(xmlUrlSet, users + "/friends", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.DAILY);
                create(xmlUrlSet, users + "/pictures", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.WEEKLY);
                create(xmlUrlSet, users + "/favorites", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.WEEKLY);
                create(xmlUrlSet, users + "/rss", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.DAILY);
                create(xmlUrlSet, users + "/atom", XmlUrl.Priority.MEDIUM, XmlUrl.ChangeFreqency.DAILY);
            }
        }

        return xmlUrlSet;
    }

    private void create(final XmlUrlSet xmlUrlSet, final String link, final XmlUrl.Priority priority, XmlUrl.ChangeFreqency changeFreqency) {
        xmlUrlSet.addUrl(new XmlUrl(link, priority, changeFreqency));
    }

}

