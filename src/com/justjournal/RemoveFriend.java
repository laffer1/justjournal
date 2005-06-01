//
//  removeFriends.java
//
//  Created by Caryn Holt on Sun Jul 06 2003.
//		Modified on: 7/10/03
//			     Imported servlet code and implemented required functions
//			     Added code to delete entry from friends table
//

package com.justjournal;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public final class RemoveFriend extends HttpServlet
{

    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws java.io.IOException
    {
        // Will be using session data, must initialize session, will not affect any current session
        HttpSession session = request.getSession( true );

        // Retreive username
       // String username = "";
        //username = (String) session.getAttribute( "auth.user" );

        // Retreive user id
        Integer userIDasi = (Integer) session.getAttribute( "auth.uid" );
        // convert Integer to int type
        int userID = 0;
        if ( userIDasi != null ) {
            userID = userIDasi.intValue();
        }

        // Send HTML type in http stream
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();

        // friend id that will be removed
        String temp = request.getParameter( "id" );
        int friendID = Integer.valueOf( temp ).intValue();

        // the value returned is currently not used

            try {
                String sqlStatement = "Delete FROM friends where id ='" + userID + "' and friendid='" + friendID + "' LIMIT 1;";
                int rowsAffected = SQLHelper.executeNonQuery( sqlStatement );

                if ( rowsAffected > 0 )
                    out.println( "Friend has been deleted." );
                else
                    out.println( "Error removing friend." );
            } catch ( Exception e ) {
                // record was not deleted
                out.println( e.getMessage() );
            }

        out.flush();
        return;
    }

    // processes get requests
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws java.io.IOException
    {
        processRequest( request, response );
    }

    // processes post requests
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws java.io.IOException
    {
        processRequest( request, response );
    }

    // required function for servlets
    public String getServletInfo()
    {
        return "Removes a friend";
    }
}

;
