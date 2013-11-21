/*
 * Copyright (c) 2003, 2004, 2005, 2006, 2008, 2011 Lucas Holt
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

import com.justjournal.db.CommentDao;
import com.justjournal.db.CommentTo;
import com.justjournal.db.EntryDAO;
import com.justjournal.db.EntryTo;
import com.justjournal.utility.FileIO;
import com.justjournal.utility.QueueMail;
import com.justjournal.utility.Spelling;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Adds a comment associated to a journal entry.
 *
 * @author Lucas Holt
 * @version $Id: AddComment.java,v 1.11 2011/07/02 01:22:44 laffer1 Exp $
 * @since 1.0
 *        Created on March 23, 2003, 12:42 PM
 *        <p/>
 *        1.3  Revised e-mail queuing code to make the e-mails format more
 *        appealing.  Fixed "null says:" bug.
 *        1.2  Comments weren't posting properly.  It appears a ! was missing in one of
 *        the error handling blocks.  Also reformatted the code to make that
 *        section more readable.
 *        1.1  Several bugs have been reported in this code.  Switched over to JustJournalBaseServlet
 *        class which has been tested for a few weeks.
 *        1.0  Initial release.  Allows comments associated with a specific journal entry
 *        to be added.
 */

/*  TODO:  FIX ERROR HANDLING IF DB TABLE IS MISSING! */
public final class AddComment extends JustJournalBaseServlet {
    private static final Logger log = Logger.getLogger(AddComment.class.getName());

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {

        boolean blnError = false;

        int userID = 0;
        String userName;
        Integer userIDasi;

        userName = (String) session.getAttribute("auth.user");
        userIDasi = (Integer) session.getAttribute("auth.uid");
        if (userIDasi != null) {
            userID = userIDasi;
        }

        // Validate the login

        if (userID < 1) {
            try {
                userName = request.getParameter("username");
                if (userName != null)
                    userName = userName.toLowerCase();
                String password = request.getParameter("password");
                userID = WebLogin.validate(userName, password);
            } catch (Exception e3) {
                log.warn("Auth Error: " + userName);
                WebError.Display("Authentication Error",
                        "Unable to login.  Please check your username and password.",
                        sb);
            }
        }

        if (userID > 0 && userName != null) {
            // We authenticated OK.  Continue...

            // Get the user input
            CommentTo comment = new CommentTo();

            try {
                String body = request.getParameter("body");
                String subject = request.getParameter("subject");

                if (subject == null)
                    subject = "No Subject";

                comment.setUserName(userName);
                comment.setUserId(userID);
                comment.setEid(Integer.valueOf(request.getParameter("id")));  // entry id
                // ' tick escaping done in comment DAO layer for subject and body.
                comment.setSubject(subject);
                comment.setBody(body);

                java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
                comment.setDate(fmt.format(now));

                EntryTo et = EntryDAO.viewSingle(comment.getEid(), false);

                if (!et.getAllowComments()) {
                    WebError.Display("Comments Blocked",
                            "The owner of this entry does not allow " +
                                    "comments.", sb);
                    blnError = true;
                }
            } catch (IllegalArgumentException e) {
                log.debug("Input Error: " + e.getMessage());
                WebError.Display("Input Error",
                        e.getMessage(),
                        sb);
            }

            if (!blnError) {
                String isSpellCheck = request.getParameter("spellcheck");
                if (isSpellCheck == null)
                    isSpellCheck = "";

                if (isSpellCheck.compareTo("checked") == 0) {
                    Spelling sp = new Spelling();

                    // store everything
                    session.setAttribute("spell.ccheck", "true");
                    session.setAttribute("spell.cbody", comment.getBody());
                    session.setAttribute("spell.csubject", comment.getSubject());

                    //check the spelling now
                    session.setAttribute("spell.csuggest", sp.checkSpelling(comment.getBody()));

                    // redirect the user agent to the promised land.
                    try {
                        response.sendRedirect("/comment/add.jsp?id=" + comment.getEid());
                    } catch (IOException e) {
                        log.error("Redirection error: " + e.getMessage());
                        WebError.Display("Redirect error",
                                e.getMessage(),
                                sb);
                    }
                } else {
                    // clear out spell check variables to be safe
                    // note this might be wrong still
                    session.setAttribute("spell.ccheck", ""); //false
                    session.setAttribute("spell.cbody", "");
                    session.setAttribute("spell.csubject", "");

                    // create header fields

                        try {
                            CommentDao cdao = new CommentDao();
                            blnError = !cdao.add(comment);
                        } catch (Exception e1) {
                            blnError = true;
                            log.debug("Error adding comment: " + e1.getMessage());
                            WebError.Display("Error", "Error adding comment", sb);
                        }

                        try {
                            EntryTo et = EntryDAO.viewSingle(comment.getEid(), false);
                            User pf = new User(et.getUserName());

                            if (et.getEmailComments()) {
                                QueueMail mail = new QueueMail();
                                mail.setFromAddress("donotreply@justjournal.com");
                                mail.setToAddress(pf.getEmailAddress());
                                mail.setBody(comment.getUserName() + " said: \n"
                                        + "Subject: " + comment.getSubject() + "\n"
                                        + comment.getBody() + "\n\nIn response to:\n"
                                        + "http://www.justjournal.com/comment/index.jsp?id="
                                        + comment.getEid() + "\n\n"
                                        + "From here, you can:\n\n"
                                        + "View all comments to this entry: "
                                        + "http://www.justjournal.com/comment/index.jsp?id="
                                        + comment.getEid() + "\n\n"
                                        + "Reply at the webpage: http://www.justjournal.com/comment/add.jsp?id="
                                        + comment.getEid()
                                        + "\n\n-- JustJournal.com\n\n"
                                        + "(If you would prefer not to get these updates," +
                                        " edit the entry to disable comment notifications.)\n");

                                mail.setSubject("JustJournal: Comment Notification");
                                mail.setPurpose("comment_notify");
                                mail.send();
                            }
                        } catch (Exception e) {
                            log.error("Could not send mail: " + e.getMessage());
                        }

                }
                // display message to user.
                if (!blnError) {
                    // Begin HTML document.
                    try {
                        String template = FileIO.ReadTextFile("/home/jj/docs/journal_template.inc");
                        String loginMenu;
                        StringBuilder content = new StringBuilder();

                        content.append("\t\t<h2>Add Comment</h2>");
                        content.append("\t\t<p><strong>Comment added successfully.</strong></p>");

                        // Authentication menu choice
                        if (userID > 0) {
                            // User is logged in.. give them the option to log out.
                            loginMenu = ("\t\t<a href=\"/prefs/index.jsp\">Preferences</a><br />");

                            loginMenu += ("\t\t<a href=\"/logout.jsp\">Log Out</a>");

                        } else {
                            // User is logged out.. give then the option to login.
                            loginMenu = ("\t\t<a href=\"/login.jsp\">Login</a>");
                        }

                        template = template.replaceAll("\\$JOURNAL_TITLE\\$", "JustJournal.com: Add Comment");
                        template = template.replaceAll("\\$USER\\$", userName);
                        template = template.replaceAll("\\$USER_STYLESHEET\\$", "6.css");
                        template = template.replaceAll("\\$USER_STYLESHEET_ADD\\$", "");
                        template = template.replaceAll("\\$USER_AVATAR\\$", "");
                        template = template.replaceAll("\\$RECENT_ENTRIES\\$", "");
                        template = template.replaceAll("\\$LOGIN_MENU\\$", loginMenu);
                        template = template.replaceAll("\\$CONTENT\\$", content.toString());
                        sb.append(template);
                    } catch (IOException e) {
                        log.error("Template Error: " + e.getMessage());
                        WebError.Display("Internal Error", "Error dislaying page.", sb);
                    }
                }
            }
        } else {
            // We couldn't authenticate.  Tell the user.
            log.warn("Auth Error: " + userName);
            WebError.Display("Authentication Error",
                    "Unable to login.  Please check your username and password.",
                    sb);
        }
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "adds a journal comment";
    }

}