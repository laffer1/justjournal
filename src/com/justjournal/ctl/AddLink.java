package com.justjournal.ctl;

/**
 * User: laffer1
 * Date: Dec 28, 2005
 * Time: 2:02:10 PM
 */
public class AddLink extends Protected {

    public String getMyLogin() {
        return this.currentLoginName();
    }

    protected String insidePerform() throws Exception {
        return SUCCESS;
    }
}
