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

import sun.jdbc.rowset.CachedRowSet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Retrieve and acquire mood's for use with journal entries.
 *
 * @author Lucas Holt
 * @version 1.0
 *          Date: Jan 9, 2004
 *          Time: 1:55:20 PM
 * @since 1.0
 */
public final class MoodDao {
    /**
     * Retrieve the moods from the data store including
     * the title, id and parent moods.
     *
     * @return Collection of MoodTo objects.
     */
    public static Collection<MoodTo> view() {
        ArrayList<MoodTo> moods = new ArrayList<MoodTo>(125);
        MoodTo mood;
        final String sqlStatement = "SELECT id,parentmood,title FROM mood ORDER BY title ASC;";

        try {
            final CachedRowSet RS = SQLHelper.executeResultSet(sqlStatement);

            while (RS.next()) {
                mood = new MoodTo();
                mood.setId(RS.getInt("id"));
                mood.setParent(RS.getInt("parentmood"));
                mood.setName(RS.getString("title"));
                moods.add(mood);
            }
        } catch (Exception e1) {

        }

        return moods;
    }
}
