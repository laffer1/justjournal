package com.justjournal.ctl;

/**
 * User: laffer1
 * Date: Dec 28, 2005
 * Time: 2:03:33 PM
 */
public class DeleteLink extends Protected {

    public String getMyLogin() {
        return this.currentLoginName();
    }

    protected String insidePerform() throws Exception {
        return SUCCESS;
    }
}
