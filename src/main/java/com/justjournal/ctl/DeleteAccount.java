package com.justjournal.ctl;

import com.justjournal.WebError;
import com.justjournal.db.SQLHelper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Delete/cancel user account
 *
 * @author Lucas Holt
 * @version $Id: DeleteAccount.java,v 1.2 2009/05/16 03:13:12 laffer1 Exp $
 */
public class DeleteAccount extends JustJournalBaseServlet {
    private static final Logger log = Logger.getLogger(DeleteAccount.class.getName());


    public String getServletInfo() {
        return "Delete JustJournal Account";
    }

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {
        boolean blnError = false;

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

        /* Make sure we are logged in */
        if (userID < 1) {
            WebError.Display("Error", "You must be logged in to cancel your account.", sb);
            return;
        }

        try {
            SQLHelper.executeNonQuery("DELETE FROM comments WHERE uid=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM entry WHERE uid=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM favorites WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM friends WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM friends_lj WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM rss_subscriptions WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_bio WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_contact WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_files WHERE ownerid=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_images WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_images_album WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_images_album_map WHERE owner=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_link WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_location WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_pic WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_pref WHERE id=" + userID + ";");
            SQLHelper.executeNonQuery("DELETE FROM user_style WHERE id=" + userID + ";");
        } catch (Exception e) {
            blnError = true;
            log.error("Error exeucting sql (delete account): " + e.getMessage());
        }

        sb.append("<html><head><title>Cancel Account</title></head><body>");
        if (blnError) {
            sb.append("<h2>Error deleting account: " + userName + "</h2>");
        } else {
            sb.append("<h2>Account " + userName + " deleted.</h2>");
        }
        sb.append("</body></html>");
    }

}
