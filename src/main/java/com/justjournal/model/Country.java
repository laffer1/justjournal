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


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;

/**
 * ISO Country Codes
 *
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "country")
public class Country implements Serializable {

  @Serial
  private static final long serialVersionUID = 5582600657460126447L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "title", length = 80, nullable = false)
  private String title;

  @Column(name = "iso", length = 2, nullable = false)
  private String iso;

  @Column(name = "iso3", length = 3, nullable = true)
  private String iso3;

  @Column(name = "numcode", nullable = true)
  private Integer numCode;

  @Column(name = "iso_title", length = 50, nullable = false)
  private String isoTitle;

  @OneToMany(mappedBy = "country")
  private List<UserLocation> userLocation;

  @OneToMany(mappedBy = "country")
  private List<State> state;

  @JsonCreator
  public Country() {}

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getIso() {
    return iso;
  }

  public void setIso(final String iso) {
    this.iso = iso;
  }

  public String getIso3() {
    return iso3;
  }

  public void setIso3(final String iso3) {
    this.iso3 = iso3;
  }

  public int getNumCode() {
    return numCode;
  }

  public void setNumCode(final int numCode) {
    this.numCode = numCode;
  }

  public String getIsoTitle() {
    return isoTitle;
  }

  public void setIsoTitle(final String isoTitle) {
    this.isoTitle = isoTitle;
  }

  public List<UserLocation> getUserLocation() {
    return userLocation;
  }

  public void setUserLocation(final List<UserLocation> userLocation) {
    this.userLocation = userLocation;
  }
}
