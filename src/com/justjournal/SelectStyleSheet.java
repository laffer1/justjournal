
package com.justjournal;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * @author Lucas Holt
 * User: laffer1
 * Date: Aug 25, 2003
 * Time: 9:22:21 PM
 */
public final class SelectStyleSheet extends HttpServlet
{

    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws java.io.IOException
    {
        // initial error condition is false
        boolean error = false;

        // Will be using session data, must initialize session, will not affect any current session
        HttpSession session = request.getSession( true );

        // Retreive username
        //String username = "";
        //username = (String) session.getAttribute( "auth.user" );

        // Retreive user id
        Integer userIDasi = (Integer) session.getAttribute( "auth.uid" );
        // convert Integer to int type
        int userID = 0;
        if ( userIDasi != null ) {
            userID = userIDasi.intValue();
        }

        String styleSheet = request.getParameter( "css" );

        // Send HTML type in http stream
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();

        if ( styleSheet != null ) {
            Integer cssId = new Integer( 1 );

            try {
                cssId = new Integer( styleSheet );
            } catch ( Exception e ) {
                error = true;
            }

            if ( error == false ) {
                try {
                    String sqlStatement = "Update user_pref SET style='" + cssId + "' where id ='" + userID + "';";
                    int rowsAffected = SQLHelper.executeNonQuery( sqlStatement );
                    if ( rowsAffected < 1 ) {
                        error = true;
                    } else {
                        out.println( "Style has been changed." );
                    }
                } catch ( Exception e ) {
                    // record was not updated
                    error = true;
                    out.println( e.getMessage() );
                }
            }

        } else {
            webError.Display( "Error", "Stylesheet must be specified.", out );
        }


        if ( error == true ) {
            webError.Display( "Error", "Unknown error has occured.", out );
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
        return new String();
    }
}
