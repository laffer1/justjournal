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


import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Represent individual journal. This allows multiple journals to be associated with one login.
 *
 * @author Lucas Holt
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "journal")
public class Journal implements Serializable {
  @Serial
  @JsonIgnore private static final long serialVersionUID = 9106701690730308047L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "slug", length = 15, nullable = false)
  private String slug = "";

  @Column(name = "name", length = 150, nullable = true)
  private String name = "";

  @JsonBackReference
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "allow_spider")
  private boolean allowSpider = true;

  @ManyToOne
  @JoinColumn(name = "style")
  private Style style;

  @Column(name = "style", updatable = false, insertable = false)
  private int styleId;

  @Column(name = "owner_view_only", nullable = false, length = 1)
  private boolean ownerViewOnly = false;

  @Column(name = "ping_services", nullable = false, length = 1)
  private boolean pingServices = true;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "since", nullable = false)
  private Date since;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "modified", nullable = false)
  private Date modified;

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(final String slug) {
    this.slug = slug;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public boolean isAllowSpider() {
    return allowSpider;
  }

  public void setAllowSpider(final boolean allowSpider) {
    this.allowSpider = allowSpider;
  }

  public int getStyleId() {
    return this.styleId;
  }

  public void setStyleId(int styleId) {
    this.styleId = styleId;
  }

  public Style getStyle() {
    return style;
  }

  public void setStyle(final Style style) {
    this.style = style;
  }

  public boolean isOwnerViewOnly() {
    return ownerViewOnly;
  }

  public void setOwnerViewOnly(final boolean ownerViewOnly) {
    this.ownerViewOnly = ownerViewOnly;
  }

  public boolean isPingServices() {
    return pingServices;
  }

  public void setPingServices(final boolean pingServices) {
    this.pingServices = pingServices;
  }

  public Date getModified() {
    return modified;
  }

  public void setModified(final Date modified) {
    this.modified = modified;
  }

  public Date getSince() {
    return since;
  }

  public void setSince(final Date since) {
    this.since = since;
  }
}
