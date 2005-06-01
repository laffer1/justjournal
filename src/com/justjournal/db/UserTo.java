
package com.justjournal.db;

/**
 * Represents a user most basic properties.
 *
 * @author Lucas Holt
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 21, 2004
 * Time: 12:20:53 PM
 *
 * TODO: add the rest of the properties.
 */
public class UserTo
{
    private int id;          // the user id imposed by mysql
    private String userName;
    private String name;     // first name
    private String password;
    private String passwordSha1;


    /**
     * Retrieve the user id.
     * @return
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Set the user id.
     * @param id
     */
    public void setId( final int id )
    {
        this.id = id;
    }

    /**
     * Retrieve the user name.
     * @return
     */
    public String getUserName()
    {
        return this.userName;
    }

    /**
     * Set the user name.
     * @param userName
     */
    public void setUserName( final String userName )
    {
        this.userName = userName;
    }

    /**
     * get the first name of the user.
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Set the first name of the user.
     * @param name
     */
    public void setName( final String name )
    {
        this.name = name;
    }

    /**
     * Retrieve clear text password.
     * Used to set passwords in the database.
     * Passwords are not permenently stored
     * clear text.
     * @return
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * Set clear text password.
     * Used to set passwords, but they are
     * not stored perminently in clear
     * text password.
     * @param password
     */
    public void setPassword( final String password )
    {
        this.password = password;
    }

    /**
     * Retrieve SHA1 password.
     * @return
     */
    public String getPasswordSha1()
    {
        return this.passwordSha1;
    }

    /**
     * Set SHA1 password.
     * @param passwordSha1
     */
    public void setPasswordSha1( final String passwordSha1 )
    {
        this.passwordSha1 = passwordSha1;
    }

    /**
     * A string representation of the user
     * in the form
     * field: value, nextfield: value ...
     *
     * Password fields are not returned
     * by this method.
     *
     * @return
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("id: ");
        sb.append(id);
        sb.append(", userName: ");
        sb.append(name);
        sb.append(", name: ");
        sb.append(name);

        return sb.toString();
    }

}
