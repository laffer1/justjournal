/*
Copyright (c) 2003-2009, Lucas Holt
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

import com.justjournal.db.SQLHelper;
import com.justjournal.utility.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jj.play.ns.nl.captcha.Captcha;

import org.apache.log4j.Logger;

import java.sql.ResultSet;

/**
 * Creates a new journal account.
 *
 * @author Lucas Holt
 * @version $Id: NewAccount.java,v 1.14 2012/07/04 18:49:20 laffer1 Exp $
 * @since 1.0
 *        <p/>
 *        Creation Date: Feb 22, 2003
 */

public final class NewAccount extends JustJournalBaseServlet {
    private static final Logger log = Logger.getLogger(NewAccount.class);


    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {

        boolean blnError = false;
        final String title = "New Account";

        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        sb.append(endl);
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append(endl);

        sb.append("<head>");
        sb.append(endl);
        sb.append("<title>JustJournal.com: " + title + "</title>");
        sb.append(endl);

        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"./layout.css\" media=\"all\"/>");
        sb.append(endl);
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"./font-normal.css\" media=\"all\"/>");
        sb.append(endl);
        sb.append("<link rel=\"home\" title=\"Home\" href=\"./index.jsp\"/>");
        sb.append(endl);
        sb.append("<link rel=\"contents\" title=\"Site Map\" href=\"./sitemap.jsp\"/>");
        sb.append(endl);
        sb.append("<link rel=\"help\" title=\"Technical Support\" href=\"./support/index.jsp\"/>");
        sb.append(endl);

        sb.append("</head>");
        sb.append(endl);


        sb.append("<body>");
        sb.append(endl);

        sb.append("<!-- Header: Begin -->");
        sb.append(endl);
        sb.append("<div id=\"header\" >");
        sb.append(endl);
        sb.append("<img src=\"/images/jj_header.gif\" alt=\"JustJournal\" width=\"614\" height=\"202\" style=\"border:0;\" usemap=\"#Map\" />");
        sb.append(endl);
        sb.append("<map name=\"Map\" id=\"Map\"><area shape=\"rect\" coords=\"483,163,509,196\" href=\"/update.jsp\" alt=\"Write\" />");
        sb.append(endl);
        sb.append("<area shape=\"rect\" coords=\"514,163,544,198\" href=\"/search/index.jsp\" alt=\"Search\" />");
        sb.append(endl);
        sb.append("<area shape=\"rect\" coords=\"549,165,575,195\" href=\"/support/index.jsp\" alt=\"Help\" />");
        sb.append(endl);
        sb.append("<area shape=\"rect\" coords=\"135,93,392,146\" href=\"/index.jsp\" alt=\"JustJournal\" />");
        sb.append(endl);
        sb.append("</map>");
        sb.append(endl);
        sb.append("</div>");
        sb.append(endl);
        sb.append("<!-- Header: End -->");
        sb.append(endl);

        if (set.isUserAllowNew()) {

            final String fname = fixInput(request, "fname");
            final String email = fixInput(request, "email").toLowerCase();
            final String username = fixInput(request, "username").toLowerCase();
            final String password = fixInput(request, "password");
            final String captcha = fixInput(request, "captcha");

            if (captcha.compareTo((String) session.getAttribute(Captcha.NAME)) != 0 ||
                    !StringUtil.lengthCheck(captcha, 2, 50)) {
                blnError = true;
                WebError.Display("Input Error", "Try to type the captcha again.", sb);
            }

            if (!StringUtil.lengthCheck(fname, 2, 20)) {
                blnError = true;
                WebError.Display("Input Error",
                        "first name must be 2-20 characters.",
                        sb);
            }

            if (!StringUtil.lengthCheck(email, 6, 100)) {
                blnError = true;
                WebError.Display("Input Error",
                        "e-mail address must be valid.",
                        sb);
            } else if (!StringUtil.isEmailValid(email)) {
                blnError = true;
                WebError.Display("Input Error",
                        "e-mail address must be valid.",
                        sb);
            }

            if (!StringUtil.lengthCheck(username, 3, 15)) {
                blnError = true;
                WebError.Display("Input Error",
                        "username must be 3-15 characters.",
                        sb);
            }

            if (!StringUtil.lengthCheck(password, 5, 18)) {
                blnError = true;
                WebError.Display("Input Error",
                        "Password must be 5-18 characters.",
                        sb);
            }

            if (!WebLogin.isUserName(username)) {
                blnError = true;
                WebError.Display("Input Error",
                        "Username must be letters and numbers only",
                        sb);
            }

            if (!WebLogin.isPassword(password)) {
                blnError = true;
                WebError.Display("Input Error",
                        "Password must be 5-18 characters.",
                        sb);
            }

            if (!blnError) {
                // add user sql
                String SqlStatement = "Insert INTO user (username,password,name) VALUES('" + username + "',sha1('" + password + "'),'" + fname + "');";
                String SqlStatement3 = "SELECT id FROM user WHERE username='" + username + "' LIMIT 1;";

                try {
                    SQLHelper.executeNonQuery(SqlStatement);
                    ResultSet RS = SQLHelper.executeResultSet(SqlStatement3);

                    if (RS.next()) {
                        String myID = RS.getString("id");

                        // contact
                        String SqlStatement2 = "Insert INTO user_contact (id,email) VALUES('" + myID + "','" + email + "');";
                        SQLHelper.executeNonQuery(SqlStatement2);

                        // bio
                        String sqlStatement4 = "Insert INTO user_bio (id,content) VALUES('" + myID + "','');";
                        SQLHelper.executeNonQuery(sqlStatement4);

                        // prefs
                        String sqlStatement5 = "Insert INTO user_pref (id) VALUES('" + myID + "');";
                        SQLHelper.executeNonQuery(sqlStatement5);

                        // location
                        String sqlStatement6 = "Insert INTO user_location (id,city) VALUES('" + myID + "','');";
                        SQLHelper.executeNonQuery(sqlStatement6);

                        // style
                        String sqlStatement7 = "Insert INTO user_style (id) VALUES('" + myID + "');";
                        SQLHelper.executeNonQuery(sqlStatement7);

                        sb.append("<div id=\"content\">");
                        sb.append(endl);
                        sb.append("<h2>" + title + "</h2>");
                        sb.append(endl);
                        sb.append("<p>User Added. <a href=\"/\">View the site</a> or <a href=\"/login.jsp\">Login</a>.</p>");
                        sb.append(endl);
                        sb.append("<p>After logging in, be sure to view preferences.  You can customize the appearance of your journal,");
                        sb.append(endl);
                        sb.append(" create a private journal, or add friends.</p>");
                        sb.append(endl);
                        sb.append("</div>");
                        sb.append(endl);
                    } else {
                        WebError.Display("DB Error", "Error adding e-mail address", sb);
                        //out.println( "<p>Error Adding e-mail address" );
                    }

                } catch (Exception e1) {
                    log.error(e1.getMessage());
                    WebError.Display("DB Error", "Database Error", sb);
                }
            } else {
                WebError.Display("Error", "Missing Information", sb);
            }
        } else {
            WebError.Display("Error", "Not currently accepting new users.  Try again later.", sb);
        }

        sb.append("<!-- Footer: Begin -->");
        sb.append(endl);
        sb.append("<div id=\"footer\">");
        sb.append(endl);
        sb.append("<p id=\"copyright\">&copy; 2003-2007 Lucas Holt.  All rights reserved.</p>");
        sb.append(endl);
        sb.append("<p><a href=\"/privacy.jsp\" title=\"Privacy Policy\">Privacy</a> |");
        sb.append(endl);
        sb.append("<a href=\"/sitemap.jsp\" title=\"Site Map\">Site Map</a></p>");
        sb.append(endl);
        sb.append(endl);
        sb.append("</div>");
        sb.append(endl);
        sb.append("<!-- Footer: End -->");
        sb.append(endl);

        sb.append("</body>");
        sb.append(endl);
        sb.append("</html>");
        sb.append(endl);
    }
}
