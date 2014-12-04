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
import com.justjournal.core.Settings;
import com.justjournal.model.*;
import com.justjournal.repository.*;
import com.justjournal.rss.CachedHeadlineBean;
import com.justjournal.rss.Rss;
import com.justjournal.search.BaseSearch;
import com.justjournal.services.EntryService;
import com.justjournal.services.ServiceException;
import com.justjournal.utility.HTMLUtil;
import com.justjournal.utility.SQLHelper;
import com.justjournal.utility.StringUtil;
import com.justjournal.utility.Xml;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Journal viewer for JustJournal.
 *
 * @author Lucas Holt
 */
@Transactional
@Controller
@RequestMapping("/users")
public class UsersController {
    public static final int SEARCH_MAX_LENGTH = 20;
    public static final float FONT_10_POINT = 10.0F;
    // constants
    private static final char endl = '\n';
    private static final Logger log = Logger.getLogger(UsersController.class);

    @SuppressWarnings({"InstanceVariableOfConcreteClass"})
    private Settings settings = null;

    @Qualifier("commentRepository")
    @Autowired
    private CommentRepository commentDao = null;

    @Qualifier("entryRepository")
    @Autowired
    private EntryRepository entryDao = null;

    @Autowired
    private EntryService entryService = null;

    @Qualifier("moodThemeDataRepository")
    @Autowired
    private MoodThemeDataRepository emoticonDao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityDao securityDao;

    @Autowired
    private RssSubscriptionsDAO rssSubscriptionsDAO;

    public void setEntryService(EntryService entryService) {
        this.entryService = entryService;
    }

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @RequestMapping(value = "{username}", method = RequestMethod.GET, produces = "text/html")
    public String entries(@PathVariable("username") String username,
                          Pageable pageable,
                          Model model, HttpSession session, HttpServletResponse response) {
        UserContext userContext = getUserContext(username, session);

        if (userContext == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userContext.getBlogUser());

        if (userContext.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userContext.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userContext));
        model.addAttribute("archive", getArchive(userContext));
        model.addAttribute("taglist", getTagMini(userContext));

        model.addAttribute("pageable", pageable);

        model.addAttribute("entries", getEntries(userContext, pageable));
        return "users";
    }

    @RequestMapping(value = "{username}/entry/{id}", method = RequestMethod.GET, produces = "text/html")
    public String entry(@PathVariable("username") String username,
                        @PathVariable("id") int id,
                        Model model, HttpSession session, HttpServletResponse response) {

        UserContext userContext = getUserContext(username, session);

        if (userContext == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userContext.getBlogUser());

        if (userContext.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userContext.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userContext));
        model.addAttribute("archive", getArchive(userContext));
        model.addAttribute("taglist", getTagMini(userContext));

        model.addAttribute("entry", getSingleEntry(id, userContext));

        return "users";
    }

    @RequestMapping(value = "{username}/friends", method = RequestMethod.GET, produces = "text/html")
    public String friends(@PathVariable("username") String username, Model model, HttpSession session, HttpServletResponse response) {
        UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userc.getBlogUser());

        if (userc.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userc));
        model.addAttribute("archive", getArchive(userc));
        model.addAttribute("taglist", getTagMini(userc));

        try {
            model.addAttribute("friends", getFriends(userc));
        } catch (ServiceException se) {
            log.error(se.getMessage());
        }
        return "users";
    }

    @RequestMapping(value = "{username}/calendar", method = RequestMethod.GET, produces = "text/html")
    public String calendar(@PathVariable("username") String username, Model model, HttpSession session, HttpServletResponse response) {

        UserContext userContext = getUserContext(username, session);

        if (userContext == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userContext.getBlogUser());

        if (userContext.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userContext.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userContext));
        model.addAttribute("archive", getArchive(userContext));
        model.addAttribute("taglist", getTagMini(userContext));

        final Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);

        model.addAttribute("startYear", userContext.getBlogUser().getSince());
        model.addAttribute("currentYear", year);

        return "users";
    }

    @RequestMapping(value = "{username}/{year}", method = RequestMethod.GET, produces = "text/html")
    public String calendarYear(@PathVariable("username") String username, @PathVariable("year") int year, Model model, HttpSession session, HttpServletResponse response) {
        UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userc.getBlogUser());

        if (userc.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userc));
        model.addAttribute("archive", getArchive(userc));
        model.addAttribute("taglist", getTagMini(userc));

        model.addAttribute("calendar", getCalendar(year, userc));

        return "users";
    }

    @RequestMapping(value = "{username}/{year}/{month}", method = RequestMethod.GET, produces = "text/html")
    public String calendarMonth(@PathVariable("username") String username,
                                @PathVariable("year") int year,
                                @PathVariable("month") int month,
                                Model model, HttpSession session, HttpServletResponse response) {
        UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userc.getBlogUser());

        if (userc.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userc));
        model.addAttribute("archive", getArchive(userc));
        model.addAttribute("taglist", getTagMini(userc));

        model.addAttribute("calendar", getCalendarMonth(year, month, userc));

        return "users";
    }

    @RequestMapping(value = "{username}/{year}/{month}/{day}", method = RequestMethod.GET, produces = "text/html")
    public String calendarDay(@PathVariable("username") String username,
                              @PathVariable("year") int year,
                              @PathVariable("month") int month,
                              @PathVariable("day") int day, Model model, HttpServletResponse response, HttpSession session) {
        UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userc.getBlogUser());

        if (userc.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userc));
        model.addAttribute("archive", getArchive(userc));
        model.addAttribute("taglist", getTagMini(userc));

        model.addAttribute("calendar", getCalendarDay(year, month, day, userc));

        return "users";
    }

    @RequestMapping(value = "{username}/atom", method = RequestMethod.GET, produces = "text/xml; charset=UTF-8")
    public
    @ResponseBody
    String atom(@PathVariable("username") String username, HttpServletResponse response) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            if (user.getUserPref().getOwnerViewOnly() == PrefBool.Y) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return "";
            }

            return getAtom(user);
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "";
        }
    }

    @RequestMapping(value = "{username}/rss", method = RequestMethod.GET, produces = "application/rss+xml; charset=ISO-8859-1")
    public
    @ResponseBody
    String rss(@PathVariable("username") String username, HttpServletResponse response) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            if (user.getUserPref().getOwnerViewOnly() == PrefBool.Y) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return "";
            }

            return getRSS(user);
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "";
        }
    }

    @RequestMapping(value = "{username}/rsspics", method = RequestMethod.GET, produces = "application/rss+xml; charset=ISO-8859-1")
    public
    @ResponseBody
    String rssPictures(@PathVariable("username") String username, HttpServletResponse response) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            if (user.getUserPref().getOwnerViewOnly() == PrefBool.Y) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return "";
            }

            return getPicturesRSS(user);
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "";
        }
    }

    @RequestMapping(value = "{username}/pdf", method = RequestMethod.GET, produces = "application/pdf")
    public void pdf(@PathVariable("username") String username, HttpServletResponse response, HttpSession session) {
        User authUser = null;
        try {
            authUser = userRepository.findByUsername(Login.currentLoginName(session));
        } catch (Exception ignored) {

        }

        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            UserContext userc = new UserContext(user, authUser);
            if (user.getUserPref().getOwnerViewOnly() == PrefBool.N || userc.isAuthBlog()) {
                getPDF(response, userc);
            } else
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "{username}/rtf", method = RequestMethod.GET, produces = "application/rtf")
    public void rtf(@PathVariable("username") String username, HttpServletResponse response, HttpSession session) {
        User authUser = null;
        try {
            authUser = userRepository.findByUsername(Login.currentLoginName(session));
        } catch (Exception ignored) {

        }

        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            UserContext userc = new UserContext(user, authUser);
            if (user.getUserPref().getOwnerViewOnly() == PrefBool.N || userc.isAuthBlog())
                getRTF(response, userc);
            else
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(value = Transactional.TxType.REQUIRED)
    @RequestMapping(value = "{username}/pictures", method = RequestMethod.GET, produces = "text/html")
    public String pictures(@PathVariable("username") String username, Model model, HttpSession session, HttpServletResponse response) {
        UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userc.getBlogUser());

        if (userc.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userc));
        model.addAttribute("archive", getArchive(userc));
        model.addAttribute("taglist", getTagMini(userc));

        model.addAttribute("pictures", getImageList(userc));

        return "users";
    }

    @RequestMapping(value = "{username}/search", method = RequestMethod.GET, produces = "text/html")
    public String search(@PathVariable("username") String username,
                         @RequestParam("max") String max,
                         @RequestParam("bquery") String bquery,
                         Model model, HttpSession session, HttpServletResponse response) {
        UserContext userc = getUserContext(username, session);
        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userc.getBlogUser());

        if (userc.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userc));
        model.addAttribute("archive", getArchive(userc));
        model.addAttribute("taglist", getTagMini(userc));
        int maxr = SEARCH_MAX_LENGTH;

        if (max != null && max.length() > 0)
            try {
                maxr = Integer.parseInt(max);
            } catch (NumberFormatException exInt) {
                maxr = SEARCH_MAX_LENGTH;
                log.error(exInt.getMessage());
            }

        model.addAttribute("search", search(userc, maxr, bquery));

        return "users";
    }

    @RequestMapping(value = "{username}/subscriptions", method = RequestMethod.GET, produces = "text/html")
    public String subscriptions(@PathVariable("username") String username, Model model, HttpSession session, HttpServletResponse response) {
        UserContext userc = getUserContext(username, session);

        if (userc == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        model.addAttribute("authenticatedUsername", Login.currentLoginName(session));
        model.addAttribute("user", userc.getBlogUser());

        if (userc.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.Y && !userc.isAuthBlog()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }

        model.addAttribute("calendarMini", getCalendarMini(userc));
        model.addAttribute("archive", getArchive(userc));
        model.addAttribute("taglist", getTagMini(userc));

        model.addAttribute("subscriptions", getSubscriptions(userc));

        return "users";
    }

    @RequestMapping(value = "{username}/tag/{tag}", method = RequestMethod.GET, produces = "text/html")
    public String tag(@PathVariable("username") String username,
                      @PathVariable("tag") String tag,
                      Model model, HttpSession session, HttpServletResponse response) {

        User authUser = null;
        try {
            authUser = userRepository.findByUsername(Login.currentLoginName(session));
        } catch (Exception ignored) {

        }

        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            UserContext userc = new UserContext(user, authUser);

            model.addAttribute("username", user);
            model.addAttribute("authenticatedUsername", Login.currentLoginName(session));

            if (userc.getBlogUser().getUserPref().getOwnerViewOnly() == PrefBool.N || userc.isAuthBlog())
                model.addAttribute("tags", getTags(userc, tag));
            else
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return "users";
    }

    @Transactional(value = Transactional.TxType.REQUIRED)
    private UserContext getUserContext(String username, HttpSession session) {
        User authUser = null;
        try {
            authUser = userRepository.findByUsername(Login.currentLoginName(session));
        } catch (Exception ignored) {

        }

        try {
            User user = userRepository.findByUsername(username);
            if (user == null || user.getId() == 0) return null;

            return new UserContext(user, authUser);
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    private void getPDF(final HttpServletResponse response, final UserContext uc) {
        try {
            response.resetBuffer();
            response.setContentType("application/pdf");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // RFC 1806
            response.setHeader("Content-Disposition", "attachment; filename=" + uc.getBlogUser().getUsername() + ".pdf");

            final Document document = new Document();
            //    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, response.getOutputStream());    // baos
            formatRTFPDF(uc, document);
            document.close();

            //     response.setContentLength(baos.size()); /* required by IE */
            //   final OutputStream out = response.getOutputStream();
            //   baos.writeTo(out);
            //   out.flush();
        } catch (DocumentException e) {
            log.error("Users.getPDF() DocumentException:" + e.getMessage());
        } catch (IOException e1) {
            log.error("Users.getPDF() IOException:" + e1.getMessage());
        } catch (Exception e) {
            // user class caused this
            log.error("Users.getPDF():" + e.getMessage());
        }
    }

    private void getRTF(final HttpServletResponse response, final UserContext uc) {
        try {
            final Document document = new Document();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            RtfWriter2.getInstance(document, baos);
            formatRTFPDF(uc, document);
            document.close();

            response.setContentType("application/rtf");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // RFC 1806
            response.setHeader("Content-Disposition", "attachment; filename=" + uc.getBlogUser().getUsername() + ".rtf");
            response.setContentLength(baos.size()); /* required by IE */
            final ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
            out.close();
        } catch (DocumentException e) {
            log.error("Users.getPDF() DocumentException:" + e.getMessage());
        } catch (IOException e1) {
            log.error("Users.getPDF() IOException:" + e1.getMessage());
        } catch (Exception e) {
            // user class caused this
            log.error("Users.getPDF():" + e.getMessage());
        }
    }

    private void formatRTFPDF(final UserContext uc, final Document document)
            throws Exception {

        document.open();
        document.add(new Paragraph(""));
        Chunk chunk = new Chunk(uc.getBlogUser().getUserPref().getJournalName());
        chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_STROKE, 0.4f, new Color(0x00, 0x00, 0xFF));
        document.add(chunk);
        document.add(new Paragraph(new Date().toString(), new Font(Font.HELVETICA, FONT_10_POINT)));
        document.add(Chunk.NEWLINE);

        final List<Entry> entries;

        if (uc.isAuthBlog())
            entries = entryDao.findByUsername(uc.getBlogUser().getUsername());
        else
            entries = entryDao.findByUsernameAndSecurity(uc.getBlogUser().getUsername(), securityDao.findOne(2));

        // Format the current time.
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");
        final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");
        String lastDate = "";
        String curDate;

        for (Entry o : entries) {
            // Parse the previous string back into a Date.
            final ParsePosition pos = new ParsePosition(0);
            final Date currentDate = formatter.parse(o.getDate().toString(), pos);

            curDate = formatmydate.format(currentDate);

            if (curDate.compareTo(lastDate) != 0) {
                document.add(new Paragraph(curDate, new Font(Font.HELVETICA, 14.0F)));
                lastDate = curDate;
            }

            document.add(new Paragraph(formatmytime.format(currentDate), new Font(Font.HELVETICA, 12.0F)));
            document.add(Chunk.NEWLINE);
            chunk = new Chunk(o.getSubject());
            chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL, 0.3F, new Color(0x00, 0x00, 0x00));
            document.add(chunk);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph(HTMLUtil.textFromHTML(o.getBody()), new Font(Font.TIMES_ROMAN, 11.0F)));
            document.add(Chunk.NEWLINE);

            if (o.getSecurity().getId() == 0)
                document.add(new Paragraph("Security: " + "Private", new Font(Font.HELVETICA, 10.0F)));
            else if (o.getSecurity().getId() == 1)
                document.add(new Paragraph("Security: " + "Friends", new Font(Font.HELVETICA, 10.0F)));
            else
                document.add(new Paragraph("Security: " + "Public", new Font(Font.HELVETICA, 10.0F)));

            document.add(new Chunk("Location: " + o.getLocation().getTitle()));
            document.add(Chunk.NEWLINE);
            document.add(new Chunk("Mood: " + o.getMood().getTitle()));
            document.add(Chunk.NEWLINE);
            document.add(new Chunk("Music: " + o.getMusic()));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
        }
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    private String getImageList(final UserContext uc) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Pictures</h2>");
        sb.append(endl);
        sb.append("<ul style=\"list-style-image: url('/images/pictureframe.png'); list-style-type: circle;\">");


        ResultSet rs = null;
        String imageTitle;
        final String sqlStmt = "SELECT id, title FROM user_images WHERE owner='" + uc.getBlogUser().getId() + "' ORDER BY title;";

        try {
            rs = SQLHelper.executeResultSet(sqlStmt);

            while (rs.next()) {
                imageTitle = rs.getString("title");

                sb.append("\t<li>");
                sb.append("<a href=\"/AlbumImage?id=");
                sb.append(rs.getString("id"));
                sb.append("\" rel=\"lightbox\" title=\"");
                sb.append(imageTitle);
                sb.append("\">");
                sb.append(imageTitle);
                sb.append("</a>");
                sb.append("</li>");
                sb.append(endl);
            }

            rs.close();

        } catch (Exception e1) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }
        sb.append("</ul>\n");
        sb.append("<p>Subscribe to pictures ");
        sb.append("<a href=\"/users/");
        sb.append(uc.getBlogUser().getUsername());
        sb.append("/rsspics\">feed</a>.</p>");

        return sb.toString();
    }

    private String getSubscriptions(final UserContext uc) {
        StringBuilder sb = new StringBuilder();

        sb.append("<h2>RSS Reader</h2>");
        sb.append(endl);
        sb.append("<p><a href=\"javascript:sweeptoggle('contract')\">Contract All</a> | <a href=\"javascript:sweeptoggle('expand')\">Expand All</a></p>");
        sb.append(endl);

        final CachedHeadlineBean hb = new CachedHeadlineBean();

        try {
            final Collection<RssSubscription> rssfeeds = rssSubscriptionsDAO.findByUser(uc.getBlogUser());

            /* Iterator */
            RssSubscription o;
            final Iterator itr = rssfeeds.iterator();
            for (int i = 0, n = rssfeeds.size(); i < n; i++) {
                o = (RssSubscription) itr.next();

                sb.append(hb.parse(o.getUri()));
                sb.append(endl);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return sb.toString();
    }

    private String getSingleEntry(final int singleEntryId, final UserContext uc) {

        StringBuffer sb = new StringBuffer();
        Entry o;

        if (singleEntryId < 1) {
            ErrorPage.Display("Invalid Entry Id", "The entry id was invalid for the journal entry you tried to get.", sb);
        } else {
            try {
                o = entryDao.findOne(singleEntryId);
                if (uc.isAuthBlog()) {
                    if (o.getUser().getId() != uc.authenticatedUser.getId())
                        o = null;

                    log.debug("getSingleEntry: User is logged in.");
                } else {
                    if (o.getSecurity().getId() != 2)
                        o = null;
                    log.debug("getSingleEntry: User is not logged in.");
                }

                log.trace("getSingleEntry: Begin reading record.");

                if (o != null && o.getId() > 0) {
                    final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");

                    String curDate = formatmydate.format(o.getDate());

                    sb.append("<h2>");
                    sb.append(curDate);
                    sb.append("</h2>");
                    sb.append(endl);

                    sb.append(formatEntry(uc, o, o.getDate(), true));
                }
            } catch (Exception e1) {
                log.error("getSingleEntry: " + e1.getMessage() + '\n' + e1.toString());

                ErrorPage.Display("Error",
                        "Unable to retrieve journal entry from data store.",
                        sb);
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    private String search(final UserContext uc, final int maxresults, final String bquery) {
        StringBuilder sb = new StringBuilder();
        ResultSet brs = null;
        String sql;

        if (uc.isAuthBlog())
            sql = "SELECT entry.subject AS subject, entry.body AS body, entry.date AS date, entry.id AS id, user.username AS username from entry, user, user_pref WHERE entry.uid = user.id AND entry.uid=user_pref.id AND user.username='" + uc.getBlogUser().getUsername() + "' AND ";
        else
            sql = "SELECT entry.subject AS subject, entry.body AS body, entry.date AS date, entry.id AS id, user.username AS username from entry, user, user_pref WHERE entry.uid = user.id AND entry.uid=user_pref.id AND user_pref.owner_view_only = 'N' AND entry.security=2 AND user.username='" + uc.getBlogUser().getUsername() + "' AND ";

        if (bquery != null && bquery.length() > 0) {
            try {
                final BaseSearch b = new BaseSearch();

                if (log.isDebugEnabled()) {
                    log.debug("Search base is: " + sql);
                    log.debug("Max results are: " + maxresults);
                    log.debug("Search query is: " + bquery);
                }

                b.setBaseQuery(sql);
                b.setFields("subject body");
                b.setMaxResults(maxresults);
                b.setSortAscending("date");
                brs = b.search(bquery);

                sb.append("<h2><img src=\"/images/icon_search.gif\" alt=\"Search Blog\" style=\"float: left;\" /> Blog Search</h2>");
                sb.append(endl);

                if (brs == null) {
                    sb.append("<p>No items were found matching your search criteria.</p>");
                    sb.append(endl);
                } else {

                    while (brs.next()) {

                        // Format the current time.
                        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");
                        final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");
                        String curDate;

                        // Parse the previous string back into a Date.
                        final ParsePosition pos = new ParsePosition(0);
                        final Date currentDate = formatter.parse(brs.getString("date"), pos);

                        curDate = formatmydate.format(currentDate);

                        sb.append("<h2>");
                        sb.append(curDate);
                        sb.append("</h2>");
                        sb.append(endl);


                        sb.append("<div class=\"ebody\">");
                        sb.append(endl);

                        sb.append("<h3>");
                        sb.append("<span class=\"time\">");
                        sb.append(formatmytime.format(currentDate));
                        sb.append("</span> - <span class=\"subject\"><a href=\"/users/").append(brs.getString("username")).append("/entry/").append(brs.getString("id")).append("\">");
                        sb.append(Xml.cleanString(brs.getString("subject")));
                        sb.append("</a></span></h3> ");
                        sb.append(endl);

                        sb.append("<div class=\"ebody\">");
                        sb.append(endl);
                        sb.append(brs.getString("body"));
                        sb.append(endl);
                        sb.append("</div>");
                        sb.append(endl);
                    }

                    brs.close();
                }

            } catch (Exception e1) {
                log.error("Could not close database resultset on first attempt: " + e1.getMessage());
                if (brs != null) {
                    try {
                        brs.close();
                    } catch (Exception e) {
                        log.error("Could not close database resultset: " + e.getMessage());
                    }
                }
            }
        }
        return sb.toString();
    }

    private String getEntries(final UserContext uc, Pageable pageable) {
        StringBuffer sb = new StringBuffer();
        Page<Entry> entries;

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

            for (Entry o : entries) {
                // Parse the previous string back into a Date.
                final ParsePosition pos = new ParsePosition(0);
                final Date currentDate = formatter.parse(new DateTimeBean(o.getDate()).toString(), pos); // TODO: seems inefficient

                curDate = formatmydate.format(currentDate);

                if (curDate.compareTo(lastDate) != 0) {
                    sb.append("\t\t<h2>");
                    sb.append(curDate);
                    sb.append("</h2>");
                    sb.append(endl);
                    lastDate = curDate;
                }

                sb.append(formatEntry(uc, o, currentDate, false));
            }
        } catch (Exception e1) {
            ErrorPage.Display("Error",
                    "Unable to retrieve journal entries from data store.",
                    sb);

            log.error("getEntries: Exception is " + e1.getMessage() + '\n' + e1.toString());
        }
        return sb.toString();
    }

    /**
     * Displays friends entries for a particular user.
     *
     * @param uc The UserContext we are working on including blog owner, authenticated user, and sb to write
     */
    private String getFriends(final UserContext uc) throws ServiceException {

        StringBuffer sb = new StringBuffer();
        final Collection entries;

  /*      if (uc.getAuthenticatedUser() != null)
            entries = entryDao.viewFriends(uc.getBlogUser().getUserId(), uc.getAuthenticatedUser().getUserId());
        else
            entries = entryDao.viewFriends(uc.getBlogUser().getUserId(), 0);
    */
        entries = entryService.getFriendsEntries(uc.getBlogUser().getUsername());
        sb.append("<h2>Friends</h2>");
        sb.append(endl);

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
                    sb.append(endl);
                    lastDate = curDate;
                }

                sb.append("<div class=\"ebody\">");
                sb.append(endl);

                // final User p = o.getUser();
                //  if (p.showAvatar()) {   TODO: avatar?
                sb.append("<img alt=\"avatar\" style=\"float: right\" src=\"/image?id=");
                sb.append(o.getUser().getId());
                sb.append("\"/>");
                sb.append(endl);
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
                sb.append(endl);

                sb.append("<div class=\"ebody\">");
                sb.append(endl);

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

                sb.append(endl);
                sb.append("</div>");
                sb.append(endl);

                sb.append("<p>");

                if (o.getSecurity().getId() == 0) {
                    sb.append("<span class=\"security\">security: ");
                    sb.append("<img src=\"/img/icon_private.gif\" alt=\"private\" /> ");
                    sb.append("private");
                    sb.append("</span><br />");
                    sb.append(endl);
                } else if (o.getSecurity().getId() == 1) {
                    sb.append("<span class=\"security\">security: ");
                    sb.append("<img src=\"/img/icon_protected.gif\" alt=\"friends\" /> ");
                    sb.append("friends");
                    sb.append("</span><br />");
                    sb.append(endl);
                }

                if (o.getLocation().getId() > 0) {
                    sb.append("<span class=\"location\">location: ");
                    sb.append(o.getLocation().getTitle());
                    sb.append("</span><br />");
                    sb.append(endl);
                }

                if (o.getMood().getTitle().length() > 0 && o.getMood().getId() != 12) {
                    final MoodThemeData emoto = emoticonDao.findByThemeIdAndMoodId(1, o.getMood().getId());

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
                    sb.append(endl);
                }

                if (o.getMusic().length() > 0) {
                    sb.append("<span class=\"music\">music: ");
                    sb.append(Xml.cleanString(o.getMusic()));
                    sb.append("</span><br>");
                    sb.append(endl);
                }

                sb.append("</p>");
                sb.append(endl);

                sb.append("<p>tags:");
                for (final EntryTag s : o.getTags()) {
                    sb.append(" ");
                    sb.append(s.getTag().getName());
                }
                sb.append("</p>");
                sb.append(endl);

                sb.append("<div>");
                sb.append(endl);
                sb.append("<table width=\"100%\"  border=\"0\">");
                sb.append(endl);
                sb.append("<tr>");
                sb.append(endl);

                if (uc.getAuthenticatedUser() != null && uc.getAuthenticatedUser().getId() == o.getUser().getId()) {
                    sb.append("<td width=\"30\"><a title=\"Edit Entry\" href=\"/#/entry/").append(o.getId());
                    sb.append("\"><i class=\"fa fa-pencil-square-o\"></i></a></td>");
                    sb.append(endl);
                    sb.append("<td width=\"30\"><a title=\"Delete Entry\" onclick=\"return deleteEntry(" + o.getId() + ")\"");
                    sb.append("\"><i class=\"fa fa-trash-o\"></i></a>");
                    sb.append("</td>");
                    sb.append(endl);

                    sb.append("<td width=\"30\"><a title=\"Add Favorite\" href=\"/favorite/add.h?entryId=");
                    sb.append(o.getId());
                    sb.append("\"><i class=\"fa fa-heart\"></i></a></td>");
                    sb.append(endl);
                } else if (uc.getAuthenticatedUser() != null) {
                    sb.append("<td width=\"30\"><a title=\"Add Favorite\" href=\"/favorite/add.h?entryId=");
                    sb.append(o.getId());
                    sb.append("\"><i class=\"fa fa-heart\"></i></a></td>");
                    sb.append(endl);
                }

                sb.append("<td><div style=\"float: right\"><a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
                sb.append(o.getId());
                sb.append("\" title=\"Link to this entry\">link</a> ");
                sb.append('(');

                switch (o.getComments().size()) {
                    case 0:
                        break;
                    case 1:
                        sb.append("<a href=\"/comment/index.jsp?id=");
                        sb.append(o.getId());
                        sb.append("\" title=\"View Comment\">1 comment</a> | ");
                        break;
                    default:
                        sb.append("<a href=\"/comment/index.jsp?id=");
                        sb.append(o.getId());
                        sb.append("\" title=\"View Comments\">");
                        sb.append(o.getComments().size());
                        sb.append(" comments</a> | ");
                }

                sb.append("<a href=\"/comment/add.jsp?id=");
                sb.append(o.getId());
                sb.append("\" title=\"Leave a comment on this entry\">comment on this</a>)");

                sb.append("</div></td>");
                sb.append(endl);
                sb.append("</tr>");
                sb.append(endl);
                sb.append("</table>");
                sb.append(endl);
                sb.append("</div>");
                sb.append(endl);

                sb.append("</div>");
                sb.append(endl);
            }

        } catch (Exception e1) {
            log.error(e1.getMessage());
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
        StringBuffer sb = new StringBuffer();
        final GregorianCalendar calendar = new GregorianCalendar();
        int yearNow = calendar.get(Calendar.YEAR);

        // print out header
        sb.append("<h2>Calendar: ");
        sb.append(year);
        sb.append("</h2>");
        sb.append(endl);

        sb.append("<p>The calendar lists months with journal entries.</p>");
        sb.append(endl);

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
        sb.append(endl);
        // END: YEARS

        try {

            Collection<Entry> entries;
            if (uc.isAuthBlog())
                entries = entryDao.findByUsernameAndYear(uc.getBlogUser().getUsername(), year);
            else
                entries = entryDao.findByUsernameAndYearAndSecurity(uc.getBlogUser().getUsername(), year, securityDao.findOne(2));

            if (entries == null || entries.size() == 0) {
                sb.append("<p>Calendar data not available.</p>");
                sb.append(endl);
            } else {
                // we have calendar data!
                final Cal mycal = new Cal(entries);
                sb.append(mycal.render());
            }

        } catch (Exception e1) {
            ErrorPage.Display(" Error",
                    "An error has occured rendering calendar.",
                    sb);
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
        StringBuffer sb = new StringBuffer();

        sb.append("<h2>Calendar: ");
        sb.append(month);
        sb.append('/');
        sb.append(year);
        sb.append("</h2>");
        sb.append(endl);

        sb.append("<p>This page lists all of the journal entries for the month.</p>");
        sb.append(endl);

        try {
            Collection<Entry> entries;
            if (uc.isAuthBlog())
                entries = entryDao.findByUsernameAndYearAndMonth(uc.getBlogUser().getUsername(), year, month);
            else
                entries = entryDao.findByUsernameAndYearAndMonthAndSecurity(uc.getBlogUser().getUsername(), year, month, securityDao.findOne(2));

            if (entries.size() == 0) {
                sb.append("<p>Calendar data not available.</p>");
                sb.append(endl);
            } else {

                final SimpleDateFormat formatmydate = new SimpleDateFormat("dd");
                final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");

                String curDate;
                String lastDate = "";

                for (Entry Entry : entries) {

                    Date currentDate = Entry.getDate();
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
                    sb.append(endl);
                }
            }

        } catch (Exception e1) {
            ErrorPage.Display(" Error",
                    "An error has occured rendering calendar.",
                    sb);
        }
        return sb.toString();
    }

    /**
     * Print a mini calendar for the current month with blog entries counts for given days in HTML.
     *
     * @param uc User Context
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Transactional(value = Transactional.TxType.REQUIRED)
    private String getCalendarMini(UserContext uc) {
        StringBuilder sb = new StringBuilder();
        try {
            final Calendar cal = new GregorianCalendar(TimeZone.getDefault());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1; // zero based

            Collection<Entry> entries;
            if (uc.isAuthBlog())
                entries = entryDao.findByUsernameAndYearAndMonth(uc.getBlogUser().getUsername(), year, month);
            else
                entries = entryDao.findByUsernameAndYearAndMonthAndSecurity(uc.getBlogUser().getUsername(), year, month, securityDao.findOne(2));


            if (entries.size() == 0) {
                sb.append("\t<!-- could not render calendar -->");
                sb.append(endl);
            } else {
                final Cal mycal = new Cal(entries);
                mycal.setBaseUrl("/users/" + uc.getBlogUser().getUsername() + '/');
                sb.append(mycal.renderMini());
            }
        } catch (Exception ex) {
            log.debug(ex);
        }
        return sb.toString();
    }

    /**
     * Print a list of tags in HTML that the blog owner is using.
     *
     * @param uc User Context
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Transactional(value = Transactional.TxType.REQUIRED)
    private String getTagMini(final UserContext uc) {
        assert (uc != null);
        assert (uc.getBlogUser() != null);
        assert (entryService != null);

        StringBuilder sb = new StringBuilder();
        Tag tag;
        Collection<Tag> tags = null;
        try {
            tags = entryService.getEntryTags(uc.getBlogUser().getUsername());
        } catch (ServiceException se) {
            log.error(se.getMessage());
            tags = Collections.emptyList();
        }
        int largest = 0;
        int smallest = 10;
        int cutSmall;
        int cutLarge;

        for (final Tag tag1 : tags) {
            tag = tag1;
            if (tag.getCount() > largest)
                largest = tag.getCount();

            if (tag.getCount() < smallest)
                smallest = tag.getCount();
        }

        cutSmall = largest / 3;
        cutLarge = cutSmall * 2;

        sb.append("\t<div class=\"menuentity\" id=\"usertags\" style=\"padding-top: 10px;\">\n\t\t<strong style=\"text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;\"><i class=\"fa fa-tags\"></i> Tags</strong>\n\t\t<p style=\"padding-left: 0; margin-left: 0;\">\n");
        for (final Tag tag1 : tags) {
            tag = tag1;
            sb.append("<a href=\"/users/");
            sb.append(uc.getBlogUser().getUsername());
            sb.append("/tag/");
            sb.append(tag.getName());
            sb.append("\" class=\"");
            if (tag.getCount() > cutLarge)
                sb.append("TagCloudLarge");
            else if (tag.getCount() < cutSmall)
                sb.append("TagCloudSmall");
            else
                sb.append("TagCloudMedium");
            sb.append("\">");
            sb.append(tag.getName());
            sb.append("</a>");
            sb.append(endl);
        }
        sb.append("\t\t</p>\n\t</div>");
        sb.append(endl);

        return sb.toString();
    }

    /**
     * Print a short list of recent blog entries in HTML
     *
     * @param uc User Context
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    @Transactional(value = Transactional.TxType.REQUIRED)
    private String getUserRecentEntries(final UserContext uc) {
        StringBuilder sb = new StringBuilder();

        try {
            List<RecentEntry> entries;

            if (uc.isAuthBlog()) {
                entries = entryService.getRecentEntries(uc.getBlogUser().getUsername());
            } else {
                entries = entryService.getRecentEntriesPublic(uc.getBlogUser().getUsername());
            }

            sb.append("\t<div class=\"menuentity\" id=\"userRecentEntries\">\n<strong style=\"text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;\">Recent Entries</strong>\n");
            sb.append("\t\t<ul class=\"list-group\">");
            sb.append(endl);

            for (RecentEntry o : entries) {
                sb.append("\t\t\t<li class=\"list-group-item\"><a href=\"/users/");
                sb.append(uc.getBlogUser().getUsername());
                sb.append("/entry/");
                sb.append(o.getId());
                sb.append("\" title=\"");
                sb.append(o.getSubject());
                sb.append("\">");
                sb.append(o.getSubject());
                sb.append("</a></li>");
                sb.append(endl);
            }

            sb.append("\t\t</ul>");
            sb.append(endl);
            sb.append("\t</div>");
            sb.append(endl);
        } catch (Exception e) {
            log.error(e.getMessage());
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
    private String getCalendarDay(final int year,
                                  final int month,
                                  final int day,
                                  final UserContext uc) {

        StringBuffer sb = new StringBuffer();

        // sb.append("<h2>Calendar: " + day + "/" + month + "/" + year + "</h2>" );

        sb.append("<p>Lists all of the journal entries for the day.</p>");
        sb.append(endl);

        try {

            final Collection<Entry> entries;
            if (uc.isAuthBlog())
                entries = entryDao.findByUsernameAndYearAndMonthAndDay(uc.getBlogUser().getUsername(), year, month, day);
            else
                entries = entryDao.findByUsernameAndYearAndMonthAndDayAndSecurity(uc.getBlogUser().getUsername(), year, month, day, securityDao.findOne(2));

            if (entries == null || entries.size() == 0) {
                sb.append("<p>Calendar data not available.</p>");
                sb.append(endl);
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
                        sb.append(endl);
                        lastDate = curDate;
                    }

                    sb.append(formatEntry(uc, o, currentDate, false));
                }
            }

        } catch (Exception e1) {
            ErrorPage.Display(" Error",
                    "An error has occured rendering calendar.",
                    sb);
        }

        return sb.toString();
    }

    /**
     * Print a list of months and years that users have blogged in as a history breadcrumb to access the calendar list
     * of blog entries in HTML.
     *
     * @param uc User Context
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    private String getArchive(final UserContext uc) {
        final GregorianCalendar calendarg = new GregorianCalendar();
        int yearNow = calendarg.get(Calendar.YEAR);
        StringBuilder sb = new StringBuilder();

        // BEGIN: YEARS
        sb.append("\t<div class=\"menuentity\" id=\"archive\" style=\"padding-top: 10px;\"><strong style=\"text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;\">Archive</strong><ul class=\"list-group\">");

        for (int i = yearNow; i >= uc.getBlogUser().getSince(); i--) {

            sb.append("<li class=\"list-group-item\"><a href=\"/users/");
            sb.append(uc.getBlogUser().getUsername());
            sb.append('/');
            sb.append(i);
            sb.append("\">");
            sb.append(i);
            sb.append(" (");
            try {
                sb.append(entryDao.calendarCount(i, uc.getBlogUser().getUsername()));
            } catch (Exception e) {
                log.error("getArchive: could not fetch count for " + uc.getBlogUser().getUsername() + ": " + i + e.getMessage());
                sb.append("0");
            }
            sb.append(")</a></li> ");

            // just in case!
            if (i == 2002)
                break;
        }

        sb.append("</ul>");
        sb.append(endl);
        sb.append("</div>");
        sb.append(endl);
        // END: YEARS

        return sb.toString();
    }

    /**
     * Handles requests for syndication content (RSS). Only returns public journal entries for the specified user.
     *
     * @param user
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    private String getRSS(final User user) {
        Rss rss = new Rss();

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

        Pageable page = new PageRequest(0, 15);
        rss.populate(entryDao.findByUserAndSecurityOrderByDateDesc(user, securityDao.findOne(2), page).getContent());
        return rss.toXml();
    }

    /**
     * Handles requests for syndication content (Atom). Only returns public journal entries for the specified user.
     *
     * @param user blog user
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    private String getAtom(final User user) {

        AtomFeed atom = new AtomFeed();

        final GregorianCalendar calendarg = new GregorianCalendar();
        calendarg.setTime(new Date());

        atom.setUserName(user.getUsername());
        atom.setAlternateLink("http://www.justjournal.com/users/" + user.getUsername());
        atom.setAuthorName(user.getFirstName());
        atom.setUpdated(calendarg.toString());
        atom.setTitle(user.getUserPref().getJournalName());
        atom.setId("http://www.justjournal.com/users/" + user.getUsername() + "/atom");
        atom.setSelfLink("/users/" + user.getUsername() + "/atom");
        Pageable page = new PageRequest(0, 15);
        atom.populate(entryDao.findByUserAndSecurityOrderByDateDesc(user, securityDao.findOne(2), page).getContent());
        return (atom.toXml());
    }

    /**
     * List the pictures associated with a blog in RSS.  This should be compatible with iPhoto.
     *
     * @param user blog user
     */
    @Transactional(value = Transactional.TxType.REQUIRED)
    private String getPicturesRSS(final User user) {

        final Rss rss = new Rss();

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
        return (rss.toXml());
    }

    /* TODO: finish this */
    @Transactional(value = Transactional.TxType.REQUIRED)
    private String getTags(final UserContext uc, String tag) {
        final StringBuilder sb = new StringBuilder();
        final Collection entries;

        try {
            if (uc.isAuthBlog()) {
                entries = entryDao.findByUsername(uc.getBlogUser().getUsername());
            } else {
                entries = entryDao.findByUsernameAndSecurity(uc.getBlogUser().getUsername(), securityDao.findOne(2));
            }

            // Format the current time.
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");

            String lastDate = "";
            String curDate;

            if (log.isDebugEnabled())
                log.debug("getTags: Begin Iteration of records.");

            /* Iterator */
            Entry o;
            final Iterator itr = entries.iterator();

            for (int i = 0, n = entries.size(); i < n; i++) {
                o = (Entry) itr.next();

                // Parse the previous string back into a Date.
                final ParsePosition pos = new ParsePosition(0);
                final Date currentDate = formatter.parse(new DateTimeBean(o.getDate()).toString(), pos);

                curDate = formatmydate.format(currentDate);

                Collection entryTags = o.getTags();

                if (entryTags.contains(tag.toLowerCase())) {
                    if (curDate.compareTo(lastDate) != 0) {
                        sb.append("\t\t<h2>");
                        sb.append(curDate);
                        sb.append("</h2>");
                        sb.append(endl);
                        lastDate = curDate;
                    }

                    sb.append(formatEntry(uc, o, currentDate, false));
                }
            }
        } catch (Exception e1) {
            if (log.isDebugEnabled())
                log.debug("getTags: Exception is " + e1.getMessage() + '\n' + e1.toString());
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
    protected String formatEntry(final UserContext uc, final Entry o, final Date currentDate, boolean single) {
        final StringBuilder sb = new StringBuilder();
        final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");

        sb.append("\t\t<div class=\"ebody\">");
        sb.append(endl);

        if (single) {
            sb.append("<article><h3>");
            sb.append("<span class=\"time\">");
            sb.append(formatmytime.format(currentDate));
            sb.append("</span> - <span class=\"subject\"><a name=\"#e");
            sb.append(o.getId());
            sb.append("\">");
            sb.append(Xml.cleanString(o.getSubject()));
            sb.append("</a></span></h3> ");
            sb.append(endl);

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
            sb.append(endl);
        }

        sb.append("\t\t\t<div class=\"ebody\">");
        sb.append(endl);


        /*
           autoformat controls whether new lines should be
           converted to br's.  If someone used html, we don't want autoformat!
           We handle Windows/UNIX with the \n case and Mac OS Classic with \r
         */
        if (o.getAutoFormat() == PrefBool.Y) {

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
        } else {
            sb.append(o.getBody());
        }

        sb.append(endl);
        sb.append("\t\t\t</div>");
        sb.append(endl);

        sb.append("\t\t\t<p>");

        if (o.getSecurity().getId() == 0) {
            sb.append("<span class=\"security\">security: ");
            sb.append("<img src=\"/images/icon_private.gif\" alt=\"private\" /> ");
            sb.append("private");
            sb.append("</span><br />");
            sb.append(endl);
        } else if (o.getSecurity().getId() == 1) {
            sb.append("\t\t\t<span class=\"security\">security: ");
            sb.append("<img src=\"/images/icon_protected.gif\" alt=\"friends\" /> ");
            sb.append("friends");
            sb.append("</span><br />");
            sb.append(endl);
        }

        if (o.getLocation().getId() > 0) {
            sb.append("\t\t\t<span class=\"location\">location: ");
            sb.append(o.getLocation().getTitle());
            sb.append("</span><br />");
            sb.append(endl);
        }

        if (o.getMood().getTitle().length() > 0 && o.getMood().getId() != 12) {
            final MoodThemeData emoto = emoticonDao.findByThemeIdAndMoodId(1, o.getMood().getId());

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
            sb.append(endl);
        }

        if (o.getMusic().length() > 0) {
            sb.append("\t\t\t<span class=\"music\">music: ");
            sb.append(Xml.cleanString(o.getMusic()));
            sb.append("</span><br />");
            sb.append(endl);
        }

        sb.append("\t\t\t</p>");
        sb.append(endl);

        Collection<EntryTag> ob = o.getTags();
        if (ob.size() > 0) {
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
            sb.append(endl);
        }

        sb.append("\t\t\t<div>");
        sb.append(endl);
        sb.append("\t\t\t\t<table width=\"100%\"  border=\"0\">");
        sb.append(endl);
        sb.append("\t\t\t\t\t<tr>");
        sb.append(endl);

        if (uc.isAuthBlog()) {
            sb.append("<td style=\"width: 30px\"><a title=\"Edit Entry\" href=\"/#!/entry/");
            sb.append(o.getId());
            sb.append("\"><i class=\"fa fa-pencil-square-o\"></i></a></td>");
            sb.append(endl);
            sb.append("<td style=\"width: 30px\"><a title=\"Delete Entry\" onclick=\"return confirmDelete()\"; href=\"/entry/delete.h?entryId=");
            sb.append(o.getId());
            sb.append("\"><i class=\"fa fa-trash-o\"></i></a>");
            sb.append("</td>");
            sb.append(endl);

            sb.append("<td style=\"width: 30px\"><a title=\"Add Favorite\" href=\"/favorite/add.h?entryId=");
            sb.append(o.getId());
            sb.append("\"><i class=\"fa fa-heart\"></i></a></td>");
            sb.append(endl);
        }

        if (single) {
            sb.append("<td><div align=\"right\">");
            if (o.getSecurity().getId() == 2) {
                sb.append("<iframe src=\"https://www.facebook.com/plugins/like.php?href=http://www.justjournal.com/users/").append(o.getUser().getUsername()).append("/entry/");
                sb.append(o.getId());
                sb.append("\" scrolling=\"no\" frameborder=\"0\" style=\"border:none; width:450px; height:80px\"></iframe>");

            }
            sb.append("</div></td>");

        } else {

            sb.append("<td><div style=\"float: right\"><a href=\"/users/").append(o.getUser().getUsername()).append("/entry/");
            sb.append(o.getId());
            sb.append("\" title=\"Link to this entry\"><i class=\"fa fa-external-link\"></i></a> ");

            sb.append('(');

            int commentCount = o.getComments().size();
            switch (commentCount) {
                case 0:
                    break;
                case 1:
                    sb.append("<a href=\"/comment/index.jsp?id=");
                    sb.append(o.getId());
                    sb.append("\" title=\"View Comment\">1 comment</a> | ");
                    break;
                default:
                    sb.append("<a href=\"/comment/index.jsp?id=");
                    sb.append(o.getId());
                    sb.append("\" title=\"View Comments\">");
                    sb.append(commentCount);
                    sb.append(" comments</a> | ");
            }

            sb.append("<a href=\"/comment/add.jsp?id=");
            sb.append(o.getId());
            sb.append("\" title=\"Leave a comment on this entry\"><i class=\"fa fa-comment-o\"></i></a>)");
            sb.append("\t\t\t\t\t\t</div></td>");
            sb.append(endl);
        }

        sb.append("\t\t\t\t\t</tr>");
        sb.append(endl);
        sb.append("\t\t\t\t</table>");
        sb.append(endl);
        sb.append("\t\t\t</div>");
        sb.append(endl);

        if (single) {
            List<Comment> comments = commentDao.findByEntryId(o.getId());

            sb.append("<div class=\"commentcount\">");
            sb.append(comments.size());
            sb.append(" comments</div>\n");

            sb.append("<div class=\"rightflt\">");
            sb.append("<a href=\"add.jsp?id=").append(o.getId()).append("\" title=\"Add Comment\">Add Comment</a></div>\n");

            for (Comment co : comments) {
                sb.append("<div class=\"comment\">\n");
                sb.append("<div class=\"chead\">\n");
                sb.append("<h3><span class=\"subject\">");
                sb.append(Xml.cleanString(co.getSubject()));
                sb.append("</span></h3>\n");
                sb.append("<img src=\"../images/userclass_16.png\" alt=\"user\"/>");
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


                if (uc.getAuthenticatedUser().getUsername().equalsIgnoreCase(co.getUser().getUsername())) {
                    sb.append("<br/><span class=\"actions\">\n");
                    sb.append("<a href=\"edit.h?commentId=");
                    sb.append(co.getId());
                    sb.append("\" title=\"Edit Comment\">");
                    sb.append("     <i class=\"fa fa-pencil-square-o\"></i>");
                    sb.append("</a>\n");

                    sb.append("<a href=\"delete.h?commentId=");
                    sb.append(co.getId());
                    sb.append("\" title=\"Delete Comment\">");
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
        sb.append(endl);

        return sb.toString();
    }

    /**
     * Represent the blog user and authenticated user in one package along with the output buffer.
     */
    @SuppressWarnings({"InstanceVariableOfConcreteClass"})
    @Transactional(value = Transactional.TxType.REQUIRED)
    static private class UserContext {
        private User blogUser;          // the blog owner
        private User authenticatedUser; // the logged in user

        /**
         * Default constructor for User Context.  Creates a usable instance.
         *
         * @param currentBlogUser blog owner
         * @param authUser        logged in user
         */
        UserContext(final User currentBlogUser, final User authUser) {
            this.blogUser = currentBlogUser;
            this.authenticatedUser = authUser;
        }

        /**
         * Retrieve the blog owner
         *
         * @return blog owner
         */
        public User getBlogUser() {
            return blogUser;
        }

        /**
         * Retrieve the authenticated aka logged in user.
         *
         * @return logged in user.
         */
        public User getAuthenticatedUser() {
            return authenticatedUser;
        }

        /**
         * Check to see if the authenticated user is the blog owner also. Used for private information.
         *
         * @return true if blog owner = auth owner
         */
        public boolean isAuthBlog() {
            return authenticatedUser != null && blogUser != null
                    && authenticatedUser.getUsername().compareTo(blogUser.getUsername()) == 0;
        }
    }
}
