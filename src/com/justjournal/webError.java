/*
 * webError.java
 *
 * Created on March 23, 2003, 1:09 PM
 */

package com.justjournal;

import java.io.PrintWriter;

/**
 * Prints out an error message in HTML.
 * @author  Lucas Holt
 * @version 1.1
 * @since 1.0
 */
public final class webError
{

    static void Display( final String ErrTitle, final String ErrMsg, final PrintWriter ResponseWriter )
    {
        StringBuffer sb = new StringBuffer();

        Display( ErrTitle, ErrMsg, sb);  // call the other version

        ResponseWriter.write(sb.toString());

    }

    static void Display( final String ErrTitle, final String ErrMsg, final StringBuffer sb )
    {
        if (sb.length() > 0) {
            // reset the output to display the error.
            sb.delete(0,sb.length() -1);
        }

        // Head
        sb.append( "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" );
        sb.append( "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" );
        sb.append( "<head>\n" );
        sb.append( "<title>" );
        sb.append(  ErrTitle );
        sb.append( "</title>\n" );
        sb.append( "</head>\n" );

        // Body
        sb.append( "<body style=\"margin: 0;\">\n" );

        sb.append( "<div style=\"width: 100%; height: 100px; margin-top: 1in; margin-left: 0; margin-right: 0; position relative; text-align: center; background: orange; color: white;\">\n" );
        sb.append( "<h1 style=\"font: 72pt Arial, Helvetica, sans-serif; letter-spacing: .2in;\">" + ErrTitle + "</h1>\n" );
        sb.append( "</div>\n" );

        sb.append( "<div style=\"margin: 1in; font: 12pt Arial, Helvetica, sans-serif;\">\n" );
        sb.append( "<p>" );
        sb.append( ErrMsg );
        sb.append( "</p>\n" );
        sb.append( "</div>\n" );

        sb.append( "</body>\n" );
        sb.append( "</html>\n" );
    }

}