/*
 * Copyright (c) 2003-2008, 2011 Lucas Holt
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

import com.justjournal.UserImpl;
import com.justjournal.WebError;
import com.justjournal.WebLogin;
import com.justjournal.db.EntryDao;
import com.justjournal.db.EntryTo;
import com.justjournal.utility.FileIO;
import com.justjournal.utility.StringUtil;
import com.justjournal.utility.Xml;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;

/**
 * Login account servlet.
 *
 * @author Lucas Holt
 *         <p/>
 *         Version 1.1 changes to a stringbuffer for output. This should improve performance a bit.
 *         <p/>
 *         1.2 fixed a bug with NULL pointer exceptions.
 *         <p/>
 *         Mon Sep 19 2005 1.3 added JJ.LOGIN.FAIL and JJ.LOGIN.OK for desktop clients.
 *         <p/>
 */
public final class LoginAccount extends JustJournalBaseServlet {
    private static final Logger log = Logger.getLogger(LoginAccount.class);
    private static final String JJ_LOGIN_OK = "JJ.LOGIN.OK";
    private static final String JJ_LOGIN_FAIL = "JJ.LOGIN.FAIL";
    //private static final String JJ_LOGIN_ERROR = "JJ.LOGIN.ERROR";  // server error

    private void htmlOutput(StringBuffer sb, String userName) {

        try {
            String template = FileIO.ReadTextFile("/home/jj/docs/journal_template.inc");
            String loginMenu;
            StringBuilder content = new StringBuilder();
            UserImpl user = new UserImpl(userName);

            content.append("\t\t<h2>Welcome back to Just Journal</h2>");
            content.append(endl);
            content.append("\t<p>You are logged in as <a href=\"/users/").append(userName).append("\"><img src=\"/images/userclass_16.png\" alt=\"user\" />").append(userName).append("</a>.</p>");
            if (user.getLastLogin() != null) {
                content.append("\t<p>Your last login was on ");
                content.append(user.getLastLogin());
                content.append("</p>");
            }
            WebLogin.setLastLogin(user.getUserId());
            content.append(endl);
            content.append("\t<p style=\"padding-left: 10px;\"><a href=\"/update.jsp\">Post a journal entry</a></p>");
            content.append(endl);
            content.append("\t<p style=\"padding-left: 10px;\"><img src=\"/images/userclass_16.png\" alt=\"user\" /> <a href=\"/users/").append(userName).append("\">View your journal</a>.</p>");
            content.append(endl);
            content.append("\t<p style=\"padding-left: 10px;\"><img src=\"/images/userclass_16.png\" alt=\"user\" /> <a href=\"/users/").append(userName).append("/friends\">Read your friends entries</a>.</p>");
            content.append(endl);

            final Collection entries;
            entries = EntryDao.viewFriends(user.getUserId(), user.getUserId());

            EntryTo o;
            final Iterator itr = entries.iterator();
            if (entries.size() != 0) {
                content.append("<h3>Recent Friends Entries</h3>");
                content.append(endl);
                content.append("<ul>");
                content.append(endl);
                for (int i = 0, n = entries.size(); i < n; i++) {
                    o = (EntryTo) itr.next();
                    content.append("<li><a href=\"users/");
                    content.append(o.getUserName());
                    content.append("\">");
                    content.append(o.getUserName());
                    content.append("</a>");
                    content.append(" - ");
                    content.append(Xml.cleanString(o.getSubject()));
                    content.append("</li>");
                    content.append(endl);
                }
                content.append("</ul>");
                content.append(endl);
            }

            // User is logged in.. give them the option to log out.
            loginMenu = ("\t\t<a href=\"/prefs/index.jsp\">Preferences</a><br />");

            loginMenu += ("\t\t<a href=\"/logout.jsp\">Log Out</a>");

            template = template.replaceAll("\\$JOURNAL_TITLE\\$", user.getJournalName());
            template = template.replaceAll("\\$USER\\$", userName);
            template = template.replaceAll("\\$USER_STYLESHEET\\$", user.getStyleId() + ".css");
            template = template.replaceAll("\\$USER_STYLESHEET_ADD\\$", user.getStyleDoc());
            if (user.showAvatar()) {
                String av = "\t\t<img alt=\"avatar\" src=\"/image?id="
                        + user.getUserId() + "\"/>";
                template = template.replaceAll("\\$USER_AVATAR\\$", av);
            } else
                template = template.replaceAll("\\$USER_AVATAR\\$", "");
            template = template.replaceAll("\\$RECENT_ENTRIES\\$", "");
            template = template.replaceAll("\\$LOGIN_MENU\\$", loginMenu);
            template = template.replaceAll("\\$CONTENT\\$", content.toString());
            sb.append(template);
        } catch (Exception e) {
            WebError.Display("Internal Error", "Error dislaying page. ", sb);
            e.printStackTrace();
            log.error(e);
        }
    }

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {
        boolean blnError = false;
        int userID;
        String userName = fixInput(request, "username");
        String password = fixInput(request, "password");
        String passwordHash = fixInput(request, "password_hash");
        String userAgent = fixHeaderInput(request, "User-Agent");
        String mobile = fixInput(request, "mobile");
        boolean webClient = true;  // browser

        // adjust the case
        userName = userName.toLowerCase();
        passwordHash = passwordHash.toLowerCase();

        // validate user input
        if (userAgent.toLowerCase().contains("justjournal"))
            webClient = false; // desktop client.. win/mac/java

        if (log.isDebugEnabled()) {
            log.debug("User Agent is: " + userAgent + endl);
            log.debug("Is web client? " + webClient + endl);
            log.debug("mobile: " + mobile + endl);
        }


        if (!StringUtil.lengthCheck(userName, 3, 15)) {
            blnError = true;
            if (webClient)
                WebError.Display("Input Error",
                        "username must be 3-15 characters.",
                        sb);
            else
                sb.append(JJ_LOGIN_FAIL);
        }

        if (passwordHash.compareTo("") == 0 && !StringUtil.lengthCheck(password, 5, 18)) {
            blnError = true;
            if (webClient)
                WebError.Display("Input Error",
                        "Please input a password.",
                        sb);
            else
                sb.append(JJ_LOGIN_FAIL);
        }

        if (!blnError) {
            try {
                if (log.isDebugEnabled())
                    log.debug("Attempting Login Validation  ");

                if (passwordHash.compareTo("") != 0) {
                    if (log.isDebugEnabled())
                        log.debug("Using SHA1 pass=" + passwordHash);

                    userID = WebLogin.validateSHA1(userName, passwordHash);
                } else {
                    if (log.isDebugEnabled())
                        log.debug("Using clear pass=" + password);

                    userID = WebLogin.validate(userName, password);
                }

                if (userID > 0) {
                    if (!webClient) {
                        sb.append(JJ_LOGIN_OK);
                        WebLogin.setLastLogin(userID);
                    } else if (mobile.compareTo("yes") == 0) {

                        session.setAttribute("auth.uid", userID);
                        session.setAttribute("auth.user", userName);

                        WebLogin.setLastLogin(userID);
                        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                        response.setHeader("Location", set.getBaseUri() + "mob/update.jsp");
                        sb = new StringBuffer();
                        return;
                    } else {
                        session.setAttribute("auth.uid", userID);
                        session.setAttribute("auth.user", userName);

                        // lastlogin is set after the html display
                        htmlOutput(sb, userName);
                    }
                } else {
                    if (webClient)
                        WebError.Display("Authentication Error",
                                "Unable to login.  Please check your username and password.",
                                sb);
                    else
                        sb.append(JJ_LOGIN_FAIL);
                }
            } catch (Exception e3) {
                if (webClient)
                    WebError.Display("Authentication Error",
                            "Unable to login.  Please check your username and password.",
                            sb);
                else
                    sb.append(JJ_LOGIN_FAIL);
                log.error(e3);
            }
        }

        if (log.isDebugEnabled())
            log.debug("Write out Response  ");

    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "login to journal service";
    }

}
