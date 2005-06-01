package com.justjournal.ctl;

/** Use this controller to protect content.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 * @see Gatekeeper
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 29, 2003
 * Time: 7:21:10 PM
 */
public class Protected extends Gatekeeper {

    // loginRequired page
    protected static final String LOGIN_REQUIRED_VIEW_NAME = "loginRequired";

    // destination setup: where do you want to go today?
    protected String dest;
    public String getDest()				{ return this.dest; }
    public void setDest(String value)	{ this.dest = value; }

    /** Kicks us out to the login required view.
     */
    protected final String outsidePerform() throws Exception
    {
        this.dest = this.getCtx().getRequest().getRequestURI();

        if (this.getCtx().getRequest().getQueryString() != null)
            this.dest += "?" + this.getCtx().getRequest().getQueryString();

        return LOGIN_REQUIRED_VIEW_NAME;
    }

    /**
     * This method should be overriden to perform application logic
     * which requires authentication.
     */
    protected String insidePerform() throws Exception
    {
        return SUCCESS;
    }

}