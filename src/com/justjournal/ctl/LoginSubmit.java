package com.justjournal.ctl;

/**
 * User: laffer1
 * Date: Oct 24, 2005
 * Time: 4:59:18 PM
 */
public class LoginSubmit extends ControllerAuth {
    protected String username;
    protected String password;
    protected String passwordHash;

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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPaswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }
}
