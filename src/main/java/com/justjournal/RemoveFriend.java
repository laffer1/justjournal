/*
Copyright (c) 2003 Caryn Holt
Copyright (c) 2005, 2012 Lucas Holt
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

//
//  removeFriends.java
//
//  Created by Caryn Holt on Sun Jul 06 2003.
//		Modified on: 7/10/03
//			     Imported servlet code and implemented required functions
//			     Added code to delete entry from friends table
//

package com.justjournal;

import com.justjournal.db.SQLHelper;
import com.justjournal.utility.FileIO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author Caryn Holt
 * @version $Id: RemoveFriend.java,v 1.9 2009/06/01 22:57:42 laffer1 Exp $
 */
public final class RemoveFriend extends JustJournalBaseServlet {

    private static final Logger log = Logger.getLogger(RemoveFriend.class);

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {

        // Retreive username and user id
        String userName;
        Integer userIDasi;
        int userID = 0;

        userName = (String) session.getAttribute("auth.user");
        // Retreive user id
        userIDasi = (Integer) session.getAttribute("auth.uid");
        // convert Integer to int type
        if (userIDasi != null) {
            userID = userIDasi;
        }

        // friend id that will be removed
        String temp = fixInput(request, "id");
        int friendID = Integer.valueOf(temp);

        if (friendID < 1)
            WebError.Display("Error", "Must use a valid friend id.", sb);

        try {
            String sqlStatement = "Delete FROM friends where id ='" + userID + "' and friendid='" + friendID + "' LIMIT 1;";
            int rowsAffected = SQLHelper.executeNonQuery(sqlStatement);

            if (rowsAffected > 0) {
                try {
                    String template = FileIO.ReadTextFile("/home/jj/docs/journal_template.inc");
                    String loginMenu;
                    StringBuilder content = new StringBuilder();

                    content.append("\t\t<h2>Remove Friend</h2>");
                    content.append("\t\t<p>Your friend have been remove.</p>");

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
                    WebError.Display("Internal Error", "Error dislaying page.", sb);
                }
            } else
                WebError.Display("Error", "Could not remove friend.", sb);
        } catch (Exception e) {
            // record was not deleted
            log.error(e.getMessage());
            WebError.Display("Error", e.getMessage(), sb);
        }
    }

    // required function for servlets
    public String getServletInfo() {
        return "Removes a friend";
    }
}
