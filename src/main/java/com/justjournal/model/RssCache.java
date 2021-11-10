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


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * Rss file record.
 *
 * @author Lucas Holt
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "rss_cache")
public final class RssCache implements Serializable {

  private static final long serialVersionUID = 7699995609479936367L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  // reserved keyword
  @Column(name = "\"interval\"", columnDefinition = "tinyint default 24")
  private int interval = 24;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "lastupdated")
  private Date lastUpdated;

  @Column(name = "uri", nullable = false, length = 255, columnDefinition = "tinytext")
  private String uri;

  @Column(name = "content", nullable = false, length = 16777215, columnDefinition = "MEDIUMTEXT")
  private String content;

  @Column(name = "active", columnDefinition = "boolean default true")
  private Boolean active;

  @Column(name = "error_count", columnDefinition = "int default 0", nullable = false)
  private int errorCount;

  @JsonCreator
  public RssCache() {
    super();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getInterval() {
    return interval;
  }

  public void setInterval(int interval) {
    this.interval = interval;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(final Boolean active) {
    this.active = active;
  }

  public int getErrorCount() {
    return errorCount;
  }

  public void setErrorCount(final int errorCount) {
    this.errorCount = errorCount;
  }
}
