package com.justjournal.ctl;

import com.justjournal.JustJournalBaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * User: laffer1
 * Date: Dec 3, 2006
 * Time: 12:40:20 PM
 */
public class JJController extends JustJournalBaseServlet {
    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {
        JJThrowawayBean jjb = new JJThrowawayBean(request, response, session, set, sb);
    }
}
