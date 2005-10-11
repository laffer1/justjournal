/*
Copyright (c) 2005, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

/*
 * addComment.java
 *
 * Created on March 23, 2003, 12:42 PM
 */

package com.justjournal;

import com.justjournal.db.CommentDao;
import com.justjournal.db.CommentTo;
import com.justjournal.db.EntryDAO;
import com.justjournal.db.EntryTo;
import com.justjournal.utility.QueueMail;
import com.justjournal.utility.Spelling;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Adds a comment associated to a journal entry.
 *
 * @author Lucas Holt
 * @version 1.1
 * @since 1.0
 *        <p/>
 *        1.1  Several bugs have been reported in this code.  Switched over to JustJournalBaseServlet
 *        class which has been tested for a few weeks.
 *        1.0  Initial release.  Allows comments associated with a specific journal entry
 *        to be added.
 */

/*  TODO:  FIX ERROR HANDLING IF DB TABLE IS MISSING!
    TODO:  Consider switching to maverick */
public final class addComment extends JustJournalBaseServlet {

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {

        boolean blnError = false;

        int userID = 0;
        String userName = (String) session.getAttribute("auth.user");
        final Integer userIDasi = (Integer) session.getAttribute("auth.uid");
        if (userIDasi != null) {
            userID = userIDasi.intValue();
        }

        // Validate the login

        if (userID < 1) {

            try {
                userName = request.getParameter("username").toLowerCase();
                String password = request.getParameter("password");
                userID = webLogin.validate(userName, password);
            } catch (Exception e3) {
                webError.Display("Authentication Error",
                        "Unable to login.  Please check your username and password.",
                        sb);
            }
        }

        if (userID > 0) {
            // We authenticated OK.  Continue...

            // Get the user input
            CommentTo comment = new CommentTo();

            try {
                String body = request.getParameter("body");
                String subject = request.getParameter("subject");

                if (subject == null)
                    subject = "No Subject";

                comment.setUserId(userID);
                comment.setEid(Integer.valueOf(request.getParameter("id")).intValue());  // entry id
                comment.setSubject(StringUtil.replace(subject, '\'', "\\\'"));
                comment.setBody(StringUtil.replace(body, '\'', "\\\'"));

                java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
                comment.setDate(fmt.format(now));

                EntryDAO dao = new EntryDAO();
                EntryTo et = dao.viewSingle(comment.getEid(), false);

                if (et.getAllowComments() == false)
                    webError.Display("Comments Blocked",
                            "The owner of this entry does not want" +
                            "any comments.", sb);
            } catch (IllegalArgumentException e) {
                webError.Display("Input Error",
                        e.getMessage(),
                        sb);
            }

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
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else {
                // clear out spell check variables to be safe
                // note this might be wrong still
                session.setAttribute("spell.ccheck", ""); //false
                session.setAttribute("spell.cbody", "");
                session.setAttribute("spell.csubject", "");

                // insert header fields
                if (blnError == false) {

                    try {
                        CommentDao cdao = new CommentDao();
                        blnError = !cdao.add(comment);

                    } catch (Exception e1) {
                        blnError = true;
                        webError.Display("Error", "Error adding comment", sb);
                        //out.println( e1.getMessage() );
                    }

                    try {
                        EntryDAO dao = new EntryDAO();
                        EntryTo et = dao.viewSingle(comment.getEid(), false);
                        Preferences pf = new Preferences(et.getUserName());

                        if (et.getEmailComments()) {
                            QueueMail mail = new QueueMail();
                            mail.setFromAddress("donotreply@justjournal.com");
                            mail.setToAddress(pf.getEmailAddress());
                            mail.setBody(comment.getUserName() + " said: \n"
                                    + comment.getBody() + "\n\n in response to:"
                                    + "http://www.justjournal.com/users/" + et.getUserName() +
                                    "/entry/" + comment.getEid());
                            mail.setSubject("JustJournal: Comment Notification");
                            mail.setPurpose("comment_notify");
                            mail.send();
                        }
                    } catch (Exception e) {

                    }
                }

                // display message to user.
                if (blnError == false) {
                    // Begin HTML document.
                    sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
                    sb.append(endl);

                    sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
                    sb.append(endl);

                    sb.append("<head>");
                    sb.append(endl);
                    sb.append("\t<title>JustJournal.com: Add Comment</title>");
                    sb.append(endl);


                    sb.append("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" title=\"default\" href=\"layout1.css\" />");

                    sb.append("</head>\n");

                    sb.append("<body>\n");

                    sb.append("<div id=\"login\" style=\"color: silver; font-weight: bold;\">");
                    sb.append("<img src=\"/images/jj-pencil.gif\" alt=\"jj pencil\" style=\"float: left;\" />");
                    sb.append("</div>");
                    sb.append(endl);


                    // BEGIN MENU
                    sb.append("\t<!-- Header: Begin -->");
                    sb.append(endl);
                    sb.append("\t\t<div id=\"header\">");
                    sb.append(endl);
                    sb.append("\t\t<h1>JustJournal.com</h1>");
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
                    sb.append("\t\t<a href=\"/users/" + userName + "\">recent entries</a><br />");
                    sb.append(endl);
                    sb.append("\t\t<a href=\"/users/" + userName + "/calendar\">Calendar</a><br />");
                    sb.append(endl);
                    sb.append("\t\t<a href=\"/users/" + userName + "/friends\">Friends</a><br />");
                    sb.append(endl);
                    sb.append("\t\t<a href=\"/users/" + userName + "/ljfriends\">LJ Friends</a><br />");
                    sb.append(endl);
                    sb.append("\t\t<a href=\"/profile.jsp?user=" + userName + "\">Profile</a><br />");
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

                    sb.append("\t<p><a href=\"/users/" + userName + "/rss\"><img src=\"/img/v4_xml.gif\" alt=\"RSS content feed\" /></a></p>");
                    sb.append(endl);

                    sb.append("\t</div>");
                    sb.append(endl);
                    sb.append("\t<!-- Menu: End -->\n");
                    sb.append(endl);

                    sb.append("\t<div id=\"content\">");
                    sb.append("\t\t<h2>Add Comment</h2>");
                    sb.append("\t\t<p><strong>Comment added successfully.</strong></p>");
                    sb.append("\t</div>");

                    sb.append("</body>");
                    sb.append("</html>");
                }
            }
        } else {
            // We couldn't authenticate.  Tell the user.
            webError.Display("Authentication Error",
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