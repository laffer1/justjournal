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

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Emoticon resource data transfer object.  Basic properties including the filename, height, width, mood and theme ids.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0 User: laffer1 Date: Sep 22, 2003 Time: 11:01:45 PM
 */
@Entity
@Table(name = "mood_theme_data")
public class MoodThemeData implements Serializable {
    private static final long serialVersionUID = 8664963658480263224L;

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "picurl", length = 100)
    private String filename;

    @ManyToOne
    @JoinColumn(name = "moodid")
    private Mood mood;

    @ManyToOne
    @JoinColumn(name = "moodthemeid")
    private MoodTheme theme;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @JsonCreator
    public MoodThemeData() {

    }

    public final int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public final String getFileName() {
        return filename;
    }

    public final void setFileName(String value) {
        filename = value;
    }

    public final MoodTheme getTheme() {
        return theme;
    }

    public final void setTheme(MoodTheme value) {
        theme = value;
    }

    public final int getWidth() {
        return width;
    }

    public final void setWidth(int value) {
        width = value;
    }

    public final int getHeight() {
        return height;
    }

    public final void setHeight(int value) {
        height = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(final Mood mood) {
        this.mood = mood;
    }
}
