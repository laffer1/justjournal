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
package com.justjournal.model.api;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

/** @author Lucas Holt */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserLinkTo implements Serializable, Comparable<UserLinkTo> {

  @JsonIgnore private static final long serialVersionUID = -132658312201527378L;

  private int id;

  private String title;

  private String uri;

  @JsonIgnore private int userId;

  @JsonCreator
  public UserLinkTo() {
    super();
  }

  /**
   * Retrieve unique identifier for link
   *
   * @return int > 0
   */
  public final int getId() {
    return id;
  }

  /**
   * Set unique identifier for link
   *
   * @param id > 0
   */
  public void setId(int id) {
    if (id > 0) this.id = id;
    else throw new IllegalArgumentException("id must be greater than zero");
  }

  /**
   * Retrieve the link title
   *
   * @return title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set the link title so that it can be displayed instead of just the hyperlink itself.
   *
   * @param title ascii text
   */
  public void setTitle(String title) {
    if (title != null) this.title = title;
    else throw new IllegalArgumentException("title cannot be null");
  }

  /**
   * Retrieve the address of the link which should be a complete URI
   *
   * @return uri string
   */
  public String getUri() {
    return uri;
  }

  /**
   * Set the Uniform resource identifier as a string
   *
   * @param uri a valid uri
   */
  public void setUri(String uri) {
    if (uri != null) this.uri = uri;
    else throw new IllegalArgumentException("uri cannot be null");
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(final int userId) {
    this.userId = userId;
  }

  public int compareTo(UserLinkTo ul) {
    return this.getTitle().compareTo(ul.getTitle());
  }
}
