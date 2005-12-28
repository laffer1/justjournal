package com.justjournal.ctl;

/**
 * User: laffer1
 * Date: Dec 28, 2005
 * Time: 2:03:54 PM
 */
public class ViewLink extends Protected {

    public String getMyLogin() {
        return this.currentLoginName();
    }

    protected String insidePerform() throws Exception {
        return SUCCESS;
    }
}
