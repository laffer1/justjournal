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

package com.justjournal;

import com.justjournal.db.EntryDAO;
import com.justjournal.db.EntryTo;
import com.justjournal.utility.Spelling;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Adds journal entries to database.
 * <p/>
 * Optionally spell checks entries and returns the user
 * to the update view to make changes.
 *
 * @author Lucas Holt
 * @version 1.4
 * @since 1.0
 *        Created on March 23, 2003, 12:42 PM
 *        <p/>
 *        1.4 Changed default behavior for allow comments flag.  Assumes
 *        the user will uncheck a box to disable comments.  auto formatting
 *        was changed in a similar manner for usability.
 */
public final class updateJournal extends HttpServlet {

    static final char endl = '\n';

    /**
     * Initializes the servlet.
     */
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    private void htmlOutput(StringBuffer sb, String userName, int userID) {
        /* Initialize Preferences Object */
        Preferences pf;
        try {
            pf = new Preferences(userName);
        } catch (Exception ex) {
            webError.Display("Load Error",
                    "Preferences could not be loaded for user " + userName,
                    sb);

            return;  // no more processing required
        }

        // Begin HTML document.
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        sb.append(endl);

        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append(endl);

        sb.append("<head>");
        sb.append(endl);
        if (pf.isSpiderAllowed() == false) {
            sb.append("\t<meta name=\"robots\" content=\"noindex, nofollow, noarchive\" />");
            sb.append(endl);
            sb.append("\t<meta name=\"googlebot\" content=\"nosnippet\" />");
            sb.append(endl);
        }
        sb.append("\t<title>");
        sb.append(pf.getName());
        sb.append("'s Journal</title>");
        sb.append(endl);

        /* User's custom style URL.. i.e. uri to css doc outside domain */
        if (pf.getStyleUrl() != "" && pf.getStyleUrl() != null) {
            sb.append("\t<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"");
            sb.append(pf.getStyleUrl());
            sb.append("\" />");
            sb.append(endl);
        } else {
            /* use our template system instead */
            sb.append("\t<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"/styles/" + pf.getStyleId() + ".css\" />");
            sb.append(endl);
        }

        /* Optional style sheet overrides! */
        if (pf.getStyleDoc() != "" && pf.getStyleDoc() != null) {
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
        sb.append(pf.getName());
        sb.append("'s Journal</h1>");
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

        sb.append("\t<p>RSS Syndication<br /><br />");
        sb.append("<a href=\"/users/");
        sb.append(userName);
        sb.append("/rss\"><img src=\"/img/v4_xml.gif\" alt=\"RSS content feed\" /> Recent</a><br />");
        sb.append("<a href=\"/users/");
        sb.append(userName);
        sb.append("/subscriptions\">Subscriptions</a>");
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
            sb.append("\t<p>You are logged in as <a href=\"/users/" + userName + "\"><img src=\"/images/user.gif\" alt=\"user\" />" + userName + "</a>.</p>");
            sb.append(endl);
        }

        sb.append("\t\t<h2>Update Journal</h2>");
        sb.append(endl);

        sb.append("\t\t<p><strong>entry added</strong></p>");
        sb.append(endl);
        sb.append("\t\t<p><a href=\"/update.jsp\">Add another entry</a></p>");
        sb.append(endl);
        sb.append("\t\t<p><a href=\"/users/" + userName + "\">View journal</a></p>");
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
        sb.append("\t\t<a href=\"/index.jsp\" title=\"JustJournal.com: Online Journals\">JustJournal.com</a> ");
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
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws java.io.IOException {

        boolean blnError = false;
        final StringBuffer sb = new StringBuffer();

        // start session if one does not exist.
        final HttpSession session = request.getSession(true);
        int userID = 0;
        String userName = (String) session.getAttribute("auth.user");
        final Integer userIDasi = (Integer) session.getAttribute("auth.uid");

        if (userIDasi != null) {
            userID = userIDasi.intValue();
        }

        String userAgent = request.getHeader("User-Agent");
        boolean webClient = true;  // browser

        if (userAgent != null && userAgent.indexOf("JustJournal") > -1)
            webClient = false; // desktop client.. win/mac


        if (webClient) {
            // Send HTML type in http stream
            response.setContentType("text/html");
            response.setDateHeader("Expires", System.currentTimeMillis());
            response.setDateHeader("Last-Modified", System.currentTimeMillis());
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
        } else {
            response.setContentType("text/xml");
            response.setDateHeader("Expires", System.currentTimeMillis());
            response.setDateHeader("Last-Modified", System.currentTimeMillis());
        }

        // Validate the login
        if (userID < 1) {
            try {
                userName = request.getParameter("user").toLowerCase();
                String password = request.getParameter("pass");
                userID = webLogin.validate(userName, password);

                if (request.getParameter("keeplogin").compareTo("checked") == 0) {
                    session.setAttribute("auth.uid", new Integer(userID));
                    session.setAttribute("auth.user", userName);
                }
            } catch (Exception e3) {
                if (webClient)
                    webError.Display("Authentication Error",
                            "Unable to login.  Please check your username and password.",
                            sb);
                else
                    sb.append("JJ.LOGIN.FAIL");
            }
        }


        if (userID > 0) {
            // We authenticated OK.  Continue...

            final EntryTo et = new EntryTo();

            // Get the user input
            int security = Integer.valueOf(request.getParameter("security")).intValue();
            int location = Integer.valueOf(request.getParameter("location")).intValue();
            int mood = Integer.valueOf(request.getParameter("mood")).intValue();
            String music = request.getParameter("music");
            String aformat = request.getParameter("aformat");
            String allowcomment = request.getParameter("allow_comment");
            String emailcomment = request.getParameter("email_comment");

            if (music == null)
                music = "";

            try {
                et.setUserId(userID);
                et.setDate(request.getParameter("date"));
                et.setSubject(StringUtil.replace(request.getParameter("subject"), '\'', "\\\'"));
                et.setBody(StringUtil.replace(request.getParameter("body"), '\'', "\\\'"));
                et.setMusic(StringUtil.replace(music, '\'', "\\\'"));
                et.setSecurityLevel(security);
                et.setLocationId(location);
                et.setMoodId(mood);

                // the check box says disable auto format
                if (aformat != null && aformat.equals("checked"))
                    et.setAutoFormat(true);
                else
                    et.setAutoFormat(false);

                // disable comments
                if (allowcomment != null && allowcomment.equals("checked"))
                    et.setAllowComments(true);
                else
                    et.setAllowComments(false);

                // disable email notifications
                if (emailcomment != null && emailcomment.equals("checked"))
                    et.setEmailComments(true);
                else
                    et.setEmailComments(false);


            } catch (IllegalArgumentException e1) {
                if (webClient)
                    webError.Display("Input Error", e1.getMessage(), sb);
                else
                    sb.append("JJ.JOURNAL.UPDATE.FAIL");

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
                session.setAttribute("spell.location", new Integer(et.getLocationId()));
                session.setAttribute("spell.subject", et.getSubject());
                session.setAttribute("spell.date", et.getDate());
                session.setAttribute("spell.security", new Integer(et.getSecurityLevel()));
                session.setAttribute("spell.mood", new Integer(et.getMoodId()));

                //check the spelling now
                session.setAttribute("spell.suggest", sp.checkSpelling(et.getBody()));

                // redirect the user agent to the promised land.
                response.sendRedirect("/update.jsp");

            } else {
                // clear out spell check variables to be safe
                // note this might be wrong still
                session.setAttribute("spell.check", ""); //false
                session.setAttribute("spell.body", "");
                session.setAttribute("spell.music", "");
                session.setAttribute("spell.location", new Integer(0));
                session.setAttribute("spell.subject", "");
                session.setAttribute("spell.date", "");
                session.setAttribute("spell.security", new Integer(0));
                session.setAttribute("spell.mood", new Integer(0));

                // insert header fields
                if (blnError == false) {
                    EntryDAO edao = new EntryDAO();
                    boolean result = edao.add(et);

                    if (result == false)
                        if (webClient)
                            webError.Display("Error", "Error adding the journal entry", sb);
                        else
                            sb.append("JJ.JOURNAL.UPDATE.FAIL");
                }

                // display message to user.
                if (blnError == false) {
                    if (webClient)
                        htmlOutput(sb, userName, userID);
                    else
                        sb.append("JJ.JOURNAL.UPDATE.OK");
                }


                // output the result of our processing
                final ServletOutputStream outstream = response.getOutputStream();
                outstream.println(sb.toString());
                outstream.flush();
            }

        } else {
            if (webClient)
            // We couldn't authenticate.  Tell the user.
                webError.Display("Authentication Error",
                        "Unable to login.  Please check your username and password.",
                        sb);
            else
                sb.append("JJ.LOGIN.FAIL");

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
        return "adds a journal entry";
    }

}