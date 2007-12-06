package com.justjournal.google;


import com.justjournal.User;
import com.justjournal.WebLogin;
import com.justjournal.db.DateTime;
import com.justjournal.db.DateTimeBean;
import com.justjournal.db.EntryDAO;
import com.justjournal.db.EntryTo;
import com.justjournal.utility.StringUtil;
import org.apache.log4j.Category;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: laffer1
 * Date: Dec 3, 2007
 * Time: 4:21:42 PM
 * $Id: Blogger.java,v 1.4 2007/12/06 19:20:40 laffer1 Exp $
 * <p/>
 * A blogger 1 compatible interface exposed by XML-RPC
 */
public class Blogger {

    private static Category log = Category.getInstance(Blogger.class.getName());

    public HashMap getUsersInfo(String appkey, String username, String password) {
        int userId;
        boolean blnError = false;
        HashMap s = new HashMap();


        if (!StringUtil.lengthCheck(username, 3, 15)) {
            blnError = true;
        }

        if (!StringUtil.lengthCheck(password, 5, 18)) {
            blnError = true;
        }

        userId = WebLogin.validate(username, password);

        try {
            User user = new User(userId);

            s.put("nickname", user.getUserName());
            s.put("userid", userId);
            s.put("url", "http://www.justjournal.com/users/" + user.getUserName());
            s.put("email", user.getEmailAddress());
            s.put("firstname", user.getFirstName());
        } catch (Exception e) {
            blnError = true;
            log.debug(e.getMessage());
        }

        if (blnError) {
            s.clear();
            s.put("faultCode", 4);
            s.put("faultString", "User authentication failed: " + username);
        }


        return s;
    }

    public ArrayList getUsersBlogs(String appkey, String username, String password) {
        int userId;
        boolean blnError = false;
        ArrayList a = new ArrayList();
        HashMap s = new HashMap();


        if (!StringUtil.lengthCheck(username, 3, 15)) {
            blnError = true;
        }

        if (!StringUtil.lengthCheck(password, 5, 18)) {
            blnError = true;
        }

        userId = WebLogin.validate(username, password);

        try {
            User user = new User(userId);

            s.put("url", "http://www.justjournal.com/users/" + user.getUserName());
            s.put("blogid", userId);
            s.put("blogName", user.getJournalName());
        } catch (Exception e) {
            blnError = true;
            log.debug(e.getMessage());
        }

        if (blnError) {
            s.clear();
            s.put("faultCode", 4);
            s.put("faultString", "User authentication failed: " + username);
        }

        a.add(s);

        return a;
    }


    public String newPost(String appkey, String blogid, String username, String password, String content, Boolean publish) {
        String result = "";
        int userId;
        boolean blnError = false;
        final EntryTo et = new EntryTo();
        EntryTo et2;

        if (!StringUtil.lengthCheck(username, 3, 15)) {
            blnError = true;
        }

        if (!StringUtil.lengthCheck(password, 5, 18)) {
            blnError = true;
        }

        userId = WebLogin.validate(username, password);

        try {
            User user = new User(userId);
            et.setUserId(userId);
            DateTime d = new DateTimeBean();
            d.set(new java.util.Date());
            et.setDate(d);
            //   et.setSubject(StringUtil.replace(request.getParameter("subject"), '\'', "\\\'"));
            et.setBody(StringUtil.replace(content, '\'', "\\\'"));
            //et.setMusic(StringUtil.replace(music, '\'', "\\\'"));
            et.setSecurityLevel(2);   // public
            et.setLocationId(0); // not specified
            et.setMoodId(12);    // not specified
            et.setAutoFormat(true);
            et.setAllowComments(true);
            et.setEmailComments(true);
            et.setUserId(userId);
            et.setUserName(user.getUserName());

            EntryDAO.add(et);
            et2 = EntryDAO.viewSingle(et);
            result = Integer.toString(et2.getId());

        } catch (Exception e) {
            blnError = true;
            log.debug(e.getMessage());
        }

        if (blnError) {
            result = "<struct>\n";

            result += "<member>\n";
            result += "<name>" + "faultCode" + "</name>\n";
            result += "<value><int>4</int>" + "</value>\n";
            result += "</member>\n";

            result += "<member>\n";
            result += "<name>" + "faultString" + "</name>\n";
            result += "<value><string>" + "User authentication failed: " + username + "</string></value>\n";
            result += "</member>\n";

            result += "</struct>\n";

        }

        return result;
    }
}
