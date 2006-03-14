package com.justjournal.ctl;

import com.justjournal.db.UserLinkDao;
import org.apache.log4j.Category;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: laffer1
 * Date: Dec 28, 2005
 * Time: 2:03:54 PM
 */
public class ViewLink extends Protected {
    private static Category log = Category.getInstance(ViewLink.class.getName());
    protected Collection links = new ArrayList(10);

    public Collection getLinks() {
        return links;
    }

    public String getMyLogin() {
        return this.currentLoginName();
    }

    protected String insidePerform() throws Exception {
        UserLinkDao dao = new UserLinkDao();

        if (log.isDebugEnabled())
            log.debug("insidePerform(): Attempting to view links");

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        if (!this.hasErrors()) {
            try {
                links = dao.view(this.currentLoginId());
            }
            catch (Exception e) {
                addError("View Links", "Could not retrieve links.");
                if (log.isDebugEnabled())
                    log.debug("insidePerform(): " + e.getMessage());
            }
        }

        if (this.hasErrors())
            return ERROR;
        else
            return SUCCESS;
    }
}
