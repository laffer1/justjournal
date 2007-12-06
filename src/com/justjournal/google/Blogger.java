package com.justjournal.google;


import com.justjournal.db.UserTo;
import com.justjournal.db.EntryTo;
import com.justjournal.db.EntryDAO;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;

import com.justjournal.db.UserDao.*;
import com.justjournal.User;
import com.justjournal.WebError;
import com.justjournal.WebLogin;
import com.justjournal.utility.StringUtil;

/**
 * User: laffer1
 * Date: Dec 3, 2007
 * Time: 4:21:42 PM
 * $Id: Blogger.java,v 1.3 2007/12/06 06:02:33 laffer1 Exp $
 *
 * A blogger 1 compatible interface
 *
 */
public class Blogger {

    public HashMap getUsersInfo( String appkey, String username, String password)
    {
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
        }

        if (blnError)
        {
            s.clear();
            s.put("faultCode", 4);
            s.put("faultString", "User authentication failed: " + username );

        }


        return s;
    }

    public ArrayList getUsersBlogs( String appkey, String username, String password)
    {
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

            s.put("url",  "http://www.justjournal.com/users/" + user.getUserName());
            s.put("blogid", userId);
            s.put("blogName", user.getJournalName());

        } catch (Exception e) {
            blnError = true;
        }

        if (blnError)
        {
            s.clear();
            s.put("faultCode", 4);
            s.put("faultString", "User authentication failed: " + username );                   
        }

        a.add(s);

        return a;
    }



    public String newPost(String appkey, String blogid, String username, String password, String content, Boolean publish)
    {
        String result = "";
        int userId;
        boolean blnError = false;
        final EntryTo et = new EntryTo();


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
               // et.setDate(java.util.Date().Now());
             //   et.setSubject(StringUtil.replace(request.getParameter("subject"), '\'', "\\\'"));
                et.setBody(StringUtil.replace(content, '\'', "\\\'"));
                et.setAutoFormat(true);
                et.setAllowComments(true);
                et.setEmailComments(true);

            EntryDAO.add(et);
            result = "12345";

        } catch (Exception e) {
            blnError = true;
        }

        if (blnError)
        {
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
