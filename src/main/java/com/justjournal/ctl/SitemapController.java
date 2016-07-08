package com.justjournal.ctl;

import com.justjournal.model.*;
import com.justjournal.repository.SettingsRepository;
import com.justjournal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
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
        create(xmlUrlSet, baseUri.getValue(), XmlUrl.Priority.HIGH);

        for (final User user : userRepository.findAll()) {
            if (user.getUserPref().getAllowSpider().equals(PrefBool.Y)) {
                create(xmlUrlSet, baseUri.getValue() + "/users/" + user.getUsername(), XmlUrl.Priority.HIGH);

                create(xmlUrlSet, baseUri.getValue() + "/users/" + user.getUsername() + "/calendar", XmlUrl.Priority.MEDIUM);
                create(xmlUrlSet, baseUri.getValue() + "/users/" + user.getUsername() + "/friends", XmlUrl.Priority.MEDIUM);
                create(xmlUrlSet, baseUri.getValue() + "/users/" + user.getUsername() + "/pictures", XmlUrl.Priority.MEDIUM);
                create(xmlUrlSet, baseUri.getValue() + "/users/" + user.getUsername() + "/favorites", XmlUrl.Priority.MEDIUM);
                create(xmlUrlSet, baseUri.getValue() + "/users/" + user.getUsername() + "/rss", XmlUrl.Priority.MEDIUM);
                create(xmlUrlSet, baseUri.getValue() + "/users/" + user.getUsername() + "/atom", XmlUrl.Priority.MEDIUM);
            }
        }

        return xmlUrlSet;
    }

    private void create(final XmlUrlSet xmlUrlSet, final String link, final XmlUrl.Priority priority) {
        xmlUrlSet.addUrl(new XmlUrl(link, priority));
    }

}

