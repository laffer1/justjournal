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

package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justjournal.WebLogin;
import com.justjournal.utility.StringUtil;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

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
public class User implements Serializable {

    private static final long serialVersionUID = -7545141063644043241L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;

    @NotNull
    @Column(name = "username", length = 15, nullable = false)
    private String username = "";

    @NotNull
    @Column(name = "name", length = 20, nullable = false)
    private String name = "";

    @Column(name = "lastname", length = 20, nullable = true)
    private String lastName = "";

    @JsonIgnore // don't show password in output
    @Basic(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "password", length = 40, nullable = false)
    private String password = "";

    @Basic(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "since", nullable = false)
    private Integer since = 2003;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "lastlogin", nullable = true)
    private Date lastLogin = null;

    @Basic(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "modified", nullable = false)
    private Timestamp modified;

    @Basic(fetch = FetchType.LAZY)
    @NotNull
    @Column(name = "type", nullable = false)
    private Integer type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Entry> entries = new HashSet<Entry>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Friend> friends = new ArrayList<Friend>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<UserLink> links = new HashSet<UserLink>();

    @OneToOne(mappedBy = "user")
    private UserBio bio;

    @OneToOne(mappedBy = "user")
    private UserContact userContactTo;

    @OneToOne(mappedBy = "user")
    private UserPref userPref;

    @OneToOne(mappedBy = "user")
    private UserLocation userLocation;

    @JsonCreator
    public User() {
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(final UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public Set<Entry> getEntries() {
        return entries;
    }

    public void setEntries(final Set<Entry> entries) {
        this.entries = entries;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(final List<Comment> comments) {
        this.comments = comments;
    }

    public UserBio getBio() {
        return bio;
    }

    public void setBio(final UserBio bio) {
        this.bio = bio;
    }

    public UserContact getUserContactTo() {
        return userContactTo;
    }

    public void setUserContactTo(final UserContact userContactTo) {
        this.userContactTo = userContactTo;
    }

    public UserPref getUserPref() {
        return userPref;
    }

    public void setUserPref(final UserPref userPref) {
        this.userPref = userPref;
    }

    public void setSince(final Integer since) {
        this.since = since;
    }

    public void setType(final Integer type) {
        this.type = type;
    }

    /**
     * First Name
     */
    public String getName() {
        return name;
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

    public Date getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
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
    public final String getUsername() {
        return this.username;
    }

    /**
     * Set the user name.
     *
     * @param username account name
     */
    public final void setUsername(final String username) {
        // TODO: move username max length to this class
        if (!StringUtil.lengthCheck(username, 3, WebLogin.USERNAME_MAX_LENGTH)) {
            throw new IllegalArgumentException("Invalid username " + username);
        }
        this.username = username.toLowerCase();
    }

    /**
     * get the first name of the user.
     *
     * @return First Name of user
     */
    public final String getFirstName() {
        return this.getName();
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

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(final List<Friend> friends) {
        this.friends = friends;
    }

    public Set<UserLink> getLinks() {
        return links;
    }

    public void setLinks(final Set<UserLink> links) {
        this.links = links;
    }
}
