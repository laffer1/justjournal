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

//
//  removeFriends.java
//
//  Created by Caryn Holt on Sun Jul 06 2003.
//		Modified on: 7/10/03
//			     Imported servlet code and implemented required functions
//			     Added code to delete entry from friends table
//

package com.justjournal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class RemoveFriend extends JustJournalBaseServlet {

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {

        // Retreive username
        // String username = "";
        //username = (String) session.getAttribute( "auth.user" );

        // Retreive user id
        Integer userIDasi = (Integer) session.getAttribute("auth.uid");
        // convert Integer to int type
        int userID = 0;
        if (userIDasi != null) {
            userID = userIDasi.intValue();
        }

        // friend id that will be removed
        String temp = fixInput(request, "id");
        int friendID = Integer.valueOf(temp).intValue();

        try {
            String sqlStatement = "Delete FROM friends where id ='" + userID + "' and friendid='" + friendID + "' LIMIT 1;";
            int rowsAffected = SQLHelper.executeNonQuery(sqlStatement);

            if (rowsAffected > 0) {
                sb.append("<p>Friend has been deleted.</p>");
                sb.append(endl);
            } else
                webError.Display("Error", "Could not remove friend.", sb);
        } catch (Exception e) {
            // record was not deleted
            webError.Display("Error", e.getMessage(), sb);
        }
    }

    // required function for servlets
    public String getServletInfo() {
        return "Removes a friend";
    }
}
