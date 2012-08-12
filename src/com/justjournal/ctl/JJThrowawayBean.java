package com.justjournal.ctl;

import com.justjournal.core.Settings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * User: laffer1
 * Date: Dec 3, 2006
 * Time: 2:08:57 PM
 */
public class JJThrowawayBean {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private Settings settings;
    private StringBuffer sb;

    public JJThrowawayBean(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                           Settings settings, StringBuffer sb) {
        this.request = request;
        this.response = response;
        this.session = session;
        this.settings = settings;
        this.sb = sb;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

}
