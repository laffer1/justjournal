/*-
 * Copyright (c) 2003-2011, 2014 Lucas Holt
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

import com.justjournal.Cal;
import com.justjournal.ErrorPage;
import com.justjournal.Login;
import com.justjournal.atom.AtomFeed;
import com.justjournal.core.UserContext;
import com.justjournal.model.*;
import com.justjournal.model.search.BlogEntry;
import com.justjournal.repository.*;
import com.justjournal.rss.CachedHeadlineBean;
import com.justjournal.rss.Rss;
import com.justjournal.services.*;
import com.justjournal.utility.StringUtil;
import com.justjournal.utility.Xml;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Journal viewer for JustJournal.
 *
 * @author Lucas Holt
 */
@Transactional
@Controller
@RequestMapping("/users")
public class UsersController {
    private static final int SEARCH_MAX_LENGTH = 20;
    // constants
    private static final char ENDL = '\n';
    private static final Logger log = Logger.getLogger(UsersController.class);
    private static final String MODEL_USER = "user";
    private static final String MODEL_AUTHENTICATED_USER = "authenticatedUsername";
    private static final String MODEL_ENTRY = "entry";
    private static final String MODEL_CALENDAR_MINI = "calendarMini";
    private static final String MODEL_CALENDAR = "calendar";
    private static final String MODEL_PICTURES = "pictures";
    private static final String MODEL_FAVORITES = "favorites";
    private static final String MODEL_FRIENDS = "friends";
    private static final String VIEW_USERS = "users";
    private static final String PATH_USERNAME = "username";
    private static final String PATH_MONTH = "month";
    private static final String MODEL_AVATAR = "avatar";

    private static final String MEDIA_TYPE_RTF = "application/rtf";
    private static final String MEDIA_TYPE_PDF = "application/pdf";

    private final CommentRepository commentDao;

    private final EntryRepository entryDao;

    private final EntryService entryService;

    private final FavoriteRepository favoriteRepository;

    private final MoodThemeDataRepository emoticonDao;

    private final UserRepository userRepository;

    private final SecurityRepository securityDao;

    private final RssSubscriptionsRepository rssSubscriptionsDAO;

    private final UserImageService userImageService;

    private final UserPicRepository userPicRepository;

    private final BlogSearchService blogSearchService;

    private final Rss rss;

    @Autowired
    public UsersController(final EntryService entryService,
                           final @Qualifier("commentRepository") CommentRepository commentDao,
                           final @Qualifier("entryRepository") EntryRepository entryDao,
                           final FavoriteRepository favoriteRepository, final @Qualifier("moodThemeDataRepository") MoodThemeDataRepository emoticonDao,
                           final UserRepository userRepository, final SecurityRepository securityDao, final RssSubscriptionsRepository rssSubscriptionsDAO,
                           final UserImageService userImageService, final UserPicRepository userPicRepository,
                           final BlogSearchService blogSearchService,
                           final Rss rss) {
        this.entryService = entryService;
        this.commentDao = commentDao;
        this.entryDao = entryDao;
        this.favoriteRepository = favoriteRepository;
        this.emoticonDao = emoticonDao;
        this.userRepository = userRepository;
        this.securityDao = securityDao;
        this.rssSubscriptionsDAO = rssSubscriptionsDAO;
        this.userImageService = userImageService;
        this.userPicRepository = userPicRepository;
        this.blogSearchService = blogSearchService;
        this.rss = rss;
    }

    /**
     * Do we have an avatar?
     * @param userId  user id
     * @return  true if avatar, false otherwise
     */
    private boolean avatar(final int userId) {
        return userPicRepository.exists(userId);
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String entries(@PathVariable(PATH_USERNAME) final String username,
                          final Pageable pageable,
                          final Model model,
                          final HttpSession session,
                          final HttpServletResponse response) {
        final UserContext userContext = getUserContext(username, session);

        if (userContext == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userContext.getBlogUser());

        if (userContext.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userContext.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userContext));
        model.addAttribute(MODEL_PICTURES, null);

        model.addAttribute(MODEL_AVATAR, avatar(userContext.getBlogUser().getId()));

        model.addAttribute("pageable", pageable);

        model.addAttribute("entries", getEntries(userContext, pageable));
        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/entry/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String entry(@PathVariable(PATH_USERNAME) final String username,
                        @PathVariable("id") final int id,
                        final Model model, final HttpSession session, final HttpServletResponse response) {

        final UserContext userContext = getUserContext(username, session);

        if (userContext == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userContext.getBlogUser());

        if (userContext.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userContext.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userContext));
        model.addAttribute(MODEL_PICTURES, null);

        final Entry entry = getEntry(id, userContext);
        model.addAttribute(MODEL_ENTRY, entry);
        model.addAttribute(MODEL_ENTRY + "_format", getSingleEntry(entry, userContext));

        model.addAttribute(MODEL_AVATAR, avatar(userContext.getBlogUser().getId()));

        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/favorites", method = RequestMethod.GET, produces = "text/html")
      public String favorites(@PathVariable(PATH_USERNAME) final String username,
                            final Model model,
                            final HttpSession session,
                            final HttpServletResponse response) {
          final UserContext userc = getUserContext(username, session);

          if (userc == null) {
              response.setStatus(HttpServletResponse.SC_NOT_FOUND);
              return "";
          }

          model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
          model.addAttribute(MODEL_USER, userc.getBlogUser());

          if (userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userc.isAuthBlog()) {
              response.setStatus(HttpServletResponse.SC_FORBIDDEN);
              return "";
          }

          model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userc));
          model.addAttribute(MODEL_PICTURES, null);
        model.addAttribute(MODEL_AVATAR, avatar(userc.getBlogUser().getId()));

          try {
              model.addAttribute(MODEL_FAVORITES, getFavorites(userc));
          } catch (final ServiceException se) {
              log.error(se.getMessage(), se);
          }
          return VIEW_USERS;
      }

    @RequestMapping(value = "{username}/friends", method = RequestMethod.GET, produces = "text/html")
    public String friends(@PathVariable(PATH_USERNAME) final String username,
                          final Model model,
                          final HttpSession session,
                          final HttpServletResponse response) {
        final UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userc.getBlogUser());

        if (userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userc));
        model.addAttribute(MODEL_PICTURES, null);
        model.addAttribute(MODEL_AVATAR, avatar(userc.getBlogUser().getId()));

        try {
            model.addAttribute(MODEL_FRIENDS, getFriends(userc));
        } catch (final ServiceException se) {
            log.error(se.getMessage(), se);
        }
        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/calendar", method = RequestMethod.GET, produces = "text/html")
    public String calendar(@PathVariable(PATH_USERNAME) final String username,
                           final Model model, final HttpSession session, final HttpServletResponse response) {

        final UserContext userContext = getUserContext(username, session);

        if (userContext == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userContext.getBlogUser());

        if (userContext.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userContext.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userContext));
        model.addAttribute(MODEL_PICTURES, null);
        model.addAttribute(MODEL_AVATAR, avatar(userContext.getBlogUser().getId()));

        final Calendar cal = Calendar.getInstance();
        final Integer year = cal.get(Calendar.YEAR);

        model.addAttribute("startYear", userContext.getBlogUser().getSince());
        model.addAttribute("currentYear", year);
        final List<Integer> years = new ArrayList<Integer>();
        for (int i = userContext.getBlogUser().getSince(); i <= year; i++) {
            years.add(i);
        }
        model.addAttribute("years", years);

        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/{year}", method = RequestMethod.GET, produces = "text/html")
    public String calendarYear(@PathVariable(PATH_USERNAME) final String username,
                               @PathVariable("year") final int year,
                               final Model model,
                               final HttpSession session,
                               final HttpServletResponse response) {
        final UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userc.getBlogUser());

        if (userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userc));
        model.addAttribute(MODEL_PICTURES, null);
        model.addAttribute(MODEL_AVATAR, avatar(userc.getBlogUser().getId()));

        model.addAttribute(MODEL_CALENDAR, getCalendar(year, userc));

        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/{year}/{month}", method = RequestMethod.GET, produces = "text/html")
    public String calendarMonth(@PathVariable(PATH_USERNAME) final String username,
                                @PathVariable("year") final int year,
                                @PathVariable(PATH_MONTH) final int month,
                                final Model model,
                                final HttpSession session, final HttpServletResponse response) {
        final UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userc.getBlogUser());

        if (userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userc));
        model.addAttribute(MODEL_PICTURES, null);
        model.addAttribute(MODEL_AVATAR, avatar(userc.getBlogUser().getId()));

        model.addAttribute(MODEL_CALENDAR, getCalendarMonth(year, month, userc));

        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/{year}/{month}/{day}", method = RequestMethod.GET, produces = "text/html")
    public String calendarDay(@PathVariable(PATH_USERNAME) final String username,
                              @PathVariable("year") final int year,
                              @PathVariable(PATH_MONTH) final int month,
                              @PathVariable("day") final int day, final Model model, final HttpServletResponse response, final HttpSession session) {
        final UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userc.getBlogUser());

        if (userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userc));
        model.addAttribute(MODEL_PICTURES, null);
        model.addAttribute(MODEL_AVATAR, avatar(userc.getBlogUser().getId()));

        model.addAttribute(MODEL_CALENDAR, getCalendarDay(year, month, day, userc));

        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/atom", method = RequestMethod.GET, produces = "text/xml; charset=UTF-8")
    @ResponseBody
    public
    String atom(@PathVariable(PATH_USERNAME) final String username, final HttpServletResponse response) {
        try {
            final User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            if (user.getJournals().get(0).isOwnerViewOnly()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return "";
            }

            return getAtom(user);
        } catch (final Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "";
        }
    }

    @RequestMapping(value = "{username}/rss", method = RequestMethod.GET, produces = "application/rss+xml; charset=ISO-8859-1")
    @ResponseBody
    public
    String rss(@PathVariable(PATH_USERNAME) final String username, final HttpServletResponse response) {
        try {
            final User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            if (user.getJournals().get(0).isOwnerViewOnly()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return "";
            }

            return getRSS(user);
        } catch (final Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "";
        }
    }

    @RequestMapping(value = "{username}/rsspics", method = RequestMethod.GET, produces = "application/rss+xml; charset=ISO-8859-1")
    @ResponseBody
    public
    String rssPictures(@PathVariable(PATH_USERNAME) final String username, final HttpServletResponse response) {
        try {
            final User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            if (user.getJournals().get(0).isOwnerViewOnly()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return "";
            }

            return getPicturesRSS(user);
        } catch (final Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "";
        }
    }

    @RequestMapping(value = "{username}/pdf", method = RequestMethod.GET, produces = MEDIA_TYPE_PDF)
    public void pdf(@PathVariable(PATH_USERNAME) final String username, final HttpServletResponse response, final HttpSession session) {
        User authUser = null;
        try {
            authUser = userRepository.findByUsername(Login.currentLoginName(session));
        } catch (final Exception e) {
           log.trace(e.getMessage(), e);
        }

        try {
            final User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final UserContext userc = new UserContext(user, authUser);
            if (! user.getJournals().get(0).isOwnerViewOnly() || userc.isAuthBlog()) {
                getPDF(response, userc);
            } else
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (final Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "{username}/rtf", method = RequestMethod.GET, produces = MEDIA_TYPE_RTF)
    public void rtf(@PathVariable(PATH_USERNAME) final String username, final HttpServletResponse response, final HttpSession session) {
        User authUser = null;
        try {
            authUser = userRepository.findByUsername(Login.currentLoginName(session));
        } catch (final Exception e) {
            log.trace(e.getMessage(), e);
        }

        try {
            final User user = userRepository.findByUsername(username);
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final UserContext userc = new UserContext(user, authUser);
            if (!user.getJournals().get(0).isOwnerViewOnly() || userc.isAuthBlog())
                getRTF(response, userc);
            else
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (final Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "{username}/pictures", method = RequestMethod.GET, produces = "text/html")
    public String pictures(@PathVariable(PATH_USERNAME) final String username, final Model model,
                           final HttpSession session, final HttpServletResponse response) {
        final UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userc.getBlogUser());

        if (userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userc));

        model.addAttribute(MODEL_PICTURES, userImageService.getUserImages(username));

        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/search", method = RequestMethod.GET, produces = "text/html")
    public String search(@PathVariable(PATH_USERNAME) final String username,
                         @RequestParam("max") final String max,
                         @RequestParam("bquery") final String bquery,
                         final Model model, final HttpSession session, final HttpServletResponse response) {
        final UserContext userc = getUserContext(username, session);
        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userc.getBlogUser());

        if (userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userc));
        model.addAttribute(MODEL_PICTURES, null);


        int maxr = SEARCH_MAX_LENGTH;

        if (max != null && max.length() > 0)
            try {
                maxr = Integer.parseInt(max);
            } catch (final NumberFormatException exInt) {
                maxr = SEARCH_MAX_LENGTH;
                log.error(exInt.getMessage());
            }

        model.addAttribute("search", search(userc, maxr, bquery));

        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/subscriptions", method = RequestMethod.GET, produces = "text/html")
    public String subscriptions(@PathVariable(PATH_USERNAME) final String username,
                                final Model model, final HttpSession session, final HttpServletResponse response) {
        final UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));
        model.addAttribute(MODEL_USER, userc.getBlogUser());

        if (userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute(MODEL_CALENDAR_MINI, getCalendarMini(userc));
        model.addAttribute(MODEL_PICTURES, null);

        model.addAttribute("subscriptions", getSubscriptions(userc));

        return VIEW_USERS;
    }

    @RequestMapping(value = "{username}/tag/{tag}", method = RequestMethod.GET, produces = "text/html")
    public String tag(@PathVariable(PATH_USERNAME) final String username,
                      @PathVariable("tag") final String tag,
                      final Model model, final HttpSession session, final HttpServletResponse response) {

        User authUser = null;
        try {
            authUser = userRepository.findByUsername(Login.currentLoginName(session));
        } catch (final Exception e) {
              log.trace(e.getMessage(), e);
        }

        try {
            final User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            final UserContext userc = new UserContext(user, authUser);

            model.addAttribute(MODEL_USER, user);
            model.addAttribute(MODEL_AUTHENTICATED_USER, Login.currentLoginName(session));

            if (! userc.getBlogUser().getJournals().get(0).isOwnerViewOnly() || userc.isAuthBlog())
                model.addAttribute("tags", getTags(userc, tag));
            else
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (final Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return VIEW_USERS;
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
    protected UserContext getUserContext(final String username, final HttpSession session) {
        User authUser = null;
        try {
            authUser = userRepository.findByUsername(Login.currentLoginName(session));
        } catch (final Exception e) {
            log.trace(e.getMessage(), e);
        }

        try {
            final User user = userRepository.findByUsername(username);
            if (user == null || user.getId() == 0)
                return null;

            return new UserContext(user, authUser);
        } catch (final Exception e) {
            log.error(e);
        }
        return null;
    }

    @Autowired
    private PdfFormatService pdfFormatService;

    private void setCommonFileHeaders(final HttpServletResponse response, final UserContext uc, String contentType, String fileExtension) {
        response.setContentType(contentType);
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // RFC 1806
        response.setHeader("Content-Disposition", "attachment; filename=" + uc.getBlogUser().getUsername() + fileExtension);
    }

    private void getPDF(final HttpServletResponse response, final UserContext uc) {

        try {
            response.resetBuffer();
            setCommonFileHeaders(response, uc, MEDIA_TYPE_PDF, ".pdf");
           
            final ServletOutputStream os = response.getOutputStream();
            pdfFormatService.write(uc, os);
            os.close();
        } catch (final IOException e1) {
            log.error("Users.getPDF() IOException:" + e1.getMessage(), e1);
        } catch (final Exception e) {
            // user class caused this
            log.error("Users.getPDF():" + e.getMessage(), e);
        }
    }

    @Autowired
    private RdfFormatService rdfFormatService;

    private void getRTF(final HttpServletResponse response, final UserContext uc) {
        try {
            final ByteArrayOutputStream baos = rdfFormatService.generate(uc);

            setCommonFileHeaders(response, uc, MEDIA_TYPE_RTF, ".rtf");
            response.setContentLength(baos.size()); /* required by IE */
            final ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
            out.close();
        } catch (final IOException e1) {
            log.error("Users.getRDF() IOException:" + e1.getMessage(), e1);
        } catch (final Exception e) {
            // user class caused this
            log.error("Users.getRDF():" + e.getMessage(), e);
        }
    }

    @Autowired
    private CachedHeadlineBean cachedHeadlineBean;

    private String getSubscriptions(final UserContext uc) {
        final StringBuilder sb = new StringBuilder();

        sb.append("<h2>RSS Reader</h2>");
        sb.append(ENDL);
        sb.append("<p><a href=\"javascript:sweeptoggle('contract')\">Contract All</a> | <a href=\"javascript:sweeptoggle('expand')\">Expand All</a></p>");
        sb.append(ENDL);


        try {
            final Collection<RssSubscription> rssfeeds = rssSubscriptionsDAO.findByUser(uc.getBlogUser());

            /* Iterator */
            RssSubscription o;
            final Iterator itr = rssfeeds.iterator();
            for (int i = 0, n = rssfeeds.size(); i < n; i++) {
                o = (RssSubscription) itr.next();

                log.info("Fetching RSS feed: " + o.getUri());
                sb.append(cachedHeadlineBean.parse(o.getUri()));
                sb.append(ENDL);
            }

        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return sb.toString();
    }


    /**
     * Get a blog entry following the security rules as follows:
     * if the entry belongs to the blog, check the remaining rules otherwise just punt with null.
     * if public entry, return entry
     * if private entry and logged in as owner of entry, return entry
     * if protected and owner, return entry
     * if protected and third party and in owner's friends list, return entry
     *
     * @param entryId entry id
     * @param uc user context
     * @return blog entry
     */
    private Entry getEntry(final int entryId, final UserContext uc) {
        final Entry entry = entryDao.findOne(entryId);
        final int entryUserId = entry.getUser().getId();

        // only show blog entries for the owner of the blog
        if (entryUserId != uc.getBlogUser().getId())
            return null;

        // everyone can see public entries
        if (entry.getSecurity().getName().equals("public"))
            return entry;

        final int authUserId = uc.getAuthenticatedUser().getId();

        // since we know the blog owner owns the entry and the auth user is the entry user, we can assume
        // private or protected access of their own blog is fine.
        if (entryUserId == authUserId)
            return entry;

        if (entry.getSecurity().getName().equals("protected")) {
            for (final Friend friend : uc.getBlogUser().getFriends()) {
                if (friend.getFriend().getId() == authUserId)
                    return entry;
            }
        }

        return null;
    }

    private String getSingleEntry(final Entry o, final UserContext uc) {

        final StringBuilder sb = new StringBuilder();

            try {

                if (o != null && o.getId() > 0) {
                    final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");

                    final String curDate = formatmydate.format(o.getDate());

                    sb.append("<h2>");
                    sb.append(curDate);
                    sb.append("</h2>");
                    sb.append(ENDL);

                    sb.append(formatEntry(uc, o, o.getDate(), true));
                }
            } catch (final Exception e1) {
                log.error("getSingleEntry: " + e1.getMessage() + '\n', e1);

                ErrorPage.Display("Error",
                        "Unable to retrieve journal entry from data store.",
                        sb);
            }

        return sb.toString();
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    private String search(final UserContext uc, final int maxResults, final String term) {
        final StringBuilder sb = new StringBuilder();

        final PageRequest page = new PageRequest(0, maxResults);
        final Page<BlogEntry> result;

        if (term != null && term.length() > 0) {
            if (uc.isAuthBlog())
                result = blogSearchService.search(term, uc.getAuthenticatedUser().getUsername(), page);
            else
                result = blogSearchService.publicSearch(term, uc.getBlogUser().getUsername(), page);

            try {

                sb.append("<h2><img src=\"/images/icon_search.gif\" alt=\"Search Blog\" /></h2>");
                sb.append(ENDL);
                sb.append("<p>Searching for <i>" + term + "</i></p>");

                if (result == null || result.getTotalElements() == 0) {
                    sb.append("<p>No items were found matching your search criteria.</p>");
                    sb.append(ENDL);
                } else {

                    for (final BlogEntry blogEntry : result.getContent())
                     {
                        // Format the current time.
                        final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");
                        final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");
                         String curDate = "";

                        final Date currentDate = blogEntry.getDate();

                        if (currentDate != null)
                            curDate = formatmydate.format(currentDate);

                        sb.append("<h2>");
                        sb.append(curDate);
                        sb.append("</h2>");
                        sb.append(ENDL);
                         
                        sb.append("<div class=\"ebody\">");
                        sb.append(ENDL);

                        sb.append("<h3>");
                        sb.append("<span class=\"time\">");
                        sb.append(formatmytime.format(currentDate));
                        sb.append("</span> - <span class=\"subject\"><a href=\"/users/")
                                .append(blogEntry.getAuthor()).append("/entry/").append(blogEntry.getId().toString()).append("\">");
                        sb.append(Xml.cleanString(blogEntry.getSubject()));
                        sb.append("</a></span></h3> ");
                        sb.append(ENDL);

                        sb.append("<div class=\"ebody\">");
                        sb.append(ENDL);
                        sb.append(blogEntry.getBody());
                        sb.append(ENDL);
                        sb.append("</div>");
                        sb.append(ENDL);
                    }
                }

            } catch (final Exception e1) {
                log.error( e1.getMessage(), e1);
            }
        }
        return sb.toString();
    }

    private String getEntries(final UserContext uc, final Pageable pageable) {
        final StringBuilder sb = new StringBuilder();
        final Page<Entry> entries;

        try {
            if (uc.isAuthBlog()) {
                entries =
                       // entryService.getEntries(uc.getBlogUser().getUsername(), pageable);
                        entryDao.findByUserOrderByDateDesc(uc.getBlogUser(), pageable);

                log.debug("getEntries: User is logged in.");
            } else {
                entries = //entryService.getPublicEntries(uc.getBlogUser().getUsername(), pageable);
                       entryDao.findByUserAndSecurityOrderByDateDesc(uc.getBlogUser(), securityDao.findOne(2), pageable);

                log.debug("getEntries: User is not logged in.");
            }

            // Format the current time.
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");

            String lastDate = "";
            String curDate;

            log.debug("getEntries: Begin Iteration of records.");

            for (final Entry o : entries) {
                // Parse the previous string back into a Date.
                final ParsePosition pos = new ParsePosition(0);
                final Date currentDate = formatter.parse(new DateTimeBean(o.getDate()).toString(), pos); // TODO: seems inefficient

                curDate = formatmydate.format(currentDate);

                if (curDate.compareTo(lastDate) != 0) {
                    sb.append("\t\t<h2>");
                    sb.append(curDate);
                    sb.append("</h2>");
                    sb.append(ENDL);
                    lastDate = curDate;
                }

                sb.append(formatEntry(uc, o, currentDate, false));
            }
        } catch (final Exception e1) {
            ErrorPage.Display("Error", "Unable to retrieve journal entries from data store.", sb);
            log.error("getEntries: Exception is " + e1.getMessage(), e1);
        }
        return sb.toString();
    }

    private String getFavorites(final UserContext uc) throws ServiceException {
        final StringBuilder sb = new StringBuilder();
        final Collection<Entry> entries = new ArrayList<Entry>();

        final List<Favorite> favorites = favoriteRepository.findByUser(uc.getBlogUser());
        final boolean auth = uc.getAuthenticatedUser() != null;

        for (final Favorite fav : favorites) {
            final Entry e = fav.getEntry();

            // if the blog entry belongs to the user or it's public, render it.
            // TODO: handle friends posts
            if (e.getSecurity().getId() == 2 ||
                    auth && (e.getUser().getId() == uc.getAuthenticatedUser().getId()))
                entries.add(e);
        }

          sb.append("<h2>Favorites</h2>");
          sb.append(ENDL);

          try {
              log.trace("getFavorites: Init Date Parsers.");

              // Format the current time.
              final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
              final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");
              final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");
              String lastDate = "";
              String curDate;

              /* Iterator */
              Entry o;
              final Iterator itr = entries.iterator();

              log.debug("getFavoritess: Number of entries " + entries.size());

              if (entries.isEmpty())
                  sb.append("<p>No favorite entries found</p>.");

              for (int i = 0, n = entries.size(); i < n; i++) {
                  o = (Entry) itr.next();

                  // Parse the previous string back into a Date.
                  final ParsePosition pos = new ParsePosition(0);
                  final Date currentDate = formatter.parse(new DateTimeBean(o.getDate()).toString(), pos);

                  curDate = formatmydate.format(currentDate);

                  if (curDate.compareTo(lastDate) != 0) {
                      sb.append("<h2>");
                      sb.append(curDate);
                      sb.append("</h2>");
                      sb.append(ENDL);
                      lastDate = curDate;
                  }

                  sb.append("<div class=\"ebody\">");
                  sb.append(ENDL);

                  // final User p = o.getUser();
                  //  if (p.showAvatar()) {   TODO: avatar?
                  sb.append("<img alt=\"avatar\" style=\"float: right\" src=\"/image?id=");
                  sb.append(o.getUser().getId());
                  sb.append("\"/>");
                  sb.append(ENDL);
                  //  }

                  sb.append("<h3>");
                  sb.append("<a href=\"/users/");
                  sb.append(o.getUser().getUsername());
                  sb.append("\" title=\"");
                  sb.append(o.getUser().getUsername());
                  sb.append("\">");
                  sb.append(o.getUser().getUsername());
                  sb.append("</a> ");

                  sb.append("<span class=\"time\">");
                  sb.append(formatmytime.format(currentDate));
                  sb.append("</span> - <span class=\"subject\">");
                  sb.append(Xml.cleanString(o.getSubject()));
                  sb.append("</span></h3> ");
                  sb.append(ENDL);

                  sb.append("<div class=\"ebody\">");
                  sb.append(ENDL);

                  // Keep this synced with getEntries()
                  if (o.getAutoFormat() != null && o.getAutoFormat() == PrefBool.Y) {
                      sb.append("<p>");
                      if (o.getBody().contains("\n"))
                          sb.append(StringUtil.replace(o.getBody(), '\n', "<br />"));
                      else if (o.getBody().contains("\r"))
                          sb.append(StringUtil.replace(o.getBody(), '\r', "<br />"));
                      else
                          // we do not have any "new lines" but it might be
                          // one long line.
                          sb.append(o.getBody());

                      sb.append("</p>");
                  } else {
                      sb.append(o.getBody());
                  }

                  sb.append(ENDL);
                  sb.append("</div>");
                  sb.append(ENDL);

                  sb.append("<p>");

                  if (o.getSecurity() == null || o.getSecurity().getId() == 0) {
                      sb.append("<span class=\"security\">security: ");
                      sb.append("<img src=\"/img/icon_private.gif\" alt=\"private\" /> ");
                      sb.append("private");
                      sb.append("</span><br />");
                      sb.append(ENDL);
                  } else if (o.getSecurity().getId() == 1) {
                      sb.append("<span class=\"security\">security: ");
                      sb.append("<img src=\"/img/icon_protected.gif\" alt=\"friends\" /> ");
                      sb.append("friends");
                      sb.append("</span><br />");
                      sb.append(ENDL);
                  }

                  if (o.getLocation() != null && o.getLocation().getId() > 0) {
                      sb.append("<span class=\"location\">location: ");
                      sb.append(o.getLocation().getTitle());
                      sb.append("</span><br />");
                      sb.append(ENDL);
                  }

                  if (o.getMood() != null && o.getMood().getTitle().length() > 0 && o.getMood().getId() != 12) {
                      final MoodThemeData emoto = emoticonDao.findByThemeIdAndMoodId(1, o.getMood().getId());

                      if (emoto != null) {
                          sb.append("<span class=\"mood\">mood: <img src=\"/images/emoticons/1/");
                          sb.append(emoto.getFileName());
                          sb.append("\" width=\"");
                          sb.append(emoto.getWidth());
                          sb.append("\" height=\"");
                          sb.append(emoto.getHeight());
                          sb.append("\" alt=\"");
                          sb.append(o.getMood().getTitle());
                          sb.append("\" /> ");
                          sb.append(o.getMood().getTitle());
                          sb.append("</span><br>");
                          sb.append(ENDL);
                      }
                  }

                  if (o.getMusic() != null && o.getMusic().length() > 0) {
                      sb.append("<span class=\"music\">music: ");
                      sb.append(Xml.cleanString(o.getMusic()));
                      sb.append("</span><br>");
                      sb.append(ENDL);
                  }

                  sb.append("</p>");
                  sb.append(ENDL);

                  if (o.getTags() != null && !o.getTags().isEmpty()) {
                      sb.append("<p>tags:");
                      for (final EntryTag s : o.getTags()) {
                          sb.append(" ");
                          sb.append(s.getTag().getName());
                      }
                      sb.append("</p>");
                      sb.append(ENDL);
                  }

                  sb.append("<div>");
                  sb.append(ENDL);
                  sb.append("<table width=\"100%\"  border=\"0\">");
                  sb.append(ENDL);
                  sb.append("<tr>");
                  sb.append(ENDL);

                  if (uc.getAuthenticatedUser() != null && uc.getAuthenticatedUser().getId() == o.getUser().getId()) {
                      sb.append("<td width=\"30\"><a title=\"Edit Entry\" href=\"/#/entry/").append(o.getId());
                      sb.append("\"><i class=\"fa fa-pencil-square-o\"></i></a></td>");
                      sb.append(ENDL);
                      sb.append("<td width=\"30\"><a title=\"Delete Entry\" onclick=\"return deleteEntry(").append(o.getId()).append(")\"");
                      sb.append("><i class=\"fa fa-trash-o\"></i></a>");
                      sb.append("</td>");
                      sb.append(ENDL);

                      sb.append("<td width=\"30\"><a title=\"Remove Favorite\" onclick=\"return deleteFavorite(");
                      sb.append(o.getId());
                      sb.append(")\"><i class=\"fa fa-heart-o\"></i></a></td>");
                      sb.append(ENDL);
                  } else if (uc.getAuthenticatedUser() != null) {
                      sb.append("<td width=\"30\"><a title=\"Add Favorite\" onclick=\"return addFavorite(");
                      sb.append(o.getId());
                      sb.append(")\"><i class=\"fa fa-heart\"></i></a></td>");
                      sb.append(ENDL);
                  }

                  sb.append("<td><div style=\"float: right\"><a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                  sb.append(o.getId());
                  sb.append("\" title=\"Link to this entry\">link</a> ");
                  sb.append('(');

                  switch (o.getComments().size()) {
                      case 0:
                          break;
                      case 1:
                          sb.append("<a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                          sb.append(o.getId());
                          sb.append("\" title=\"View Comment\">1 comment</a> | ");
                          break;
                      default:
                          sb.append("<a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                          sb.append(o.getId());
                          sb.append("\" title=\"View Comments\">");
                          sb.append(o.getComments().size());
                          sb.append(" comments</a> | ");
                  }

                  sb.append("<a href=\"/#!/comment/");
                  sb.append(o.getId());
                  sb.append("\" title=\"Leave a comment on this entry\">comment on this</a>)");

                  sb.append("</div></td>");
                  sb.append(ENDL);
                  sb.append("</tr>");
                  sb.append(ENDL);
                  sb.append("</table>");
                  sb.append(ENDL);
                  sb.append("</div>");
                  sb.append(ENDL);

                  sb.append("</div>");
                  sb.append(ENDL);
              }

          } catch (final Exception e1) {
              log.error(e1.getMessage(), e1);
              ErrorPage.Display(" Error", "Error retrieving favorite entries", sb);
          }
          return sb.toString();
      }

    /**
     * Displays friends entries for a particular user.
     *
     * @param uc The UserContext we are working on including blog owner, authenticated user, and sb to write
     */
    private String getFriends(final UserContext uc) throws ServiceException {
        final StringBuilder sb = new StringBuilder();
        final Collection entries;

  /*      if (uc.getAuthenticatedUser() != null)
            entries = entryDao.viewFriends(uc.getBlogUser().getUserId(), uc.getAuthenticatedUser().getUserId());
        else
            entries = entryDao.viewFriends(uc.getBlogUser().getUserId(), 0);
    */
        entries = entryService.getFriendsEntries(uc.getBlogUser().getUsername());
        sb.append("<h2>Friends</h2>");
        sb.append(ENDL);

        try {
            if (log.isDebugEnabled())
                log.debug("getFriends: Init Date Parsers.");

            // Format the current time.
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");
            final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");
            String lastDate = "";
            String curDate;

            /* Iterator */
            Entry o;
            final Iterator itr = entries.iterator();

            log.trace("getFriends: Number of entries " + entries.size());

            if (entries.isEmpty())
                sb.append("<p>No friends entries found</p>.");

            for (int i = 0, n = entries.size(); i < n; i++) {
                o = (Entry) itr.next();

                // Parse the previous string back into a Date.
                final ParsePosition pos = new ParsePosition(0);
                final Date currentDate = formatter.parse(new DateTimeBean(o.getDate()).toString(), pos);

                curDate = formatmydate.format(currentDate);

                if (curDate.compareTo(lastDate) != 0) {
                    sb.append("<h2>");
                    sb.append(curDate);
                    sb.append("</h2>");
                    sb.append(ENDL);
                    lastDate = curDate;
                }

                sb.append("<div class=\"ebody\">");
                sb.append(ENDL);

                // final User p = o.getUser();
                //  if (p.showAvatar()) {   TODO: avatar?
                sb.append("<img alt=\"avatar\" style=\"float: right\" src=\"/image?id=");
                sb.append(o.getUser().getId());
                sb.append("\"/>");
                sb.append(ENDL);
                //  }

                sb.append("<h3>");
                sb.append("<a href=\"/users/");
                sb.append(o.getUser().getUsername());
                sb.append("\" title=\"");
                sb.append(o.getUser().getUsername());
                sb.append("\">");
                sb.append(o.getUser().getUsername());
                sb.append("</a> ");

                sb.append("<span class=\"time\">");
                sb.append(formatmytime.format(currentDate));
                sb.append("</span> - <span class=\"subject\">");
                sb.append(Xml.cleanString(o.getSubject()));
                sb.append("</span></h3> ");
                sb.append(ENDL);

                sb.append("<div class=\"ebody\">");
                sb.append(ENDL);

                // Keep this synced with getEntries()
                if (o.getAutoFormat() == PrefBool.Y) {
                    sb.append("<p>");
                    if (o.getBody().contains("\n"))
                        sb.append(StringUtil.replace(o.getBody(), '\n', "<br />"));
                    else if (o.getBody().contains("\r"))
                        sb.append(StringUtil.replace(o.getBody(), '\r', "<br />"));
                    else
                        // we do not have any "new lines" but it might be
                        // one long line.
                        sb.append(o.getBody());

                    sb.append("</p>");
                } else {
                    sb.append(o.getBody());
                }

                sb.append(ENDL);
                sb.append("</div>");
                sb.append(ENDL);

                sb.append("<p>");

                if (o.getSecurity().getId() == 0) {
                    sb.append("<span class=\"security\">security: ");
                    sb.append("<img src=\"/img/icon_private.gif\" alt=\"private\" /> ");
                    sb.append("private");
                    sb.append("</span><br />");
                    sb.append(ENDL);
                } else if (o.getSecurity().getId() == 1) {
                    sb.append("<span class=\"security\">security: ");
                    sb.append("<img src=\"/img/icon_protected.gif\" alt=\"friends\" /> ");
                    sb.append("friends");
                    sb.append("</span><br />");
                    sb.append(ENDL);
                }

                if (o.getLocation().getId() > 0) {
                    sb.append("<span class=\"location\">location: ");
                    sb.append(o.getLocation().getTitle());
                    sb.append("</span><br />");
                    sb.append(ENDL);
                }

                if (o.getMood().getTitle().length() > 0 && o.getMood().getId() != 12) {
                    final MoodThemeData emoto = emoticonDao.findByThemeIdAndMoodId(1, o.getMood().getId());

                    if (emoto != null) {
                        sb.append("<span class=\"mood\">mood: <img src=\"/images/emoticons/1/");
                        sb.append(emoto.getFileName());
                        sb.append("\" width=\"");
                        sb.append(emoto.getWidth());
                        sb.append("\" height=\"");
                        sb.append(emoto.getHeight());
                        sb.append("\" alt=\"");
                        sb.append(o.getMood().getTitle());
                        sb.append("\" /> ");
                        sb.append(o.getMood().getTitle());
                        sb.append("</span><br>");
                        sb.append(ENDL);
                    }
                }

                if (o.getMusic().length() > 0) {
                    sb.append("<span class=\"music\">music: ");
                    sb.append(Xml.cleanString(o.getMusic()));
                    sb.append("</span><br>");
                    sb.append(ENDL);
                }

                sb.append("</p>");
                sb.append(ENDL);

                sb.append("<p>tags:");
                for (final EntryTag s : o.getTags()) {
                    sb.append(" ");
                    sb.append(s.getTag().getName());
                }
                sb.append("</p>");
                sb.append(ENDL);

                sb.append("<div>");
                sb.append(ENDL);
                sb.append("<table width=\"100%\"  border=\"0\">");
                sb.append(ENDL);
                sb.append("<tr>");
                sb.append(ENDL);

                if (uc.getAuthenticatedUser() != null && uc.getAuthenticatedUser().getId() == o.getUser().getId()) {
                    sb.append("<td width=\"30\"><a title=\"Edit Entry\" href=\"/#/entry/").append(o.getId());
                    sb.append("\"><i class=\"fa fa-pencil-square-o\"></i></a></td>");
                    sb.append(ENDL);
                    sb.append("<td width=\"30\"><a title=\"Delete Entry\" onclick=\"return deleteEntry(").append(o.getId()).append(")\"");
                    sb.append("><i class=\"fa fa-trash-o\"></i></a>");
                    sb.append("</td>");
                    sb.append(ENDL);

                    sb.append("<td width=\"30\"><a title=\"Add Favorite\" onclick=\"return addFavorite(\"");
                    sb.append(o.getId());
                    sb.append("\"><i class=\"fa fa-heart\"></i></a></td>");
                    sb.append(ENDL);
                } else if (uc.getAuthenticatedUser() != null) {
                    sb.append("<td width=\"30\"><a title=\"Add Favorite\" onclick=\"return addFavorite(\"");
                    sb.append(o.getId());
                    sb.append("\"><i class=\"fa fa-heart\"></i></a></td>");
                    sb.append(ENDL);
                }

                sb.append("<td><div style=\"float: right\"><a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                sb.append(o.getId());
                sb.append("\" title=\"Link to this entry\">link</a> ");
                sb.append('(');


                switch (o.getComments().size()) {
                    case 0:
                        break;
                    case 1:
                        sb.append("<a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                        sb.append(o.getId());
                        sb.append("\" title=\"View Comment\">1 comment</a> | ");
                        break;
                    default:
                        sb.append("<a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                        sb.append(o.getId());
                        sb.append("\" title=\"View Comments\">");
                        sb.append(o.getComments().size());
                        sb.append(" comments</a> | ");
                }

                sb.append("<a href=\"/#!/comment/");
                sb.append(o.getId());
                sb.append("\" title=\"Leave a comment on this entry\">comment on this</a>)");

                sb.append("</div></td>");
                sb.append(ENDL);
                sb.append("</tr>");
                sb.append(ENDL);
                sb.append("</table>");
                sb.append(ENDL);
                sb.append("</div>");
                sb.append(ENDL);

                sb.append("</div>");
                sb.append(ENDL);
            }

        } catch (final Exception e1) {
            log.error(e1.getMessage(), e1);
            ErrorPage.Display(" Error",
                    "Error retrieving the friends entries",
                    sb);
        }
        return sb.toString();
    }

    /**
     * Prints the calendar for the year specified for months with journal entries.  Other months are not printed.
     *
     * @param year The year to print
     * @param uc   The UserContext we are working on including blog owner, authenticated user, and sb to write
     * @see com.justjournal.Cal
     * @see com.justjournal.CalMonth
     */
    private String getCalendar(final int year,
                               final UserContext uc) {
        final StringBuilder sb = new StringBuilder();
        final GregorianCalendar calendar = new GregorianCalendar();
        final int yearNow = calendar.get(Calendar.YEAR);

        // print out header
        sb.append("<h2>Calendar: ");
        sb.append(year);
        sb.append("</h2>");
        sb.append(ENDL);

        sb.append("<p>The calendar lists months with journal entries.</p>");
        sb.append(ENDL);

        // BEGIN: YEARS
        sb.append("<p>");

        for (int i = yearNow; i >= uc.getBlogUser().getSince(); i--) {

            sb.append("<a href=\"/users/");
            sb.append(uc.getBlogUser().getUsername());
            sb.append('/');
            sb.append(i);
            sb.append("\">");
            sb.append(i);
            sb.append("</a> ");

            // just in case!
            if (i == 2002)
                break;
        }

        sb.append("</p>");
        sb.append(ENDL);
        // END: YEARS

        try {
            final Collection<Entry> entries;
            if (uc.isAuthBlog())
                entries = entryDao.findByUsernameAndYear(uc.getBlogUser().getUsername(), year);
            else
                entries = entryDao.findByUsernameAndYearAndSecurity(uc.getBlogUser().getUsername(), year, securityDao.findOne(2));

            if (entries == null || entries.isEmpty()) {
                sb.append("<p>Calendar data not available.</p>");
                sb.append(ENDL);
            } else {
                // we have calendar data!
                final Cal mycal = new Cal(entries);
                sb.append(mycal.render());
            }

        } catch (final Exception e1) {
            log.trace(e1.getMessage(), e1);
            ErrorPage.Display(" Error", "An error has occured rendering calendar.", sb);
        }

        return sb.toString();
    }

    /**
     * Lists all of the journal entries for the month specified in the year specified.
     *
     * @param year  the year to display data for
     * @param month the month we want
     * @param uc    The UserContext we are working on including blog owner, authenticated user, and sb to write
     */
    private String getCalendarMonth(final int year,
                                    final int month,
                                    final UserContext uc) {
        final StringBuilder sb = new StringBuilder();

        sb.append("<h2>Calendar: ");
        sb.append(month);
        sb.append('/');
        sb.append(year);
        sb.append("</h2>");
        sb.append(ENDL);

        sb.append("<p>This page lists all of the journal entries for the month.</p>");
        sb.append(ENDL);

        try {
            final Collection<Entry> entries;
            if (uc.isAuthBlog())
                entries = entryDao.findByUsernameAndYearAndMonth(uc.getBlogUser().getUsername(), year, month);
            else
                entries = entryDao.findByUsernameAndYearAndMonthAndSecurity(uc.getBlogUser().getUsername(), year, month, securityDao.findOne(2));

            if (entries.isEmpty()) {
                sb.append("<p>Calendar data not available.</p>");
                sb.append(ENDL);
            } else {

                final SimpleDateFormat formatmydate = new SimpleDateFormat("dd");
                final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");

                String curDate;
                String lastDate = "";

                for (final Entry Entry : entries) {

                    final Date currentDate = Entry.getDate();
                    curDate = formatmydate.format(currentDate);

                    if (curDate.compareTo(lastDate) != 0) {
                        sb.append("<p><strong>").append(curDate).append("</strong></p>");
                        lastDate = curDate;
                    }

                    sb.append("<p><span class=\"time\">").append(formatmytime.format(currentDate)).append("</span> - <span class=\"subject\"><a href=\"");

                    /*TODO: fix bug where relative url is incorrect
                       Need to check if we are in a calendar state and
                       drop the extra parts on the request.
                       it is appended 08/02/08/02 etc. */
                    if (month < 10)
                        sb.append('0');

                    sb.append(month).append('/').append(curDate).append("\">").append(Entry.getSubject()).append("</a></span></p> ");
                    sb.append(ENDL);
                }
            }

        } catch (final Exception e1) {
            log.trace(e1.getMessage(), e1);
            ErrorPage.Display(" Error", "An error has occured rendering calendar.", sb);
        }
        return sb.toString();
    }

    /**
     * Print a mini calendar for the current month with blog entries counts for given days in HTML.
     *
     * @param uc User Context
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Transactional(value = Transactional.TxType.SUPPORTS)
    protected String getCalendarMini(final UserContext uc) {
        final StringBuilder sb = new StringBuilder();
        try {
            final Calendar cal = new GregorianCalendar(TimeZone.getDefault());
            final int year = cal.get(Calendar.YEAR);
            final int month = cal.get(Calendar.MONTH) + 1; // zero based

            final Collection<Entry> entries;
            if (uc.isAuthBlog())
                entries = entryDao.findByUsernameAndYearAndMonth(uc.getBlogUser().getUsername(), year, month);
            else
                entries = entryDao.findByUsernameAndYearAndMonthAndSecurity(uc.getBlogUser().getUsername(), year, month, securityDao.findOne(2));


            if (entries.isEmpty()) {
                sb.append("\t<!-- could not render calendar -->");
                sb.append(ENDL);
            } else {
                final Cal mycal = new Cal(entries);
                mycal.setBaseUrl("/users/" + uc.getBlogUser().getUsername() + '/');
                sb.append(mycal.renderMini());
            }
        } catch (final Exception ex) {
            log.debug(ex);
        }
        return sb.toString();
    }


    /**
     * Generates all of the HTML to display journal entries for a particular day specified in the url.
     *
     * @param year  the year to display
     * @param month the month we want to look at
     * @param day   the day we are interested in
     * @param uc    The UserContext we are working on including blog owner, authenticated user, and sb to write
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    protected String getCalendarDay(final int year,
                                  final int month,
                                  final int day,
                                  final UserContext uc) {

        final StringBuilder sb = new StringBuilder();

        sb.append("<p>Lists all of the journal entries for the day.</p>");
        sb.append(ENDL);

        try {

            final Collection<Entry> entries;
            if (uc.isAuthBlog())
                entries = entryDao.findByUsernameAndYearAndMonthAndDay(uc.getBlogUser().getUsername(), year, month, day);
            else
                entries = entryDao.findByUsernameAndYearAndMonthAndDayAndSecurity(uc.getBlogUser().getUsername(), year, month, day, securityDao.findOne(2));

            if (entries == null || entries.isEmpty()) {
                sb.append("<p>Calendar data not available.</p>");
                sb.append(ENDL);
            } else {
                final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");

                String lastDate = "";
                String curDate;

                /* Iterator */
                Entry o;
                final Iterator itr = entries.iterator();

                for (int i = 0, n = entries.size(); i < n; i++) {
                    o = (Entry) itr.next();

                    // Parse the previous string back into a Date.
                    final ParsePosition pos = new ParsePosition(0);
                    final Date currentDate = formatter.parse(new DateTimeBean(o.getDate()).toString(), pos);

                    curDate = formatmydate.format(currentDate);

                    if (curDate.compareTo(lastDate) != 0) {
                        sb.append("\t\t<h2>");
                        sb.append(curDate);
                        sb.append("</h2>");
                        sb.append(ENDL);
                        lastDate = curDate;
                    }

                    sb.append(formatEntry(uc, o, currentDate, false));
                }
            }

        } catch (final Exception e1) {
            log.trace(e1.getMessage(), e1);
            ErrorPage.Display(" Error", "An error has occurred rendering calendar.", sb);
        }

        return sb.toString();
    }


    /**
     * Handles requests for syndication content (RSS). Only returns public journal entries for the specified user.
     *
     * @param user
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    protected String getRSS(final User user) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        rss.setTitle(user.getUsername());
        rss.setLink("http://www.justjournal.com/users/" + user.getUsername());
        rss.setSelfLink("http://www.justjournal.com/users/" + user.getUsername() + "/rss");
        rss.setDescription("Just Journal for " + user.getUsername());
        rss.setLanguage("en-us");
        rss.setCopyright("Copyright " + calendar.get(Calendar.YEAR) + ' ' + user.getFirstName());
        rss.setWebMaster("webmaster@justjournal.com (Lucas)");
        // RSS advisory board format
        rss.setManagingEditor(user.getUserContact().getEmail() + " (" + user.getFirstName() + ")");

        final Pageable page = new PageRequest(0, 15);
        rss.populate(entryDao.findByUserAndSecurityOrderByDateDesc(user, securityDao.findOne(2), page).getContent());
        return rss.toXml();
    }

    /**
     * Handles requests for syndication content (Atom). Only returns public journal entries for the specified user.
     *
     * @param user blog user
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    protected String getAtom(final User user) {
        final AtomFeed atom = new AtomFeed();

        final GregorianCalendar calendarg = new GregorianCalendar();
        calendarg.setTime(new Date());

        atom.setUserName(user.getUsername());
        atom.setAlternateLink("http://www.justjournal.com/users/" + user.getUsername());
        atom.setAuthorName(user.getFirstName());
        atom.setUpdated(calendarg.toString());
        atom.setTitle(user.getJournals().get(0).getName());
        atom.setId("http://www.justjournal.com/users/" + user.getUsername() + "/atom");
        atom.setSelfLink("/users/" + user.getUsername() + "/atom");
        final Pageable page = new PageRequest(0, 15);
        atom.populate(entryDao.findByUserAndSecurityOrderByDateDesc(user, securityDao.findOne(2), page).getContent());
        return atom.toXml();
    }

    
    /**
     * List the pictures associated with a blog in RSS.  This should be compatible with iPhoto.
     *
     * @param user blog user
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    protected String getPicturesRSS(final User user) {

        final GregorianCalendar calendarg = new GregorianCalendar();
        calendarg.setTime(new Date());

        rss.setTitle(user.getUsername() + "\'s pictures");
        rss.setLink("http://www.justjournal.com/users/" + user.getUsername() + "/pictures");
        rss.setSelfLink("http://www.justjournal.com/users/" + user.getUsername() + "/pictures/rss");
        rss.setDescription("Just Journal Pictures for " + user.getUsername());
        rss.setLanguage("en-us");
        rss.setCopyright("Copyright " + calendarg.get(Calendar.YEAR) + ' ' + user.getFirstName());
        rss.setWebMaster("webmaster@justjournal.com (Luke)");
        // RSS advisory board format
        rss.setManagingEditor(user.getUserContact().getEmail() + " (" + user.getFirstName() + ")");
        rss.populateImageList(user.getId(), user.getUsername());
        return rss.toXml();
    }


    @Transactional(value = Transactional.TxType.REQUIRED)
    protected String getTags(final UserContext uc, final String tag) {
        final StringBuilder sb = new StringBuilder();
        final Collection entries;

        try {
            if (uc.isAuthBlog()) {
                entries = entryDao.findByUsernameAndTag(uc.getBlogUser().getUsername(), tag);
            } else {
                entries = entryDao.findByUsernameAndSecurityAndTag(uc.getBlogUser().getUsername(), securityDao.findOne(2), tag);
            }

            // Format the current time.
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");

            String lastDate = "";
            String curDate;

            log.trace("getTags: Begin Iteration of records.");

            Entry o;
            final Iterator itr = entries.iterator();

            for (int i = 0, n = entries.size(); i < n; i++) {
                o = (Entry) itr.next();

                // Parse the previous string back into a Date.
                final ParsePosition pos = new ParsePosition(0);
                final Date currentDate = formatter.parse(new DateTimeBean(o.getDate()).toString(), pos);

                curDate = formatmydate.format(currentDate);

                if (curDate.compareTo(lastDate) != 0) {
                    sb.append("\t\t<h2>");
                    sb.append(curDate);
                    sb.append("</h2>");
                    sb.append(ENDL);
                    lastDate = curDate;
                }

                sb.append(formatEntry(uc, o, currentDate, false));
            }
        } catch (final Exception e1) {
            log.error("getTags: Exception is " + e1.getMessage() + '\n', e1);
        }
        return sb.toString();
    }

    /**
     * Format a blog entry in HTML
     *
     * @param uc          User Context
     * @param o           Entry to format
     * @param currentDate Date to format (of the entry)
     * @param single      Single blog entries are formatted differently
     * @return HTML formatted entry
     */
    protected String formatEntry(final UserContext uc, final Entry o, final Date currentDate, final boolean single) {
        final StringBuilder sb = new StringBuilder();
        final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");

        sb.append("\t\t<div class=\"ebody\">");
        sb.append(ENDL);

        if (single) {
            sb.append("<article><h3>");
            sb.append("<span class=\"time\">");
            sb.append(formatmytime.format(currentDate));
            sb.append("</span> - <span class=\"subject\"><a name=\"#e");
            sb.append(o.getId());
            sb.append("\">");
            sb.append(Xml.cleanString(o.getSubject()));
            sb.append("</a></span></h3> ");
            sb.append(ENDL);

            sb.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" \n");
            sb.append("xmlns:dc=\"http://purl.org/dc/elements/1.1/\" \n");
            sb.append("xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">\n");
            sb.append("\t<rdf:Description ");
            sb.append("rdf:about=\"");
            sb.append("http://www.justjournal.com/users/").append(o.getUser().getUsername()).append("/entry/");
            sb.append(o.getId());
            sb.append("#e");
            sb.append(o.getId());
            sb.append("\" dc:identifier=\"");
            sb.append("http://www.justjournal.com/users/").append(o.getUser().getUsername()).append("/entry/");
            sb.append(o.getId());
            sb.append("#e");
            sb.append(o.getId());
            sb.append("\" dc:title=\"");
            sb.append(Xml.cleanString(o.getSubject()));
            sb.append("\" ");
            sb.append("trackback:ping=\"http://www.justjournal.com/trackback?entryID=");
            sb.append(o.getId());
            sb.append("\" />\n");
            sb.append("</rdf:RDF>\n");
        } else {
            sb.append("<article><h3>");
            sb.append("<span class=\"time\">");
            sb.append(formatmytime.format(currentDate));
            sb.append("</span> - <span class=\"subject\">");
            sb.append("<a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
            sb.append(o.getId());
            sb.append("\" rel=\"bookmark\" title=\"");
            sb.append(Xml.cleanString(o.getSubject()));
            sb.append("\">");
            sb.append(Xml.cleanString(o.getSubject()));
            sb.append("</a></span></h3> ");
            sb.append(ENDL);
        }

        sb.append("\t\t\t<div class=\"ebody\">");
        sb.append(ENDL);

        /*
           autoformat controls whether new lines should be
           converted to br's.  If someone used html, we don't want autoformat!
           We handle Windows/UNIX with the \n case and Mac OS Classic with \r
         */
        if (o.getAutoFormat() != null && o.getAutoFormat() == PrefBool.Y) {

            if (o.getBody() != null) {
                sb.append("\t\t\t\t<p>");
                if (o.getBody().contains("\n"))
                    sb.append(StringUtil.replace(o.getBody(), '\n', "<br>"));
                else if (o.getBody().contains("\r"))
                    sb.append(StringUtil.replace(o.getBody(), '\r', "<br>"));
                else
                    // we do not have any "new lines" but it might be
                    // one long line.
                    sb.append(o.getBody());

                sb.append("</p>");
            }
        } else {
            sb.append(o.getBody());
        }

        sb.append(ENDL);
        sb.append("\t\t\t</div>");
        sb.append(ENDL);

        sb.append("\t\t\t<p>");

        if (o.getSecurity() == null || o.getSecurity().getId() == 0) {
            sb.append("<span class=\"security\">security: ");
            sb.append("<img src=\"/images/icon_private.gif\" alt=\"private\" /> ");
            sb.append("private");
            sb.append("</span><br />");
            sb.append(ENDL);
        } else if (o.getSecurity().getId() == 1) {
            sb.append("\t\t\t<span class=\"security\">security: ");
            sb.append("<img src=\"/images/icon_protected.gif\" alt=\"friends\" /> ");
            sb.append("friends");
            sb.append("</span><br />");
            sb.append(ENDL);
        }

        if (o.getLocation() != null && o.getLocation().getId() > 0) {
            sb.append("\t\t\t<span class=\"location\">location: ");
            sb.append(o.getLocation().getTitle());
            sb.append("</span><br />");
            sb.append(ENDL);
        }

        if (o.getMood() != null && o.getMood().getTitle().length() > 0 && o.getMood().getId() != 12) {
            final MoodThemeData emoto = emoticonDao.findByThemeIdAndMoodId(1, o.getMood().getId());

            if (emoto != null) {
                sb.append("\t\t\t<span class=\"mood\">mood: <img src=\"/images/emoticons/1/");
                sb.append(emoto.getFileName());
                sb.append("\" width=\"");
                sb.append(emoto.getWidth());
                sb.append("\" height=\"");
                sb.append(emoto.getHeight());
                sb.append("\" alt=\"");
                sb.append(o.getMood().getTitle());
                sb.append("\" /> ");
                sb.append(o.getMood().getTitle());
                sb.append("</span><br />");
                sb.append(ENDL);
            } else {
                log.error("Couldn't get mood theme data for " + o.getMood().getId());
            }
        }

        if (o.getMusic() != null && o.getMusic().length() > 0) {
            sb.append("\t\t\t<span class=\"music\">music: ");
            sb.append(Xml.cleanString(o.getMusic()));
            sb.append("</span><br />");
            sb.append(ENDL);
        }

        sb.append("\t\t\t</p>");
        sb.append(ENDL);

        final Collection<EntryTag> ob = o.getTags();
        if (! ob.isEmpty()) {
            sb.append("<p>tags:");
            for (final EntryTag tag : ob) {
                sb.append(" ");
                sb.append("<a href=\"/users/");
                sb.append(uc.getBlogUser().getUsername());
                sb.append("/tag/");
                sb.append(tag.getTag().getName());
                sb.append("\">");
                sb.append(tag.getTag().getName());
                sb.append("</a>");
            }
            sb.append("</p>");
            sb.append(ENDL);
        }

        sb.append("\t\t\t<div>");
        sb.append(ENDL);
        sb.append("\t\t\t\t<table width=\"100%\"  border=\"0\">");
        sb.append(ENDL);
        sb.append("\t\t\t\t\t<tr>");
        sb.append(ENDL);

        if (uc.isAuthBlog()) {
            sb.append("<td style=\"width: 30px\"><a title=\"Edit Entry\" href=\"/#!/entry/");
            sb.append(o.getId());
            sb.append("\"><i class=\"fa fa-pencil-square-o\"></i></a></td>");
            sb.append(ENDL);
            sb.append("<td style=\"width: 30px\"><a title=\"Delete Entry\" onclick=\"return deleteEntry(" + o.getId() + ");\"");
            sb.append("><i class=\"fa fa-trash-o\"></i></a>");
            sb.append("</td>");
            sb.append(ENDL);

            sb.append("<td style=\"width: 30px\"><a title=\"Add Favorite\" onclick=\"return addFavorite(");
            sb.append(o.getId());
            sb.append(")\"><i class=\"fa fa-heart\"></i></a></td>");
            sb.append(ENDL);
        }

        if (single) {
            sb.append("<td><div align=\"right\">");
            if (o.getSecurity() != null && o.getSecurity().getId() == 2) {
                // facebook
                sb.append("<div style=\"padding-right: 5px\" class=\"fb-share-button\" data-href=\"http://www.justjournal.com/users/").append(o.getUser().getUsername()).append("/entry/");
                sb.append(o.getId());
                sb.append("\" data-layout=\"button_count\">");
                sb.append("\t</div> ");

                // google+
                sb.append("<div class=\"g-plus\" data-action=\"share\"></div>");
            }

            sb.append("</div></td>");

        } else {

            sb.append("<td><div style=\"float: right\"><a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
            sb.append(o.getId());
            sb.append("\" title=\"Link to this entry\"><i class=\"fa fa-external-link\"></i></a> ");

            sb.append('(');

            switch (o.getComments().size()) {
                case 0:
                    break;
                case 1:
                    sb.append("<a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                    sb.append(o.getId());
                    sb.append("\" title=\"View Comment\">1 comment</a> | ");
                    break;
                default:
                    sb.append("<a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                    sb.append(o.getId());
                    sb.append("\" title=\"View Comments\">");
                    sb.append(o.getComments().size());
                    sb.append(" comments</a> | ");
            }

            sb.append("<a href=\"/#!/comment/");
            sb.append(o.getId());
            sb.append("\" title=\"Leave a comment on this entry\"><i class=\"fa fa-comment-o\"></i></a>)");
            sb.append("\t\t\t\t\t\t</div></td>");
            sb.append(ENDL);
        }

        sb.append("\t\t\t\t\t</tr>");
        sb.append(ENDL);
        sb.append("\t\t\t\t</table>");
        sb.append(ENDL);
        sb.append("\t\t\t</div>");
        sb.append(ENDL);

        if (single) {
            final List<Comment> comments = commentDao.findByEntryId(o.getId());

            sb.append("<div class=\"commentcount\">");
            sb.append(comments.size());
            sb.append(" comments</div>\n");

            sb.append("<div class=\"rightflt\">");
            sb.append("<a href=\"/#!/comment/").append(o.getId()).append("\" title=\"Add Comment\">Add Comment</a></div>\n");

            for (final Comment co : comments) {
                sb.append("<div class=\"comment\">\n");
                sb.append("<div class=\"chead\">\n");
                sb.append("<h3><span class=\"subject\">");
                sb.append(Xml.cleanString(co.getSubject()));
                sb.append("</span></h3>\n");
                sb.append("<img src=\"/static/images/userclass_16.png\" alt=\"user\"/>");
                sb.append("<a href=\"../users/");
                sb.append(co.getUser().getUsername());
                sb.append("\" title=\"");
                sb.append(co.getUser().getUsername());
                sb.append("\">");
                sb.append(co.getUser().getUsername());
                sb.append("</a>\n");

                sb.append("<br/><span class=\"time\">");
                sb.append(new DateTimeBean(co.getDate()).toPubDate());
                sb.append("</span>\n");

                if (uc.getAuthenticatedUser() != null &&
                        uc.getAuthenticatedUser().getUsername().equalsIgnoreCase(co.getUser().getUsername())) {
                    sb.append("<br/><span class=\"actions\">\n");
                    sb.append("<a href=\"/#!/comment/").append(o.getId()).append("/edit/");
                    sb.append(co.getId());
                    sb.append("\" title=\"Edit Comment\">");
                    sb.append("     <i class=\"fa fa-pencil-square-o\"></i>");
                    sb.append("</a>\n");

                    sb.append("<a onclick=\"deleteComment(");
                    sb.append(co.getId());
                    sb.append(")\" title=\"Delete Comment\">");
                    sb.append("<i class=\"fa fa-trash-o\"></i>");
                    sb.append("</a>\n");
                    sb.append("</span>\n");
                }
                sb.append("</div>\n");

                sb.append("<p>");
                sb.append(Xml.cleanString(o.getBody()));
                sb.append("</p>\n</div>\n");
            }
        }

        sb.append("\t\t</div></article>");
        sb.append(ENDL);

        return sb.toString();
    }
}
