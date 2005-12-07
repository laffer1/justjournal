package com.justjournal.ctl;

/**
 * Displays a login form to the user so that they can login
 * to just journal prior to posting or viewing protected entries.
 * <p/>
 * The username is set in case they come back to this form.  Also
 * if the user is already logged in we want to display the current
 * username.  Its possible they have multiple accounts.
 * <p/>
 * Although passwords are accepted on the form, we should not save
 * them or display them here. Only the LoginSubmit class should
 * be used for this purpose.
 * 
 * User: laffer1
 * Date: Oct 24, 2005
 * Time: 4:45:10 PM
 */
public class Login extends ControllerAuth {

    protected String username;

    public String getMyLogin() {
        return this.currentLoginName();
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
