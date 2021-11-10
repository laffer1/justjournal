/*
Copyright (c) 2003-2021, Lucas Holt
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


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * Track the number of users, entry and comment statistics, and other information.
 *
 * @author Lucas Holt
 */
@EqualsAndHashCode
@Component
public final class StatisticsImpl implements Statistics {

  private long users = 0;
  private long entries = 0;
  private long publicEntries = 0;
  private long friendsEntries = 0;
  private long privateEntries = 0;
  private long comments = 0;
  private long styles = 0;
  private long tags = 0;

  @JsonCreator
  public StatisticsImpl() {
    super();
  }

  public long getPublicEntries() {
    return publicEntries;
  }

  public void setPublicEntries(final long publicEntries) {
    this.publicEntries = publicEntries;
  }

  public long getFriendsEntries() {
    return friendsEntries;
  }

  public void setFriendsEntries(final long friendsEntries) {
    this.friendsEntries = friendsEntries;
  }

  public long getPrivateEntries() {
    return privateEntries;
  }

  public void setPrivateEntries(final long privateEntries) {
    this.privateEntries = privateEntries;
  }

  public void setUsers(final long users) {
    this.users = users;
  }

  public void setEntries(final long entries) {
    this.entries = entries;
  }

  public void setComments(final long comments) {
    this.comments = comments;
  }

  public void setStyles(final long styles) {
    this.styles = styles;
  }

  public void setTags(final long tags) {
    this.tags = tags;
  }

  public long getUsers() {
    return users;
  }

  public long getEntries() {
    return entries;
  }

  public long getComments() {
    return comments;
  }

  public long getStyles() {
    return styles;
  }

  public long getTags() {
    return tags;
  }
}
