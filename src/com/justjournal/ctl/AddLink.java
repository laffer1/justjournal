package com.justjournal.ctl;

import com.justjournal.db.UserLinkDao;
import com.justjournal.db.UserLinkTo;
import org.apache.log4j.Category;

/**
 * User: laffer1
 * Date: Dec 28, 2005
 * Time: 2:02:10 PM
 */
public class AddLink extends Protected {
    private static Category log = Category.getInstance(AddLink.class.getName());
    protected String title;
    protected String uri;

    public String getMyLogin() {
        return this.currentLoginName();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    protected String insidePerform() throws Exception {

        if (log.isDebugEnabled())
            log.debug("insidePerform(): Attempting to add link.");

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        if (!this.hasErrors()) {
            try {
                UserLinkDao dao = new UserLinkDao();
                UserLinkTo ul = new UserLinkTo();

                ul.setTitle(title);
                ul.setUri(uri);
                ul.setUserId(this.currentLoginId());

                if (!dao.add(ul)) ;
                addError("Add Link", "Error adding link.");
            }
            catch (Exception e) {
                addError("Add Link", "Could not add the link.");
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
