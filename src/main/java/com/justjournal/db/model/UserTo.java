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

package com.justjournal.db.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justjournal.WebLogin;
import com.justjournal.utility.StringUtil;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user most basic properties.
 *
 * @author Lucas Holt
 * @version $Id: UserTo.java,v 1.10 2012/06/23 18:15:31 laffer1 Exp $ Date: Jan 21, 2004 Time: 12:20:53 PM
 *          <p/>
 *          TODO: add the rest of the properties.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "user")
public final class UserTo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;

    @NotNull
    @Column(name = "username", length = 15, nullable = false)
    private String userName = "";

    @NotNull
    @Column(name = "name", length = 20, nullable = false)
    private String name = "";

    @Column(name = "lastname", length = 20, nullable = true)
    private String lastName = "";

    @NotNull
    @Column(name = "password", length = 40, nullable = false)
    private String password = "";

    @NotNull
    @Column(name = "since", nullable = false)
    private Integer since = 2003;

    @Column(name = "lastlogin", nullable = true)
    private Date lastLogin = null;

    @NotNull
    @Column(name = "modified", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @NotNull
    @Column(name = "type", nullable = false)
    private Integer type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<EntryTo> entries = new HashSet<EntryTo>();

    @OneToOne(mappedBy="id")
    private Bio bio;

    @OneToOne(mappedBy="id")
    private UserContactTo userContactTo;

    @OneToOne(mappedBy="id")
    private UserPref userPref;

    @JsonCreator
    public UserTo() {
    }

    /**
     * First Name
     */
    public String getName() {
        return name;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(final Date modified) {
        this.modified = modified;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieve the user id.
     *
     * @return User id as an int >= 0
     */
    public final int getId() {
        return this.id;
    }

    /**
     * Set the user id.
     *
     * @param id user id
     */
    public final void setId(final int id) {
        this.id = id;
    }

    /**
     * Retrieve last login date as a <code>DateTimeBean</code>
     *
     * @return last login in a DateTimeBean
     * @see DateTimeBean
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(java.util.Date dt) {
        this.lastLogin = dt;
    }

    /**
     * Retrieve the user name.
     *
     * @return username
     */
    public final String getUserName() {
        return this.userName;
    }

    /**
     * Set the user name.
     *
     * @param userName account name
     */
    public final void setUserName(final String userName) {
        // TODO: move username max length to this class
        if (!StringUtil.lengthCheck(userName, 3, WebLogin.USERNAME_MAX_LENGTH)) {
            throw new IllegalArgumentException("Invalid userName " + userName);
        }
        this.userName = userName.toLowerCase();
    }

    /**
     * get the first name of the user.
     *
     * @return First Name of user
     */
    public final String getFirstName() {
        return this.getName();
    }

    /**
     * Set the first name of the user.
     *
     * @param name User's first name
     */
    public final void setName(final String name) {
        if (!StringUtil.lengthCheck(name, 2, 20)) {
            throw new IllegalArgumentException("Invalid name. Must be 2-20 characters");
        }
        this.name = name;
    }


    public final String getPassword() {
        return this.password;
    }

    public final void setPassword(final String password) {

        if (!StringUtil.lengthCheck(password, 5, 40)) {
            throw new IllegalArgumentException("Invalid password");
        }
        this.password = password;
    }

    public final int getSince() {
        return since;
    }

    public final void setSince(final int since) {
        this.since = since;
    }

    /**
     * A string representation of the user in the form field: value, nextfield: value ...
     * <p/>
     * Password fields are not returned by this method.
     *
     * @return Representation of some fields for debuging.
     */
    @JsonIgnore
    @Override
    public final String toString() {

        return "id: " + getId() + ", userName: " + userName + ", name: " + getName() + ", lastname: " + lastName + ", since: " + since + ", lastLogin:" + lastLogin;
    }

    @JsonIgnore
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserTo userTo = (UserTo) o;

        return getId() == userTo.getId() && !(getName() != null ? !getName().equals(userTo.getName()) : userTo.getName() != null) && !(getPassword() != null ? !getPassword().equals(userTo.getPassword()) : userTo.getPassword() != null) && userName.equals(userTo.userName);
    }

    @JsonIgnore
    @Override
    public final int hashCode() {
        int result;
        result = getId();
        result = 29 * result + userName.hashCode();
        result = 29 * result + (getName() != null ? getName().hashCode() : 0);
        result = 29 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }
}
