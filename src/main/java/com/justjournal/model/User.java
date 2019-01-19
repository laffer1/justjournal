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
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.justjournal.Login;
import com.justjournal.utility.StringUtil;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
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
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "user")
public class User implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = -7545141063644043241L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;

    @Column(name = "username", length = 15, nullable = false)
    private String username = "";
    
    @Column(name = "name", length = 20, nullable = false)
    private String name = "";

    @Column(name = "lastname", length = 20, nullable = true)
    private String lastName = "";

    @JsonIgnore // don't show password in output
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "password", length = 40, nullable = false)
    private String password = "";

    @Column(name = "password_type", length = 10)
    @Enumerated(EnumType.STRING)
    private PasswordType passwordType = null;

    @Column(name = "since", nullable = false)
    private Integer since = 2003;

    @Column(name = "lastlogin", nullable = true)
    private Date lastLogin = null;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "modified", nullable = false)
    private Timestamp modified;

    @Column(name = "type", nullable = false)
    private Integer type;

    @JsonManagedReference(value = "journal-user")
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Journal> journals = new HashSet<>();

    @JsonManagedReference(value="entry-user")
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Entry> entries = new HashSet<>();

    @JsonManagedReference(value="comment-user")
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments = new HashSet<>();

    @JsonManagedReference
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Friend> friends = new HashSet<>();

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER) // TODO: Lazy fetch type
    private Set<UserLink> links = new HashSet<>();

    @JsonManagedReference
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<UserImage> images = new HashSet<>();

    @JsonManagedReference
    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserBio bio;

    @JsonManagedReference
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @OneToOne(mappedBy = "user")
    private UserContact userContact;

    @JsonManagedReference
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @OneToOne(mappedBy = "user")
    private UserPref userPref;

    @JsonManagedReference
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @OneToOne(mappedBy = "user")
    private UserLocation userLocation;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    @JsonCreator
    public User() {
    }

    public User(User user) {
    		super();
    		this.id = user.getId();
    		this.name = user.getName();
    		this.username = user.getUsername();
    		this.password = user.getPassword();
    		this.passwordType = user.getPasswordType();
    		this.roles = user.getRoles();
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(final Set<Comment> comments) {
        this.comments = comments;
    }

    public UserBio getBio() {
        return bio;
    }

    public void setBio(final UserBio bio) {
        this.bio = bio;
    }

    public UserContact getUserContact() {
        return userContact;
    }

    public void setUserContact(final UserContact userContact) {
        this.userContact = userContact;
    }

    public UserPref getUserPref() {
        return userPref;
    }

    public void setUserPref(final UserPref userPref) {
        this.userPref = userPref;
    }

    public Integer getSince() {
        return since;
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

    public void setModified(final Timestamp modified) {
        this.modified = modified;
    }

    public Integer getType() {
        return type;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
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

    public void setLastLogin(final java.util.Date dt) {
        this.lastLogin = dt;
    }

    /**
     * Retrieve the user name.
     *
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the user name.
     *
     * @param username account name
     */
    public final void setUsername(final String username) {
        // TODO: move username max length to this class
        if (!StringUtil.lengthCheck(username, 3, Login.USERNAME_MAX_LENGTH)) {
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

        if (!StringUtil.lengthCheck(password, 5, 64)) {
            throw new IllegalArgumentException("Invalid password");
        }
        this.password = password;
    }

    public final PasswordType getPasswordType() {
        return this.passwordType;
    }

    public final void setPasswordType(final PasswordType passwordType) {
        this.passwordType = passwordType;
    }

    public Set<Friend> getFriends() {
        return friends;
    }

    public void setFriends(final Set<Friend> friends) {
        this.friends = friends;
    }

    public Set<UserLink> getLinks() {
        return links;
    }

    public void setLinks(final Set<UserLink> links) {
        this.links = links;
    }

    public Set<UserImage> getImages() {
        return images;
    }

    public void setImages(final Set<UserImage> images) {
        this.images = images;
    }

    public Set<Journal> getJournals() {
        return journals;
    }

    public void setJournals(final Set<Journal> journals) {
        this.journals = journals;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }
}
