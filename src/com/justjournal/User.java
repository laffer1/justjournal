/*
Copyright (c) 2005, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package com.justjournal;

/**
 * Represents a user's basic credentals including userId and
 * userName.
 *
 * @author Lucas Holt
 * @version 1.0
 *          Date: Jan 4, 2004
 *          Time: 9:59:35 PM
 * @since 1.0
 */
public final class User {
    private String userName = "";
    private int userId = 0;
    private int type = 0;
    private String firstName = ""; // real name
    private int since = 2003;  // starting year for account
    private Preferences preferences = null;

    /**
     * Retrieve the username associated with this user.
     *
     * @return the username
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Set the username associated with this user. Maximum size
     * is 15 characters.
     *
     * @param userName The username associated with this account.
     */
    public void setUserName(final String userName) {
        if (userName == null)
            throw new IllegalArgumentException("username can not be null.");

        if (userName.length() > 15)
            throw new IllegalArgumentException("username can not be longer than 15 characters");

        if (userName.length() < 3)
            throw new IllegalArgumentException("username must be at least 3 characters.");

        this.userName = userName;
    }

    /**
     * Get the user id associated with this account.  The user id
     * is the unique identifier. It is an int where n >= 0.
     *
     * @return unique userid used with this account.
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Set the unique user id associated with this user account.
     * This unique identifier is an int where n >= 0.
     * @param userId A unique user id to set for the account.
     */
    public void setUserId(final int userId) {
        if (userId < 0)
            throw new IllegalArgumentException("user id must be greater than or equal to zero.");

        this.userId = userId;
    }

    /**
     * Sets the unique user id associated with this user account.
     * This version takes an integer version of the user id.
     *
     * @param userId Integer user id where n >= 0
     */
    public void setUserId(final Integer userId) {
        if (userId == null)
            throw new IllegalArgumentException("user id can not be null.");

        setUserId(userId.intValue());
    }

    /**
     * Retrieve the type of user account.  Default account type
     * is 0.  type will always be >= 0
     *
     * @return User account type.
     */
    public int getType()
    {
        return this.type;
    }

    /**
     * Set the type of user account as an int representation
     * greater than or equal to zero.
     *
     * @param type Account type >= 0
     */
    public void setType(final int type)
    {
        this.type = type;
    }

    /**
     * Get the users first name as specified during
     * the signup process. Name can not be longer than
     * 20 characters.
     *
     * @return user's first name
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * Set the first name of the user.  Name length is limited
     * to 20 characters.
     * @param firstName user's first name.
     */
    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Retrieve the year when this user account was
     * created.
     *
     * n >= 2003
     *
     * @return  Year account was created.
     */
    public int getSince()
    {
        return this.since;
    }

    /**
     * Set the year when the user account was created.
     *
     * n >= 2003
     *
     * @param since Year account was created.
     */
    public void setSince(final int since)
    {
        this.since = since;
    }

    /**
     * Retrieve a copy of the preferences for the user.
     *
     * @return User Preferences
     */
    public Preferences getPreferences()
    {
        try {
        if (preferences == null)
            preferences = new Preferences(userName);
        } catch (Exception e) {
            preferences = null;
        }

        return preferences;
    }

    public String toString() {
        return Integer.toString(userId) + "," + userName + "," + type +
                "," + firstName + "," + since;
    }
}
