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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;

/** @author Lucas Holt */
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalTo implements Serializable {
  @Serial
  @JsonIgnore private static final long serialVersionUID = 9106701690730308099L;

  private int id;

  private String slug = "";

  private String name = "";

  private boolean allowSpider = true;

  private int styleId;

  private boolean ownerViewOnly = false;

  private boolean pingServices = true;

  private Date since;

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

  @JsonCreator
  public JournalTo() {
    super();
  }
}
