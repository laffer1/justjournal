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

 import com.sun.istack.internal.NotNull;

 import java.sql.ResultSet;


/**
 * @author Lucas Holt
 *         User: laffer1
 *         Date: Sep 22, 2003
 *         Time: 11:01:27 PM
 */
public final class EmoticonDao {

    public @NotNull
    static EmoticonTo view(int themeId, int moodId) {
        int id = moodId;  // start at mood id but change as necessary

        String sqlStatement;

        EmoticonTo et = new EmoticonTo();
        ResultSet rs = null;
        ResultSet rs2;
        boolean icon = false;
        String sqlStatement2 = "Select parentmood from mood WHERE id=\"";

        try {

            while (!icon) {
                sqlStatement = "SELECT picurl, width, height from mood_theme_data where moodid="
                        + id + " AND moodthemeid=" + themeId + ";";

                rs = SQLHelper.executeResultSet(sqlStatement);

                if (rs.next()) {
                    et.setMoodId(moodId);
                    et.setFileName(rs.getString("picurl"));
                    et.setMoodTheme(themeId);
                    et.setWidth(rs.getInt("width"));
                    et.setHeight(rs.getInt("height"));

                    icon = true;
                } else {

                    if (id == 0)
                        break;

                    rs2 = SQLHelper.executeResultSet(sqlStatement2 + id + "\";");

                    if (rs2.next())
                        id = rs2.getInt("parentmood");
                    else
                        break;

                    try {
                        rs2.close();
                    } catch (Exception e) {
                        //nothing to do.
                    }
                }
            }

            rs.close();

        } catch (Exception e1) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }


        return et;
    }

}
