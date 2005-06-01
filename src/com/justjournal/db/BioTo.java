
package com.justjournal.db;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 3, 2004
 * Time: 11:21:22 PM
 * To change this template use Options | File Templates.
 */
public class BioTo
{
    private int userId;
    private String bio;

    public int getUserId()
    {
        return this.userId;
    }

    public void setUserId( int userId )
    {
        this.userId = userId;
    }

    public String getBio()
    {
        return this.bio;
    }

    public void setBio( String bio )
    {
        this.bio = bio;
    }

    public String toString()
    {
         StringBuffer output = new StringBuffer();

        output.append("user id: ");
        output.append(userId);
        output.append('\n');

        output.append("bio: ");
        output.append(bio);
        output.append('\n');

        return output.toString();
    }

}
