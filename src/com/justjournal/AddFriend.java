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
//  AddFriends.java
//
//  Created by Caryn Holt on Sun Jul 06 2003
//		Modified on: 7/10/03
//			     Imported servlet code and implemented required functions
//			     Implemented processRequest
//

package com.justjournal;

import sun.jdbc.rowset.CachedRowSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public final class AddFriend extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        // initial error condition is false
        boolean error = false;

        // Will be using session data, must initialize session, will not affect any current session
        HttpSession session = request.getSession(true);

        // Retreive username
        //String username = "";
        //username = (String) session.getAttribute( "auth.user" );

        // Retreive user id
        Integer userIDasi = (Integer) session.getAttribute("auth.uid");
        // convert Integer to int type
        int userID = 0;
        if (userIDasi != null) {
            userID = userIDasi.intValue();
        }

        // Send HTML type in http stream
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String friend1 = request.getParameter("fr1");
        String friend2 = request.getParameter("fr2");
        String friend3 = request.getParameter("fr3");
        String friend4 = request.getParameter("fr4");
        String friend5 = request.getParameter("fr5");

        int counter = 0;

        if (friend1.length() == 0) {
            error = true;
            webError.Display("Input Error", "One friend must be specified", out);
        } else {
            counter++;
        }

        if (friend2.length() > 0)
            counter++;
        if (friend3.length() > 0)
            counter++;
        if (friend4.length() > 0)
            counter++;
        if (friend5.length() > 0)
            counter++;

        // place the usernames in an array
        String[] friends = new String[counter];
        int index = 0;
        friends[index] = new String(friend1);

        // checking each optional parameter again, hope code passes monkey test
        if (friend2.length() > 0)
            friends[++index] = friend2;
        if (friend3.length() > 0)
            friends[++index] = friend3;
        if (friend4.length() > 0)
            friends[++index] = friend4;
        if (friend5.length() > 0)
            friends[++index] = friend5;

        for (int i = 0; i < counter; i++) {
            // check if error condition is false
            if (error == false) {
                // query users table for user id
                String sqlStatement = "SELECT * FROM user where username='" + friends[i] + "' LIMIT 1;";
                int friendID = 0;
                try {
                    CachedRowSet record;
                    record = SQLHelper.executeResultSet(sqlStatement);
                    // get id
                    // 0 is not a valid id
                    if (record.next())
                        friendID = record.getInt("id");
                    else
                        error = true;
                }
                        // generate error if user id is not valid and set error condition to true
                catch (Exception e) {
                    out.println(e.getMessage());
                    error = true;
                }


                // generate sql statement
                sqlStatement = "Insert INTO friends (id, friendid) values('" + userID + "','" + friendID + "');";
                //check error condition again, since an error could have been generated at this point
                if (error == false) {
                    // insert, placing method in try catch block
                    try {
                        int rowsAffected = SQLHelper.executeNonQuery(sqlStatement);

                        if (rowsAffected < 1)
                            webError.Display("Error adding friend", "Unable to add friend.", out);
                    }
                            // if exception, set error condition to true, display message
                    catch (Exception e1) {
                        out.println(e1.getMessage());
                        error = true;
                    }
                }
            }
            out.println("Your friend(s) have been added.");
        }

        out.flush();

        return;

    }

    // processes get requests
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    // processes post requests
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    // required function for servlets
    public String getServletInfo() {
        return "Add a friend";
    }

}

;
