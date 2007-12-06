/*
Copyright (c) 2007, Lucas Holt
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
 * $Id: Blogger.java,v 1.6 2007/12/06 19:43:36 laffer1 Exp $
 * <p/>
 * A blogger 1 compatible interface exposed by XML-RPC
 * <p/>
 * http://www.blogger.com/developers/api/1_docs/
 * <p/>
 * Methods defined in Blogger 1.0 API:
 * blogger.newPost: Makes a new post to a designated blog. Optionally, will publish the blog after making the post.
 * blogger.editPost: Edits a given post. Optionally, will publish the blog after making the edit.
 * blogger.getUsersBlogs: Returns information on all the blogs a given user is a member of.
 * blogger.getUserInfo: Authenticates a user and returns basic user info (name, email, userid, etc.)
 * blogger.getTemplate: Returns the main or archive index template of a given blog.
 * blogger.setTemplate: Edits the main or archive index template of a given blog.
 */
public class Blogger {

    private static Category log = Category.getInstance(Blogger.class.getName());

    /**
     * Fetch the users personal information including their username, userid,
     * email address and name.
     * <p/>
     * The Blogger API defines nickname, userid, url, email, firstname and lastname here.  We don't track
     * last name.
     *
     * @param appkey   Not used but required by Blogger API
     * @param username username to authenticate/check
     * @param password the account secret
     * @return A HashMap of the users personal info or an error code as a hashmap
     */
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


    /**
     * Fetch the users blogs.  JJ only supports 1 blog per user right now.  Still this must be a list
     * of all blogs.
     * <p/>
     * The Blogger API defines url, blogid and blogName.
     *
     * @param appkey   Not used but required by Blogger API
     * @param username username to authenticate/check
     * @param password the account secret
     * @return A list of hash maps for each blog
     */
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


    /**
     * Create a new blog entry using the limited feautres of the blogger api.
     *
     * @param appkey   Ignored but required
     * @param blogid   Ignored since JJ supports 1 blog per user.  Just the UID anyway.
     * @param username Username of blog owner.
     * @param password password of blog owner.
     * @param content  The body/content of the blog (no subjects)
     * @param publish  A boolean representing the publish state of the entry.  JJ does not support drafts yet.
     * @return The entry id of the entry as a string or an error.
     */
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

    /**
     * Modify a blog entry using the limited feautres of the blogger api.
     * <p/>
     * TODO: make this work
     *
     * @param appkey   Ignored but required
     * @param postid   entry id as a string
     * @param username Username of blog owner.
     * @param password password of blog owner.
     * @param content  The body/content of the blog (no subjects)
     * @param publish  A boolean representing the publish state of the entry.  JJ does not support drafts yet.
     * @return The entry id of the entry as a string or an error.
     */
    public String editPost(String appkey, String postid, String username, String password, String content, Boolean publish) {
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
