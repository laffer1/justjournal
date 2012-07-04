/*
Copyright (c) 2003 Caryn Holt
Copyright (c) 2005-2011 Lucas Holt
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
//  AddFriends.java
//
//  Created by Caryn Holt on Sun Jul 06 2003
//		Modified on: 7/10/03
//			     Imported servlet code and implemented required functions
//			     Implemented processRequest
//

package com.justjournal;

import com.justjournal.db.SQLHelper;
import com.justjournal.utility.FileIO;
import com.justjournal.utility.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

/**
 * Adds a friend to the Users list for their friends page
 *
 * @author Caryn Holt, Lucas Holt
 * @version $Id: AddFriend.java,v 1.12 2012/07/04 18:47:44 laffer1 Exp $
 * @since 1.0
 */
public final class AddFriend extends JustJournalBaseServlet {
    private static final Logger log = Logger.getLogger(AddFriend.class);

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {
        // initial error condition is false
        boolean error = false;

        // Retrieve username
        String userName;
        Integer userIDasi;

        userName = (String) session.getAttribute("auth.user");
        // Retrieve user id
        userIDasi = (Integer) session.getAttribute("auth.uid");

        // convert Integer to int type
        int userID = 0;
        if (userIDasi != null) {
            userID = userIDasi;
        }

        String friend1 = fixInput(request, "fr1");
        String friend2 = fixInput(request, "fr2");
        String friend3 = fixInput(request, "fr3");
        String friend4 = fixInput(request, "fr4");
        String friend5 = fixInput(request, "fr5");

        int counter = 0;

        if (!StringUtil.lengthCheck(friend1, 3, 15)) {
            error = true;
            WebError.Display("Input Error", "One friend must be specified", sb);
        } else {
            counter++;
        }

        if (friend2.length() > 0)
            counter++;
        if (friend3.length() > 0)
            counter++;
        if (friend4.length() > 0)
            counter++;
        if (friend5.length() > 0)
            counter++;

        // place the usernames in an array
        String[] friends = new String[counter];
        int index = 0;
        friends[index] = friend1;

        // checking each optional parameter again, hope code passes monkey test
        if (friend2.length() > 0)
            friends[++index] = friend2;
        if (friend3.length() > 0)
            friends[++index] = friend3;
        if (friend4.length() > 0)
            friends[++index] = friend4;
        if (friend5.length() > 0)
            friends[++index] = friend5;

        for (int i = 0; i < counter; i++) {
            // check if error condition is false
            if (!error) {
                // query Users table for user id
                String sqlStatement = "SELECT id FROM user where username='" + friends[i] + "' LIMIT 1;";
                int friendID = 0;
                try {
                    ResultSet record = SQLHelper.executeResultSet(sqlStatement);
                    // get id
                    // 0 is not a valid id
                    if (record.next())
                        friendID = record.getInt("id");
                    else
                        error = true;
                }
                // generate error if user id is not valid and set error condition to true
                catch (Exception e) {
                    log.error(e.getMessage());
                    WebError.Display("Error", e.getMessage(), sb);
                    error = true;
                }

                // generate sql statement
                sqlStatement = "Insert INTO friends (id, friendid) values('" + userID + "','" + friendID + "');";
                //check error condition again, since an error could have been generated at this point
                if (!error) {
                    // create, placing method in try catch block
                    try {
                        int rowsAffected = SQLHelper.executeNonQuery(sqlStatement);

                        if (rowsAffected < 1)
                            WebError.Display("Error adding friend", "Unable to add friend.", sb);
                    }
                    // if exception, set error condition to true, display message
                    catch (Exception e1) {
                        log.error(e1.getMessage());
                        WebError.Display("Error", e1.getMessage(), sb);
                        error = true;
                    }
                }
            }

            try {
                String template = FileIO.ReadTextFile("/home/jj/docs/journal_template.inc");
                String loginMenu;
                StringBuilder content = new StringBuilder();

                content.append("\t\t<h2>Add Friend</h2>");
                content.append("\t\t<p>Your friend(s) have been added.</p>");

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
                log.error(e.getMessage());
                WebError.Display("Internal Error", "Error displaying page.", sb);
            }
        }
    }
}
