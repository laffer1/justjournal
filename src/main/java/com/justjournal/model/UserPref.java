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


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

/** @author Lucas Holt */
@Entity
@Table(name = "user_pref")
public class UserPref implements Serializable {

  private static final long serialVersionUID = -4301597963273403598L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id = 0;

  @Column(name = "show_avatar", nullable = false, length = 1)
  @Enumerated(EnumType.STRING)
  private PrefBool showAvatar = PrefBool.N;

  @Column(name = "modified", nullable = false)
  private Timestamp modified;

  @JsonBackReference
  @JsonIgnore
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public PrefBool getShowAvatar() {
    return showAvatar;
  }

  public void setShowAvatar(final PrefBool showAvatar) {
    this.showAvatar = showAvatar;
  }

  public Timestamp getModified() {
    return modified;
  }

  public void setModified(final Timestamp modified) {
    this.modified = modified;
  }
}
