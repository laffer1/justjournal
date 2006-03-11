package com.justjournal.core;

import com.justjournal.utility.MailSender;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
//import javax.servlet.ServletContext;

/**
 * User: laffer1
 * Date: Mar 11, 2006
 * Time: 11:23:55 AM
 */
public class MailServletContextListener
        implements ServletContextListener {

    Thread m;

    public MailServletContextListener() {

    }

    public void contextInitialized(ServletContextEvent sce) {
        //ServletContext sc = sce.getServletContext();
        m = new MailSender();
        m.start();
        System.out.println("MailServletContextListener:" +
                "contextInitialized.");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        m.interrupt();
        System.out.println("MailServletContextListener:" +
                "contextDestroyed.");
    }
}
