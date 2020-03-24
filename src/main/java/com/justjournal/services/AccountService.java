package com.justjournal.services;

import com.justjournal.Login;
import com.justjournal.model.Journal;
import com.justjournal.model.PasswordType;
import com.justjournal.model.PrefBool;
import com.justjournal.model.Style;
import com.justjournal.model.User;
import com.justjournal.model.UserBio;
import com.justjournal.model.UserContact;
import com.justjournal.model.UserPref;
import com.justjournal.model.api.NewUser;
import com.justjournal.repository.JournalRepository;
import com.justjournal.repository.UserBioRepository;
import com.justjournal.repository.UserContactRepository;
import com.justjournal.repository.UserPrefRepository;
import com.justjournal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBioRepository userBioDao;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UserPrefRepository userPrefRepository;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private StyleService styleService;

    public User signup(final NewUser newUser) throws ServiceException {
        final Style style = styleService.getDefaultStyle();

        User user = new User();
        user.setName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setUsername(newUser.getUsername());
        user.setPassword(Login.getHashedPassword(newUser.getUsername(), newUser.getPassword()));
        user.setPasswordType(PasswordType.SHA256);
        user.setType(0);
        user.setSince(Calendar.getInstance().get(Calendar.YEAR));
        user.setLastLogin(new Date());
        try {
            user = userRepository.saveAndFlush(user);
        } catch (final Exception e) {
            log.error("Unable to save user", e);
            throw new ServiceException("Unable to save user", e);
        }
        
        final Journal journal = new Journal();
        journal.setStyle(style);
        journal.setUser(user);
        journal.setSlug(newUser.getUsername());
        journal.setAllowSpider(true);
        journal.setOwnerViewOnly(false);
        journal.setPingServices(true);
        journal.setName(user.getName() + "\'s Journal");
        journal.setSince(Calendar.getInstance().getTime());
        journal.setModified(Calendar.getInstance().getTime());
        try {
            journalRepository.saveAndFlush(journal);
        } catch (final Exception e) {
            log.error("Could not save journal", e);
            throw new ServiceException("Unable to save journal");
        }

        final UserPref userPref = new UserPref();
        userPref.setShowAvatar(PrefBool.N);
        userPref.setUser(user);
        try {
            userPrefRepository.save(userPref);
        } catch (final Exception e) {
            log.error("Could not save user pref", e);
            throw new ServiceException("Unable to save user preferences");
        }

        final UserContact userContact = new UserContact();
        userContact.setEmail(newUser.getEmail());
        userContact.setUser(user);
        try {
            userContactRepository.save(userContact);
        } catch (final Exception e) {
            log.error("Could not save user contact", e);
            throw new ServiceException("Unable to save user contact");
        }

        final UserBio userBio = new UserBio();
        userBio.setBio("");
        userBio.setUser(user);
        try {
            userBioDao.save(userBio);
        } catch (final Exception e) {
            log.error("Could not save user bio", e);
            throw new ServiceException("Unable to save user bio");
        }
        return user;
    }
}
