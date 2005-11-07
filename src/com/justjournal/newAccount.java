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

import sun.jdbc.rowset.CachedRowSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Creates a new journal account.
 *
 * @author Lucas Holt
 * @version 1.1
 * @since 1.0
 *        <p/>
 *        Creation Date: Feb 22, 2003
 */

public final class newAccount extends HttpServlet {

    /**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    public void doPost(final HttpServletRequest request,
                       final HttpServletResponse response)
            throws IOException {
        boolean blnError = false;

        response.setContentType("text/html");

        final PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");


        final String title = "New Account";
        out.println("<title>JustJournal.com:" + title + "</title>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" title=\"default\" href=\"/layout1.css\">");
        out.println("</head>");


        out.println("<body>");

        out.println("<div id=\"login\" style=\"color: silver; font-weight: bold;\">");
        out.println("<img src=\"/images/jj-pencil.gif\" alt=\"jj pencil\" style=\"float: left;\" />");
        out.println("</div>");

        out.println("<!-- Header: Begin -->");
        out.println("<div id=\"header\">");
        out.println("\t<h1>JustJournal.com</h1>");
        out.println("</div>");
        out.println("<!-- Header: End -->");

        final String fname = request.getParameter("fname");
        final String email = request.getParameter("email").toLowerCase();
        final String username = request.getParameter("username").toLowerCase();
        final String password = request.getParameter("password");

        if (fname.length() < 2) {
            blnError = true;
            webError.Display("Input Error",
                    "first name must be at least 2 characters.",
                    out);
        }

        if (email.length() < 6) {
            blnError = true;
            webError.Display("Input Error",
                    "e-mail address must be valid.",
                    out);
        } else if (!StringUtil.isEmailValid(email)) {
            blnError = true;
            webError.Display("Input Error",
                    "e-mail address must be valid.",
                    out);
        }

        if (username.length() < 3) {
            blnError = true;
            webError.Display("Input Error",
                    "username must be at least 3 characters.",
                    out);
        }

        if (password.length() < 5) {
            blnError = true;
            webError.Display("Input Error",
                    "Password must be at least 5 characters.",
                    out);
        }


        if (!blnError) {
            // add user sql
            String SqlStatement = "Insert INTO user (username,password,name) VALUES('" + username + "',sha1('" + password + "'),'" + fname + "');";
            String SqlStatement3 = "SELECT id FROM user WHERE username='" + username + "' LIMIT 1;";

            try {
                SQLHelper.executeNonQuery(SqlStatement);
                CachedRowSet RS = SQLHelper.executeResultSet(SqlStatement3);

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

                    out.println("<div id=\"content\">");
                    out.println("<h2>" + title + "</h2>");
                    out.println("<p>User Added. <a href=\"/\">View the site</a> or <a href=\"/login.jsp\">Login</a>.</p>");
                    out.println("</div>");
                } else {
                    webError.Display("DB Error", "Error adding e-mail address", out);
                    //out.println( "<p>Error Adding e-mail address" );
                }

            } catch (Exception e1) {
                webError.Display("DB Error", "Database Error", out);
                //out.println( "<p>Database Error</p>" );
                //out.println( e1.getMessage() );
            }
        } else {
            webError.Display("Error", "Missing Information", out);
        }

        out.println("</body>");
        out.println("</html>");

        out.flush();
    }

    public void doGet(final HttpServletRequest request,
                      final HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        final PrintWriter out = response.getWriter();
        out.println("Method not supported.");
        out.flush();

    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "create a new account for journal service";
    }

}
