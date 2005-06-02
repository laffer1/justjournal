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

package com.justjournal.db;

/**
 * Emoticon resource data transfer object.  Basic properites
 * including the filename, height, width, mood and theme ids.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 *        User: laffer1
 *        Date: Sep 22, 2003
 *        Time: 11:01:45 PM
 */
public class EmoticonTo {
    private String filename;
    private int moodId;
    private int moodTheme;
    private int width;
    private int height;

    public String getFileName() {
        return filename;
    }

    public void setFileName(String value) {
        filename = value;
    }

    public int getMoodId() {
        return moodId;
    }

    public void setMoodId(int value) {
        moodId = value;
    }

    public int getMoodTheme() {
        return moodTheme;
    }

    public void setMoodTheme(int value) {
        moodTheme = value;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int value) {
        width = value;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int value) {
        height = value;
    }

    public String toString() {
        StringBuffer output = new StringBuffer();

        output.append("mood id: ");
        output.append(moodId);
        output.append('\n');

        output.append("theme id: ");
        output.append(moodTheme);
        output.append('\n');

        output.append("filename: ");
        output.append(filename);
        output.append('\n');

        output.append("width: ");
        output.append(width);
        output.append('\n');

        output.append("height: ");
        output.append(height);
        output.append('\n');

        return output.toString();
    }
}
