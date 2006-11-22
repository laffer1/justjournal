package com.justjournal.ctl;

import com.justjournal.JustJournalBaseServlet;
import com.justjournal.WebError;
import com.justjournal.User;
import com.justjournal.db.SQLHelper;
import com.justjournal.utility.StringUtil;
import com.justjournal.utility.FileIO;
import org.apache.log4j.Category;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Modify the journal title
 *
 * @author Lucas Holt
 * @version $id$
 */
public class JournalTitle extends JustJournalBaseServlet {

    private static Category log = Category.getInstance(JournalTitle.class.getName());

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
            userID = userIDasi.intValue();
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
                    log.debug("Attempting user prefs update  ");

                if (userID > 0) {
                    String strSQL = "UPDATE user_pref SET journal_name='" +
                            StringUtil.replace(jtitle, '\'', "\\\'") + "' WHERE id='" + userID + "' LIMIT 1;";
                    if (SQLHelper.executeNonQuery(strSQL) == 1)
                        htmlOutput(sb, userName);
                    else
                        WebError.Display("Databaase error",
                                "Unable to update the journal title.", sb);


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
