/*
 * Copyright (c) 2005, 2009, 2011 Lucas Holt
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

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;

import com.justjournal.core.Settings;
import com.justjournal.utility.HTMLUtil;
import com.justjournal.utility.StringUtil;
import com.justjournal.utility.Spelling;
import com.justjournal.db.EntryTo;
import com.justjournal.db.EntryDAO;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Lucas Holt
 * @version $Id: EditEntry.java,v 1.3 2012/07/04 18:49:20 laffer1 Exp $
 */
public class EditEntry extends HttpServlet {
    private static final char endl = '\n';
    private static final Logger log = Logger.getLogger(UpdateJournal.class);
    @SuppressWarnings({"InstanceVariableOfConcreteClass"})
    private Settings set;  // global jj settings


    /**
     * Initializes the servlet.
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        set = Settings.getSettings(ctx);
    }

    /**
     * Print out the web page for HTML friendly clients.
     *
     * @param sb       output buffer
     * @param userName the blog owner
     * @param userID   the blog owner's id
     */
    private void htmlOutput(StringBuffer sb, String userName, int userID) {
        /* Initialize Preferences Object */
        User pf;
        try {
            pf = new User(userName);
        } catch (Exception ex) {
            WebError.Display("Load Error",
                    "Preferences could not be loaded for user " + userName,
                    sb);

            return;  // no more processing required
        }

        // Begin HTML document.
        // IE hates this.
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append(endl);

        //  sb.append("<?xml-stylesheet href=\"http://www.w3.org/StyleSheets/TR/W3C-REC.css\" type=\"text/css\"?>");
        // sb.append(endl);
        sb.append("<?xml-stylesheet href=\"#UserStyleSheet\" type=\"text/css\"?>");
        sb.append(endl);
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        sb.append(endl);

        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append(endl);

        sb.append("<head>");
        sb.append(endl);
        sb.append("\t<title>");
        sb.append(pf.getJournalName());
        sb.append("</title>");
        sb.append(endl);

        /* User's custom style URL.. i.e. uri to css doc outside domain */
        if (!pf.getStyleUrl().equals("") && pf.getStyleUrl() != null) {
            sb.append("\t<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"");
            sb.append(pf.getStyleUrl());
            sb.append("\" />");
            sb.append(endl);
        } else {
            /* use our template system instead */
            sb.append("\t<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"/styles/").append(pf.getStyleId()).append(".css\" />");
            sb.append(endl);
        }

        /* Optional style sheet overrides! */
        if (!pf.getStyleDoc().equals("") && pf.getStyleDoc() != null) {
            sb.append("<style type=\"text/css\" media=\"screen\">");
            sb.append(endl);
            sb.append("<!--");
            sb.append(endl);
            sb.append(pf.getStyleDoc());
            sb.append("-->");
            sb.append(endl);
            sb.append("</style>");
            sb.append(endl);
        }
        /* End overrides */
        sb.append("</head>\n");

        sb.append("<body>\n");

        // BEGIN MENU
        sb.append("\t<!-- Header: Begin -->");
        sb.append(endl);
        sb.append("\t\t<div id=\"header\">");
        sb.append(endl);
        sb.append("\t\t<h1>");
        sb.append(pf.getJournalName());
        sb.append("</h1>");
        sb.append(endl);
        sb.append("\t</div>");
        sb.append(endl);
        sb.append("\t<!-- Header: End -->\n");
        sb.append(endl);

        sb.append("\t<!-- Menu: Begin -->");
        sb.append(endl);
        sb.append("\t<div id=\"menu\">");
        sb.append(endl);

        sb.append("\t<p id=\"muser\">");
        sb.append(endl);
        sb.append("\t\t<a href=\"/users/");
        sb.append(userName);
        sb.append("\">recent entries</a><br />");
        sb.append(endl);
        sb.append("\t\t<a href=\"/users/").append(userName).append("/calendar\">Calendar</a><br />");
        sb.append(endl);
        sb.append("\t\t<a href=\"/users/").append(userName).append("/friends\">Friends</a><br />");
        sb.append(endl);
        sb.append("\t\t<a href=\"/profile.jsp?user=").append(userName).append("\">Profile</a><br />");
        sb.append(endl);
        sb.append("\t</p>");
        sb.append(endl);

        // General stuff...
        sb.append("\t<p id=\"mgen\">");
        sb.append(endl);
        sb.append("\t\t<a href=\"/update.jsp\">Update Journal</a><br />");
        sb.append(endl);

        // Authentication menu choice
        if (userID > 0) {
            // User is logged in.. give them the option to log out.
            sb.append("\t\t<a href=\"/prefs/index.jsp\">Preferences</a><br />");
            sb.append(endl);
            sb.append("\t\t<a href=\"/logout.jsp\">Log Out</a>");
            sb.append(endl);
        } else {
            // User is logged out.. give then the option to login.
            sb.append("\t\t<a href=\"/login.jsp\">Login</a>");
            sb.append(endl);
        }
        sb.append("\t</p>");
        sb.append(endl);

        sb.append("\t<p>");
        sb.append("<a href=\"/users/");
        sb.append(userName);
        sb.append("/subscriptions\">RSS Reader</a>");
        sb.append("<a href=\"/users/");
        sb.append(userName);
        sb.append("/rss\"><img src=\"/images/rss2.gif\" alt=\"Journal RSS Feed\" /></a><br />");

        sb.append("\t</p>");
        sb.append(endl);

        sb.append("\t</div>");
        sb.append(endl);
        sb.append("\t<!-- Menu: End -->\n");
        sb.append(endl);
        // END MENU

        sb.append("\t<!-- Content: Begin -->");
        sb.append(endl);
        sb.append("\t<div id=\"content\">");
        sb.append(endl);

        if (userID > 0) {
            sb.append("\t<p>You are logged in as <a href=\"/users/").append(userName).append("\"><img src=\"/images/userclass_16.png\" alt=\"user\" />").append(userName).append("</a>.</p>");
            sb.append(endl);
        }

        sb.append("\t\t<h2>Edit Journal Entry</h2>");
        sb.append(endl);

        sb.append("\t\t<p><strong>Your blog entry has been changed.</strong></p>");
        sb.append(endl);
        sb.append("\t\t<p><a href=\"/update.jsp\">Add an entry</a></p>");
        sb.append(endl);
        sb.append("\t\t<p><a href=\"/users/");
        sb.append(userName);
        sb.append("\">View journal</a></p>");
        sb.append(endl);

        sb.append("\t</div>");
        sb.append(endl);
        sb.append("\t<!-- Content: End -->");
        sb.append(endl);
        sb.append(endl);

        sb.append("\t<!-- Footer: Begin -->");
        sb.append(endl);
        sb.append("\t<div id=\"footer\">");
        sb.append(endl);
        sb.append("\t\t<a href=\"/index.jsp\" title=\"");
        sb.append(set.getSiteName()); // TODO: long name?
        sb.append("\">");
        sb.append(set.getSiteName());
        sb.append("</a> ");
        sb.append("\t</div>");
        sb.append(endl);

        sb.append("\t<!-- Footer: End -->\n");
        sb.append(endl);

        sb.append("</body>");
        sb.append(endl);
        sb.append("</html>");
        sb.append(endl);

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws java.io.IOException it is a web app
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws java.io.IOException {

        boolean blnError = false;
        final StringBuffer sb = new StringBuffer();

        // start session if one does not exist.
        final HttpSession session = request.getSession(true);
        int userID = 0;
        String userName;
        Integer userIDasi;

        userName = (String) session.getAttribute("auth.user");
        userIDasi = (Integer) session.getAttribute("auth.uid");

        if (userIDasi != null) {
            userID = userIDasi;
        }

        /* Detect user agent */
        String userAgent = request.getHeader("User-Agent");

        // Send HTML type in http stream
        String mimeType = HTMLUtil.determineMimeType(request.getHeader("Accept"), userAgent);
        response.setContentType(mimeType + "; charset=utf-8");
        response.setBufferSize(8192);
        response.setDateHeader("Expires", System.currentTimeMillis());
        response.setDateHeader("Last-Modified", System.currentTimeMillis());
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        // Validate the login
        if (userID < 1) {
            try {
                userName = request.getParameter("user");
                if (userName != null)
                    userName = userName.toLowerCase();
                String password = request.getParameter("pass");
                userID = WebLogin.validate(userName, password);

                String keepLogin = request.getParameter("keeplogin");
                if (keepLogin == null)
                    keepLogin = "";
                if (keepLogin.compareTo("checked") == 0) {
                    session.setAttribute("auth.uid", userID);
                    session.setAttribute("auth.user", userName);
                    WebLogin.setLastLogin(userID);
                }
            } catch (Exception e3) {

                WebError.Display("Authentication Error",
                        "Unable to login.  Please check your username and password.",
                        sb);
            }
        }

        if (userID > 0) {
            // We authenticated OK.  Continue...

            final EntryTo et = new EntryTo();

            // Get the user input
            int entryId = Integer.valueOf(request.getParameter("entryid"));
            int security = Integer.valueOf(request.getParameter("security"));
            int location = Integer.valueOf(request.getParameter("location"));
            int mood = Integer.valueOf(request.getParameter("mood"));
            String music = request.getParameter("music");
            String aformat = request.getParameter("aformat");
            String allowcomment = request.getParameter("allow_comment");
            String emailcomment = request.getParameter("email_comment");
            String tags = request.getParameter("tags");
            String trackback = request.getParameter("trackback");
            String date = request.getParameter("date");
            String time = request.getParameter("time");

            if (music == null)
                music = "";
            if (aformat == null)
                aformat = "";
            if (allowcomment == null)
                allowcomment = "";
            if (emailcomment == null)
                emailcomment = "";
            if (tags == null)
                tags = "";
            if (trackback == null)
                trackback = "";
            if (date == null) {
                java.text.SimpleDateFormat fmtdate = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.text.SimpleDateFormat fmttime = new java.text.SimpleDateFormat("HH:mm:ss");
                java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
                date = fmtdate.format(now);
                time = fmttime.format(now);
            }
            if (time == null) {
                time = ""; // assume date was set ok
            }

            music = music.trim();
            aformat = aformat.trim();
            allowcomment = allowcomment.trim();
            emailcomment = emailcomment.trim();
            tags = tags.trim();
            tags = tags.toLowerCase();  // tags must be lowercase
            trackback = trackback.trim();
            date = date.trim();
            time = time.trim();
            time = StringUtil.replace(time, 'T', "");
            log.debug("date: " + date);
            log.debug("time: " + time);
            String subject = request.getParameter("subject");
            String body = request.getParameter("body");

            if ((subject == null || subject.equals("")) && body != null) {
                final Pattern p = Pattern.compile("(<title>)(.*?)(</title>)");

                final Matcher m = p.matcher(body);

                if (m.find()) {
                    subject = m.group(2);
                    body = m.replaceAll("");
                }
            }

            // escape out for MySQL
            subject = StringUtil.replace(subject, '\'', "\\\'");
            body = StringUtil.replace(body, '\'', "\\\'");

            try {
                et.setId(entryId);
                et.setUserId(userID);
                et.setDate(date + " " + time);
                et.setSubject(subject);
                et.setBody(body);
                et.setMusic(StringUtil.replace(music, '\'', "\\\'"));
                et.setSecurityLevel(security);
                et.setLocationId(location);
                et.setMoodId(mood);

                // the check box says disable auto format
                if ((aformat != null && aformat.equals("checked")))
                    et.setAutoFormat(true);
                else
                    et.setAutoFormat(false);

                // disable comments
                if ((allowcomment != null && allowcomment.equals("checked")))
                    et.setAllowComments(true);
                else
                    et.setAllowComments(false);

                // disable email notifications
                if ((emailcomment != null && emailcomment.equals("checked")))
                    et.setEmailComments(true);
                else
                    et.setEmailComments(false);


            } catch (IllegalArgumentException e1) {
                WebError.Display("Input Error", e1.getMessage(), sb);
                blnError = true;
            }

            final String isSpellCheck = request.getParameter("spellcheck");
            if (isSpellCheck != null &&
                    isSpellCheck.compareTo("checked") == 0) {
                Spelling sp = new Spelling();

                // store everything
                session.setAttribute("spell.check", "true");
                session.setAttribute("spell.body", et.getBody());
                session.setAttribute("spell.music", et.getMusic());
                session.setAttribute("spell.location", et.getLocationId());
                session.setAttribute("spell.subject", et.getSubject());
                session.setAttribute("spell.date", et.getDate());
                session.setAttribute("spell.security", et.getSecurityLevel());
                session.setAttribute("spell.mood", et.getMoodId());
                session.setAttribute("spell.tags", tags);
                session.setAttribute("spell.trackback", trackback);

                //check the spelling now
                session.setAttribute("spell.suggest", sp.checkSpelling(HTMLUtil.textFromHTML(et.getBody())));

                // redirect the user agent to the promised land.
                response.sendRedirect("/editentry.jsp");

            } else {
                // clear out spell check variables to be safe
                // note this might be wrong still
                session.setAttribute("spell.check", ""); //false
                session.setAttribute("spell.body", "");
                session.setAttribute("spell.music", "");
                session.setAttribute("spell.location", 0);
                session.setAttribute("spell.subject", "");
                session.setAttribute("spell.date", "");
                session.setAttribute("spell.time", "");
                session.setAttribute("spell.security", 0);
                session.setAttribute("spell.mood", -1);
                session.setAttribute("spell.tags", "");
                session.setAttribute("spell.trackback", "");

                // create entry
                if (!blnError) {
                    boolean result = EntryDAO.update(et);

                    if (!result) {

                        WebError.Display("Error", "Error editing the journal entry", sb);

                        blnError = true;
                    }
                }

                // add tags
                if (!blnError) {
                    log.debug("Add Tags");
                    if (tags != null && tags.length() > 0) {
                        ArrayList<String> t = new ArrayList<String>();
                        StringTokenizer st = new StringTokenizer(tags, " :,;");
                        while (st.hasMoreTokens()) {
                            String tok = st.nextToken();
                            tok = StringUtil.deleteChar(tok, ',');
                            tok = StringUtil.deleteChar(tok, ':');
                            tok = StringUtil.deleteChar(tok, ' ');
                            tok = StringUtil.deleteChar(tok, ';');
                            if (StringUtil.isAlpha(tok))
                                t.add(tok);
                            else
                                log.debug("tag is: " + tok);
                        }

                        // lookup the tag id
                        if (t.size() > 0) {
                            EntryTo et2 = EntryDAO.viewSingle(et);
                            EntryDAO.setTags(et2.getId(), t);
                        }
                    }
                }

                // display message to user.
                if (!blnError) {

                    htmlOutput(sb, userName, userID);


                }

                // output the result of our processing
                final ServletOutputStream outstream = response.getOutputStream();
                outstream.println(sb.toString());
                outstream.flush();
            }

        } else {

            // We couldn't authenticate.  Tell the user.
            WebError.Display("Authentication Error",
                    "Unable to login.  Please check your username and password.",
                    sb);

            // output the result of our processing
            final ServletOutputStream outstream = response.getOutputStream();
            outstream.println(sb.toString());
            outstream.flush();
        }

    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Edit journal entry";
    }
}
