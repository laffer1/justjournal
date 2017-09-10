package com.justjournal.services;

import com.justjournal.Login;
import com.justjournal.core.Settings;
import com.justjournal.model.*;
import com.justjournal.model.api.NewUser;
import com.justjournal.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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



    public User signup(final NewUser newUser) throws ServiceException, UnsupportedEncodingException, NoSuchAlgorithmException {
        final Style style = styleService.getDefaultStyle();

        User user = new User();
        user.setName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setUsername(newUser.getUsername());
        user.setPassword(Login.SHA1(newUser.getPassword()));
        user.setType(0);
        user.setSince(Calendar.getInstance().get(Calendar.YEAR));
        user.setLastLogin(new Date());
        user = userRepository.saveAndFlush(user);
        if (user == null)
            throw new ServiceException("Unable to save user");

        Journal journal = new Journal();
        journal.setStyle(style);
        journal.setUser(user);
        journal.setSlug(newUser.getUsername());
        journal.setAllowSpider(true);
        journal.setOwnerViewOnly(false);
        journal.setPingServices(true);
        journal.setName(user.getName() + "\'s Journal");
        journal.setSince(Calendar.getInstance().getTime());
        journal.setModified(Calendar.getInstance().getTime());
        journal = journalRepository.saveAndFlush(journal);
        if (journal == null)
            throw new ServiceException("Unable to save journal");

        UserPref userPref = new UserPref();
        userPref.setShowAvatar(PrefBool.N);
        userPref.setUser(user);
        userPref = userPrefRepository.save(userPref);
        if (userPref == null)
            throw new ServiceException("Unable to save user preferences");

        UserContact userContact = new UserContact();
        userContact.setEmail(newUser.getEmail());
        userContact.setUser(user);
        userContact = userContactRepository.save(userContact);
        if (userContact == null)
            throw new ServiceException("Unable to save user contact");

        UserBio userBio = new UserBio();
        userBio.setBio("");
        userBio.setUser(user);
        userBio = userBioDao.save(userBio);
        if (userBio == null)
            throw new ServiceException("Unable to save user bio");

        return user;
    }
}
