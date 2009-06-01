/*
Copyright (c) 2003-2009 Lucas Holt
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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 * @author Lucas Holt
 * @version $Id: UpdateStyleSheetOverride.java,v 1.8 2009/06/01 22:57:42 laffer1 Exp $
 */
public final class UpdateStyleSheetOverride
        extends HttpServlet {

    private static final Logger log = Logger.getLogger(UpdateStyleSheetOverride.class);

    protected void processRequest(final HttpServletRequest request,
                                  final HttpServletResponse response)
            throws java.io.IOException {
        // initial error condition is false
        boolean error = false;

        // Will be using session data, must initialize session, will not affect any current session
        final HttpSession session = request.getSession(true);

        // Retreive username
        // String username;
        //username = (String) session.getAttribute( "auth.user" );
        Integer userIDasi;
        // Retreive user id
        synchronized (session) {
            userIDasi = (Integer) session.getAttribute("auth.uid");
        }
        // convert Integer to int type
        int userID = 0;
        if (userIDasi != null) {
            userID = userIDasi.intValue();
        }

        String styleSheet = request.getParameter("css");

        // Send HTML type in http stream
        response.setContentType("text/html");
        final PrintWriter out = response.getWriter();

        if (styleSheet != null) {
            try {
                String sqlStatement = "Update user_style SET doc='" + styleSheet + "' where id ='" + userID + "' LIMIT 1;";
                int rowsAffected = SQLHelper.executeNonQuery(sqlStatement);
                if (rowsAffected < 1) {
                    error = true;
                } else {
                    out.println("Stylesheet doc has been imported.");
                }
            } catch (Exception e) {
                // record was not updated
                error = true;
                log.error(e.getMessage());
            }
        }

        if (error) {
            WebError.Display("Error", "Unknown error has occured.", out);
        }

        out.flush();

        return;
    }

    // processes get requests
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    // processes post requests
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    // required function for servlets
    public String getServletInfo() {
        return "Update the style sheet override";
    }
}
