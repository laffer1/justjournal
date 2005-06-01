package com.justjournal.ctl;

import com.justjournal.webLogin;

/** Controller provides framework for authenticated services.
 *  The parent is a bean based controller using this as its
 *  bean unless otherwise specified.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 * @see ControllerErrorable
 */
public class ControllerAuth extends ControllerErrorable {
    /**
     * The name of the session attribute which stores the login name.
     */
    protected static final String LOGIN_ATTRNAME = "auth.user";
    protected static final String LOGIN_ATTRID = "auth.uid";

    /**
     * @return the login of the user currently logged in, or null if no
     *	user is logged in.
     */
    protected String currentLoginName() {
        return (String) this.getCtx().getRequest().getSession().getAttribute(LOGIN_ATTRNAME);
    }

    protected int currentLoginId() {
        int aUserID = 0;
        Integer userIDasi = (Integer) this.getCtx().getRequest().getSession().getAttribute(LOGIN_ATTRID);

        if (userIDasi != null) {
            aUserID = userIDasi.intValue();
        }

        return aUserID;
    }

    /**
     * Try to log in with the specified credentials.
     * @return true if it worked, false if the credentials are bad.
     */
    protected boolean login(String userName, String password) {

        int userId = webLogin.validate(userName, password);


        if (userId < 1) {
            return false;
        } else {
            this.getCtx().getRequest().getSession().setAttribute(LOGIN_ATTRNAME, userName);
            this.getCtx().getRequest().getSession().setAttribute(LOGIN_ATTRID, new Integer(userId));
            return true;
        }
    }

    /** Disable the login credentials in the session.
     *  username and id are removed.
     */
    protected void logout() {
        this.getCtx().getRequest().getSession().removeAttribute(LOGIN_ATTRNAME);
        this.getCtx().getRequest().getSession().removeAttribute(LOGIN_ATTRID);
    }

    /** Determines if the user is logged in during this session.
     */
    protected boolean isLoggedIn() {
        return this.getCtx().getRequest().getSession().getAttribute(LOGIN_ATTRNAME) != null;
    }

}
