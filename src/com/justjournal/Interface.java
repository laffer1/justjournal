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

import com.justjournal.db.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 18, 2004
 * Time: 10:11:10 PM
 */
public class Interface extends HttpServlet {
    final static String endl = "\r\n";
    final static String SUCCESS_OK = "OK";
    final static String SUCCESS_FAIL = "FAIL";
    final static String MODE_LOGIN = "login";
    final static String MODE_GETFRIENDS = "getfriends";

    /**
     * Initializes the servlet.
     */
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

    }

    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, java.io.IOException {

        boolean blnError = false;
        final StringBuffer sb = new StringBuffer();
        String userName = "";
        int userID = 0;
        String mode = request.getParameter("mode");
        String errMsg = "";
        String ver;
        String clientversion;

        /*  Livejournal/ Just Journal interface implementation

        Server request is like

        Content-Type: text/plain

        name
        Mr. Test Account
        success
        OK
        message
        Hello Test Account

        */

        // Send HTML type in http stream
        response.setContentType("text/plain");
        //response.setDateHeader( "Expires", System.currentTimeMillis() );
        //response.setDateHeader( "Last-Modified", System.currentTimeMillis() );
        //response.setHeader( "Cache-Control", "no-store, no-cache, must-revalidate" );
        //response.setHeader( "Pragma", "no-cache" );

        // Validate the login

        try {
            userName = request.getParameter("user").toLowerCase();
            String password = request.getParameter("password");
            String hpassword = request.getParameter("hpassword");

            if (hpassword != null && hpassword.length() > 0)
            //userID = webLogin.validateMd5( userName, hpassword );
                errMsg = "Unsupported authenticated method.";
            else if (password != null && password.length() > 0)
                userID = webLogin.validate(userName, password);
            else
                errMsg = "Invalid Password";

        } catch (Exception e3) {
            errMsg = "Invalid Password";
        }


        if (userID > 0) {

            // We authenticated OK.  Continue...

            if (mode.equals(MODE_LOGIN)) {
                // for now lets ignore this stuff
                ver = request.getParameter("ver");
                clientversion = request.getParameter("clientversion");

                String getmenus; // web jump menus
                // start number for moods
                Integer moodStart = Integer.valueOf(request.getParameter("getmoods"));

                getmenus = request.getParameter("getmenus");

                try {
                    Preferences pf = new Preferences(userName);

                    sb.append("name");
                    sb.append(endl);
                    sb.append(pf.getName());
                    sb.append("'s Journal");
                    sb.append(endl);

                    sb.append("success");
                    sb.append(endl);

                    if (errMsg.length() == 0) {
                        sb.append(SUCCESS_OK);
                        sb.append(endl);
                    } else {
                        sb.append(SUCCESS_FAIL);
                        sb.append(endl);
                        sb.append("errmsg");
                        sb.append(endl);
                    }


                    sb.append("message");
                    sb.append("Hello ");
                    sb.append(pf.getName());

                    if (getmenus != null && getmenus.length() > 0) {
                        sb.append("menu_0_1_text");
                        sb.append(endl);
                        sb.append("Recent Entries");
                        sb.append(endl);
                        sb.append("menu_0_1_url");
                        sb.append(endl);
                        sb.append("http://www.justjournal.com/users/" + userName);
                        sb.append(endl);

                        sb.append("menu_0_2_text");
                        sb.append(endl);
                        sb.append("Calendar View");
                        sb.append(endl);
                        sb.append("menu_0_2_url");
                        sb.append(endl);
                        sb.append("http://www.justjournal.com/users/" + userName + "/calendar");
                        sb.append(endl);

                        sb.append("menu_0_count");
                        sb.append(endl);
                        sb.append("2");
                        sb.append(endl);
                        // TODO: add the rest (menus) here!

                    }

                    if (moodStart != null) {
                        MoodDao mdao = new MoodDao();
                        // TODO: add start at x number command!
                        Collection moods = mdao.view();


                        /* Iterator */
                        MoodTo o;
                        final Iterator itr = moods.iterator();
                        int i;
                        int n;

                        for (i = 0, n = moods.size(); i < n; i++) {
                            o = (MoodTo) itr.next();

                            sb.append("mood_");
                            sb.append(i + 1);
                            sb.append("_id");
                            sb.append(endl);
                            sb.append(o.getId());
                            sb.append(endl);

                            sb.append("mood_");
                            sb.append(i + 1);
                            sb.append("_name");
                            sb.append(endl);
                            sb.append(o.getName());
                            sb.append(endl);

                            sb.append("mood_");
                            sb.append(i + 1);
                            sb.append("_parent");
                            sb.append(endl);
                            sb.append(o.getParent());
                            sb.append(endl);

                        }

                        sb.append("mood_count");
                        sb.append(endl);
                        sb.append(i + 1);
                        sb.append(endl);
                    }

                } catch (Exception e) {
                    sb.delete(0, sb.length() - 1);

                    sb.append("success");
                    sb.append(endl);

                    sb.append(SUCCESS_FAIL);
                    sb.append(endl);

                    sb.append("errmsg");
                    sb.append(endl);
                    sb.append(errMsg);
                    sb.append(endl);
                }

            } else if (mode.equals(MODE_GETFRIENDS)) {

                sb.append("success");
                sb.append(endl);

                if (errMsg.length() == 0) {
                    sb.append(SUCCESS_OK);
                    sb.append(endl);
                } else {
                    sb.append(SUCCESS_FAIL);
                    sb.append(endl);
                    sb.append("errmsg");
                    sb.append(endl);
                }

                FriendsDao fdao = new FriendsDao();
                Collection friends = fdao.view(userID);

                /* Iterator */
                FriendTo o;
                final Iterator itr = friends.iterator();
                int i;
                int n;

                for (i = 0, n = friends.size(); i < n; i++) {
                    o = (FriendTo) itr.next();

                    sb.append("friend_");
                    sb.append(i + 1);
                    sb.append("_user");
                    sb.append(endl);
                    sb.append(o.getUserName());
                    sb.append(endl);

                    // todo: add the other properties

                }

                sb.append("friend_count");
                sb.append(endl);
                sb.append(i + 1);
                sb.append(endl);


            } else if (false) {


                EntryTo et = new EntryTo();

                // Get the user input
                int security = Integer.valueOf(request.getParameter("security")).intValue();
                int location = Integer.valueOf(request.getParameter("location")).intValue();
                int mood = Integer.valueOf(request.getParameter("mood")).intValue();
                String music = request.getParameter("music");
                String aformat = request.getParameter("aformat");
                String discomments = request.getParameter("discomments");
                String noemail = request.getParameter("noemail");

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
                        et.setAutoFormat(false);
                    else
                        et.setAutoFormat(true);

                    // disable comments
                    if (discomments != null && discomments.equals("checked"))
                        et.setAllowComments(false);
                    else
                        et.setAllowComments(true);

                    // disable email notifications
                    if (noemail != null && noemail.equals("checked"))
                        et.setEmailComments(false);
                    else
                        et.setEmailComments(true);


                } catch (IllegalArgumentException e1) {
                    webError.Display("Input Error", e1.getMessage(), sb);
                    blnError = true;
                }


                // insert header fields
                if (blnError == false) {
                    EntryDAO edao = new EntryDAO();
                    boolean result = edao.add(et);

                    if (result == false)
                        webError.Display("Error", "Error adding the journal entry", sb);
                }


                // display message to user.
                if (blnError == false) {

                    /* Initialize Preferences Object */
                    Preferences pf;
                    try {
                        pf = new Preferences(userName);
                    } catch (Exception ex) {
                        throw new ServletException(ex);
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
                    sb.append("\t<title>" + pf.getName() + "'s Journal</title>");
                    sb.append(endl);

                    /* User's custom style URL.. i.e. uri to css doc outside domain */
                    if (pf.getStyleUrl() != "" && pf.getStyleUrl() != null) {
                        sb.append("\t<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"" + pf.getStyleUrl() + "\" />");
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
                    sb.append("\t\t<h1>" + pf.getName() + "'s Journal</h1>");
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

                    sb.append("\t<p>RSS Syndication<br /><br />");
                    sb.append("<a href=\"/users/" + userName + "/rss\"><img src=\"/img/v4_xml.gif\" alt=\"RSS content feed\" /> Recent</a>");
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

                    sb.append("</body>\n");
                    sb.append("</html>");
                    sb.append(endl);
                }
            } else {
                // we have an unsupported method
                sb.append("success");
                sb.append(endl);
                sb.append(SUCCESS_FAIL);
                sb.append(endl);

                sb.append("errmsg");
                sb.append(endl);
                sb.append("unsupported mode");
                sb.append(endl);
            }

            // output the result of our processing
            ServletOutputStream outstream = response.getOutputStream();
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
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "public interface";
    }

}

