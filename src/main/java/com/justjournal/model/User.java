/*
 * Copyright (c) 2003-2021 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.justjournal.model;

import static com.justjournal.core.Constants.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.justjournal.utility.StringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a user most basic properties.
 *
 * @author Lucas Holt
 * @version $Id: UserTo.java,v 1.10 2012/06/23 18:15:31 laffer1 Exp $ Date: Jan 21, 2004 Time:
 *     12:20:53 PM
 *     <p>TODO: add the rest of the properties.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "user")
public class User implements Serializable {

  @Serial
  @JsonIgnore private static final long serialVersionUID = -7545141063644043241L;

    @Getter
    @Setter
    @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id = 0;

    @Getter
    @Column(name = "username", length = 15, nullable = false)
  private String username = "";

    @Getter
    @Column(name = "name", length = 20, nullable = false)
  private String name = "";

  @Getter
  @Setter
  @Column(name = "lastname", length = 20)
  private String lastName = "";

  @Getter
  @JsonIgnore // don't show password in output
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "password", length = 40, nullable = false)
  private String password = "";

  @Setter
  @Getter
  @Column(name = "password_type", length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private PasswordType passwordType = null;

  @Getter
  @Setter
  @Column(name = "since", nullable = false)
  private Integer since = 2003;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "lastlogin")
  private Date lastLogin = null;

  @Setter
  @JsonIgnore
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "modified", nullable = false)
  private LocalDateTime modified;

  @Setter
  @Getter
  @Column(name = "type", nullable = false)
  private Integer type;

  @Getter
  @JsonManagedReference(value = "journal-user")
  @JsonIgnore
  @Basic(fetch = FetchType.LAZY)
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<Journal> journals = new HashSet<>();

  @Setter
  @Getter
  @JsonManagedReference(value = "entry-user")
  @JsonIgnore
  @Basic(fetch = FetchType.LAZY)
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<Entry> entries = new HashSet<>();

  @Setter
  @Getter
  @JsonManagedReference(value = "comment-user")
  @JsonIgnore
  @Basic(fetch = FetchType.LAZY)
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<Comment> comments = new HashSet<>();

  @Setter
  @Getter
  @JsonManagedReference
  @JsonIgnore
  @Basic(fetch = FetchType.LAZY)
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<Friend> friends = new HashSet<>();

  @Setter
  @Getter
  @JsonManagedReference
  @JsonIgnore
  @OneToMany(
      cascade = CascadeType.ALL,
      mappedBy = "user",
      fetch = FetchType.EAGER) // TODO: Lazy fetch type
  private Set<UserLink> links = new HashSet<>();

  @Setter
  @Getter
  @JsonManagedReference
  @JsonIgnore
  @Basic(fetch = FetchType.LAZY)
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<UserImage> images = new HashSet<>();

  @JsonManagedReference
  @JsonIgnore
  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private UserBio bio;

  @Getter
  @JsonManagedReference
  @JsonIgnore
  @Basic(fetch = FetchType.LAZY)
  @OneToOne(mappedBy = "user")
  private UserContact userContact;

  @Getter
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

  @Getter
  @JsonIgnore
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private Set<Role> roles = new HashSet<>();

  @JsonCreator
  public User() {}

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

  public void setUserLocation(UserLocation userLocation) {
    this.userLocation = userLocation;
  }

  public Set<Entry> getEntries() {
    return entries;
  }

  public void setEntries(Set<Entry> entries) {
    this.entries = entries;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  public UserBio getBio() {
    return bio;
  }

  public void setBio(UserBio bio) {
    this.bio = bio;
  }

  public void setUserContact(UserContact userContact) {
    this.userContact = userContact;
  }

  public UserPref getUserPref() {
    return userPref;
  }

  public void setUserPref(UserPref userPref) {
    this.userPref = userPref;
  }

    /**
   * Set the first name of the user.
   *
   * @param name User's first name
   */
  public void setName(String name) {
    if (!StringUtil.lengthCheck(name, 2, 20)) {
      throw new IllegalArgumentException("Invalid name. Must be 2-20 characters");
    }
    this.name = name;
  }

  public LocalDateTime getModified() {
    return modified;
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
   * Set the user name.
   *
   * @param username account name
   */
  public void setUsername(String username) {
    // TODO: move username max length to this class
    if (!StringUtil.lengthCheck(username, USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH)) {
      throw new IllegalArgumentException("Invalid username " + username);
    }
    this.username = username.toLowerCase();
  }

  /**
   * get the first name of the user.
   *
   * @return First Name of user
   */
  public String getFirstName() {
    return this.getName();
  }

    public void setPassword(String password) {

    if (!StringUtil.lengthCheck(password, 5, 64)) {
      throw new IllegalArgumentException("Invalid password");
    }
    this.password = password;
  }

  public Set<Friend> getFriends() {
    return friends;
  }

  public void setFriends(Set<Friend> friends) {
    this.friends = friends;
  }

  public Set<UserLink> getLinks() {
    return links;
  }

  public void setLinks(Set<UserLink> links) {
    this.links = links;
  }

  public Set<UserImage> getImages() {
    return images;
  }

  public void setImages(Set<UserImage> images) {
    this.images = images;
  }
    public void setJournals(Set<Journal> journals) {
    this.journals = journals;
  }

    public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}
