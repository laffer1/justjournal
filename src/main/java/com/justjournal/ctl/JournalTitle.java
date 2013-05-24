/*
Copyright (c) 2006, 2008 Lucas Holt
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

package com.justjournal.ctl;

import com.justjournal.JustJournalBaseServlet;
import com.justjournal.User;
import com.justjournal.WebError;
import com.justjournal.db.SQLHelper;
import com.justjournal.utility.FileIO;
import com.justjournal.utility.StringUtil;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Modify the journal title
 *
 * @author Lucas Holt
 * @version $Id: JournalTitle.java,v 1.5 2012/06/23 18:15:31 laffer1 Exp $
 */
public class JournalTitle extends JustJournalBaseServlet {

    private static final Logger log = Logger.getLogger(JournalTitle.class.getName());

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {
        boolean blnError = false;
        String jtitle = fixInput(request, "jtitle");

        // Retreive username
        String userName;
        userName = (String) session.getAttribute("auth.user");

        // Retreive user id
        Integer userIDasi = (Integer) session.getAttribute("auth.uid");
        // convert Integer to int type
        int userID = 0;
        if (userIDasi != null) {
            userID = userIDasi;
        }

        if (!StringUtil.lengthCheck(jtitle, 5, 150)) {
            blnError = true;
            WebError.Display("Input Error",
                    "Journal title must be 5-150 characters.",
                    sb);
        }

        if (!blnError) {
            try {
                if (log.isDebugEnabled())
                    log.debug("Attempting user prefs edit  ");

                if (userID > 0) {
                    String strSQL = "UPDATE user_pref SET journal_name='" +
                            StringUtil.replace(jtitle, '\'', "\\\'") + "' WHERE id='" + userID + "' LIMIT 1;";
                    if (SQLHelper.executeNonQuery(strSQL) == 1)
                        htmlOutput(sb, userName);
                    else
                        WebError.Display("Databaase error",
                                "Unable to edit the journal title.", sb);


                } else {
                    WebError.Display("Authentication error",
                            "Please make sure you are logged in correctly and try again.", sb);

                }
            } catch (Exception e3) {
                WebError.Display("Unknown Error",
                        "Unable to complete your request.  Try again later.",
                        sb);
            }
        }

        if (log.isDebugEnabled())
            log.debug("Write out Response  ");
    }

    private void htmlOutput(StringBuffer sb, String userName) {

        try {
            String template = FileIO.ReadTextFile("/home/jj/docs/journal_template.inc");
            String loginMenu;
            StringBuilder content = new StringBuilder();
            User user = new User(userName);

            content.append("\t\t<h2>Preferences</h2>");
            content.append(endl);
            content.append("\t<p>You are logged in as <a href=\"/users/").append(userName).append("\"><img src=\"/images/userclass_16.png\" alt=\"user\" />").append(userName).append("</a>.</p>");
            content.append(endl);
            content.append("\t<h3>Journal Title</h3>");
            content.append(endl);
            content.append("\t<p>The title has been updated.</p>");
            content.append(endl);
            content.append("\t<p><a href=\"/prefs/index.jsp\">Return to preferences.</a></p>");
            content.append(endl);

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
            WebError.Display("Internal Error", "Error dislaying page. " + e.getMessage(), sb);
        }
    }

    public String getServletInfo() {
        return "Journal Title Management";
    }
}
