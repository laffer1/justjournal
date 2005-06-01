
package com.justjournal;

import org.apache.log4j.Category;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Login account servlet.
 * @author Lucas Holt
 * @version 1.2
 * Sat Jun 07 2003
 *
 * Version 1.1 changes to a stringbuffer for output.
 * This should improve performance a bit.
 *
 * 1.2 fixed a bug with NULL pointer exceptions.
 *
 * TODO CONVERT TO MAVERICK CODE
 */
public final class loginAccount extends HttpServlet
{
    private static final char endl = '\n';
    private static Category log = Category.getInstance( loginAccount.class.getName() );

    /** Initializes the servlet.
     */
    public void init( ServletConfig config ) throws ServletException
    {
        super.init( config );

    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws java.io.IOException
    {
        final StringBuffer sb = new StringBuffer();
        final HttpSession session = request.getSession( true );

        response.setContentType( "text/html" );
        response.setDateHeader( "Expires", System.currentTimeMillis() );
        response.setDateHeader( "Last-Modified", System.currentTimeMillis() );
        response.setHeader( "Cache-Control", "no-store, no-cache, must-revalidate" );
        response.setHeader( "Pragma", "no-cache" );

        boolean blnError = false;

        // Validate the login
        int userID;
        String userName = request.getParameter( "username" ).toLowerCase();
        String password = request.getParameter( "password" );
        String passwordHash = request.getParameter( "password_hash" ).trim().toLowerCase();

        if ( userName == null || userName.length() < 3 ) {
            blnError = true;
            webError.Display(
                    "Input Error",
                    "username must be at least 3 characters.",
                    sb );
        }

        if ( passwordHash == null && password == null )
        {
            blnError = true;
            webError.Display( "Input Error",
                    "Please input a password.",
                    sb );
        }

        if ( blnError == false )
        {
            try
            {
                if ( log.isDebugEnabled() )
                    log.debug( "Attempting Login Validation  " );

                if ( passwordHash != null && passwordHash != "" )
                {
                    if ( log.isDebugEnabled() )
                        log.debug( "Using SHA1  pass=" + passwordHash );

                    userID = webLogin.validateSHA1( userName, passwordHash );
                }
                else
                {
                    if ( log.isDebugEnabled() )
                        log.debug( "Using clear  pass=" + password );

                    userID = webLogin.validate( userName, password );
                }

                if ( userID > 0 )
                {
                    session.setAttribute( "auth.uid", new Integer( userID ) );
                    session.setAttribute( "auth.user", userName );

                    // Begin HTML document.
                    sb.append( "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" );
                    sb.append( endl );
                    sb.append( "<html xmlns=\"http://www.w3.org/1999/xhtml\">" );
                    sb.append( endl );
                    sb.append( "<head>" );
                    sb.append( endl );
                    sb.append( "<title>JustJournal.com: Login</title>" );
                    sb.append( endl );
                    sb.append( "<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" title=\"default\" href=\"/layout1.css\">" );
                    sb.append( endl );
                    sb.append( "</head>" );
                    sb.append( endl );

                    sb.append( "<body>" );
                    sb.append( endl );

                    sb.append("<div id=\"login\" style=\"color: silver; font-weight: bold;\">");
                    sb.append( endl );
	                sb.append("<img src=\"/images/jj-pencil.gif\" alt=\"jj pencil\" style=\"float: left;\" />");
                    sb.append( endl );
                    sb.append("</div>");
                    sb.append( endl );

                    sb.append( "<!-- Header: Begin -->" );
                    sb.append( endl );
                    sb.append( "<div id=\"header\">" );
                    sb.append( endl );
                    sb.append( "\t<h1>JustJournal.com</h1>" );
                    sb.append( endl );
                    sb.append( "</div>" );
                    sb.append( endl );
                    sb.append( "<!-- Header: End -->" );
                    sb.append( endl );

                    sb.append( "\t<!-- Menu: Begin -->" );
                    sb.append( endl );
                    sb.append( "\t<div id=\"menu\">" );
                    sb.append( endl );

                    sb.append( "\t<ul>" );
                    sb.append( endl );
                    sb.append( "\t\t<li><a href=\"/users/" + userName + "\">recent entries</a></li>" );
                    sb.append( endl );
                    sb.append( "\t\t<li><a href=\"/users/" + userName + "/calendar\">Calendar</a></li>" );
                    sb.append( endl );
                    sb.append( "\t\t<li><a href=\"/users/" + userName + "/friends\">Friends</a></li>" );
                    sb.append( endl );
                    sb.append( "\t\t<li><a href=\"/profile.jsp?user=" + userName + "\">Profile</a></li>" );
                    sb.append( endl );
                    sb.append( "\t</ul>" );
                    sb.append( endl );

                    // General stuff...
                    sb.append( "\t<ul>" );
                    sb.append( endl );
                    sb.append( "\t\t<li><a href=\"/update.jsp\">Update Journal</a></li>" );
                    sb.append( endl );

                    // User is logged in.. give them the option to log out.
                    sb.append( "\t\t<li><a href=\"/prefs/index.jsp\">Preferences</a></li>" );
                    sb.append( endl );
                    sb.append( "\t\t<li><a href=\"/logout.jsp\">Log Out</a></li>" );
                    sb.append( endl );
                    sb.append( "\t</ul>" );
                    sb.append( endl );

                    sb.append( "\t<p>RSS Syndication<br /><br />" );
                    sb.append( "<a href=\"/users/" );
                    sb.append( userName );
                    sb.append( "/rss\"><img src=\"/img/v4_xml.gif\" alt=\"RSS content feed\" /> Recent</a><br />" );
                    sb.append( "<a href=\"/users/" );
                    sb.append( userName );
                    sb.append( "/subscriptions\">Subscriptions</a>" );
                    sb.append( "\t</p>" );
                    sb.append( endl );

                    sb.append( "\t</div>" );
                    sb.append( endl );
                    sb.append( "\t<!-- Menu: End -->\n" );
                    sb.append( endl );

                    // END MENU

                    sb.append( "<div id=\"content\">" );
                    sb.append( endl );
                    sb.append( "\t<h2>Login</h2>" );
                    sb.append( endl );
                    sb.append( "\t<p><strong>You are logged in as " + userName + ".</strong></p>" );
                    sb.append( endl );
                    sb.append( "</div>" );
                    sb.append( endl );

                    sb.append( "</body>" );
                    sb.append( endl );
                    sb.append( "</html>" );
                    sb.append( endl );
                } else {
                    webError.Display("Authentication Error",
                                 "Unable to login.  Please check your username and password.",
                                 sb);
                }
            } catch ( Exception e3 ) {
                webError.Display(
                    "Authentication Error",
                    "Unable to login.  Please check your username and password.",
                    sb );
            }
        }

        if ( log.isDebugEnabled() )
                log.debug( "Write out HTML  " );
        // output the result of our processing
        final ServletOutputStream outstream = response.getOutputStream();
        outstream.println( sb.toString() );
        outstream.flush();
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws java.io.IOException
    {
        processRequest( request, response );
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws java.io.IOException
    {
        processRequest( request, response );
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "login to journal service";
    }

}
