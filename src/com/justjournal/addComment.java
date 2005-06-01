/*
 * addComment.java
 *
 * Created on March 23, 2003, 12:42 PM
 */

package com.justjournal;

import com.justjournal.db.CommentTo;
import com.justjournal.db.CommentDao;
import com.justjournal.utility.Spelling;
import com.justjournal.utility.QueueMail;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Lucas Holt
 * @version 1.0
 */


/*  TODO:  FIX ERROR HANDLING IF DB TABLE IS MISSING!*/
public final class addComment extends HttpServlet {

    static final char endl = '\n';

    /** Initializes the servlet.
     */
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(final HttpServletRequest request,
                                  final HttpServletResponse response)
            throws java.io.IOException {

        boolean blnError = false;
        final StringBuffer sb = new StringBuffer();

        // start session if one does not exist.
        final HttpSession session = request.getSession(true);
        int userID = 0;
        String userName;

        userName = (String) session.getAttribute("auth.user");
        final Integer userIDasi = (Integer) session.getAttribute("auth.uid");
        if (userIDasi != null) {
            userID = userIDasi.intValue();
        }


        // Send HTML type in http stream
        response.setContentType("text/html");
        response.setDateHeader("Expires", System.currentTimeMillis());
        response.setDateHeader("Last-Modified", System.currentTimeMillis());
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        // Validate the login

        if (userID < 1) {

            try {
                userName = request.getParameter("username").toLowerCase();
                String password = request.getParameter("password");
                userID = webLogin.validate(userName, password);
            } catch (Exception e3) {
                webError.Display(
                        "Authentication Error",
                        "Unable to login.  Please check your username and password.",
                        sb);
            }
        }

        if (userID > 0) {
            // We authenticated OK.  Continue...

            // Get the user input
            CommentTo comment = new CommentTo();

            try {
                String body = request.getParameter("body");
                String subject = request.getParameter("subject");

                comment.setUserId(userID);
                comment.setEid(Integer.valueOf(request.getParameter("id")).intValue());  // entry id
                comment.setSubject(StringUtil.replace( subject, '\'', "\\\'" ));
                comment.setBody(StringUtil.replace( body, '\'', "\\\'" ));

                java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.sql.Date now = new java.sql.Date(System.currentTimeMillis());

                comment.setDate(fmt.format(now));
            } catch (IllegalArgumentException e) {
                webError.Display(
                        "Input Error",
                        e.getMessage(),
                        sb);
            }

            String isSpellCheck = request.getParameter("spellcheck");
            if (isSpellCheck == null)
                isSpellCheck = "";

            if (isSpellCheck.compareTo("checked") == 0) {
                Spelling sp = new Spelling();

                // store everything
                session.setAttribute("spell.ccheck", "true");
                session.setAttribute("spell.cbody", comment.getBody());
                session.setAttribute("spell.csubject", comment.getSubject());

                //check the spelling now
                session.setAttribute("spell.csuggest", sp.checkSpelling(comment.getBody()));

                // redirect the user agent to the promised land.
                response.sendRedirect("/comment/add.jsp?id=" + comment.getEid());
            } else {
                // clear out spell check variables to be safe
                // note this might be wrong still
                session.setAttribute("spell.ccheck", ""); //false
                session.setAttribute("spell.cbody", "");
                session.setAttribute("spell.csubject", "");



                // insert header fields
                if (blnError == false) {

                    try {
                        CommentDao cdao = new CommentDao();

                        blnError = !cdao.add(comment);

                    } catch (Exception e1) {
                        blnError = true;
                        webError.Display("Error", "Error adding comment", sb);
                        //out.println( e1.getMessage() );
                    }

                    try {
                        QueueMail mail = new QueueMail();

                        mail.setFromAddress("donotreply@jusetjournal.com");
                        mail.setToAddress("");
                        mail.setBody("");
                    } catch ( Exception e) {

                    }
                }

                // display message to user.
                if (blnError == false) {
                     // Begin HTML document.
                sb.append( "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" );
                sb.append( endl );

                sb.append( "<html xmlns=\"http://www.w3.org/1999/xhtml\">" );
                sb.append( endl );

                sb.append( "<head>" );
                sb.append( endl );
                sb.append( "\t<title>JustJournal.com: Add Comment</title>" );
                sb.append( endl );


                sb.append("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" title=\"default\" href=\"layout1.css\" />");

                sb.append( "</head>\n" );

                sb.append( "<body>\n" );

                sb.append("<div id=\"login\" style=\"color: silver; font-weight: bold;\">");
                sb.append("<img src=\"/images/jj-pencil.gif\" alt=\"jj pencil\" style=\"float: left;\" />");
                sb.append("</div>");
                sb.append( endl );


                    // BEGIN MENU
                sb.append( "\t<!-- Header: Begin -->" );
                sb.append( endl );
                sb.append( "\t\t<div id=\"header\">" );
                sb.append( endl );
                sb.append( "\t\t<h1>JustJournal.com</h1>" );
                sb.append( endl );
                sb.append( "\t</div>" );
                sb.append( endl );
                sb.append( "\t<!-- Header: End -->\n" );
                sb.append( endl );

                    sb.append( "\t<!-- Menu: Begin -->" );
                sb.append( endl );
                sb.append( "\t<div id=\"menu\">" );
                sb.append( endl );

                sb.append( "\t<p id=\"muser\">" );
                sb.append( endl );
                sb.append( "\t\t<a href=\"/users/" + userName + "\">recent entries</a><br />" );
                sb.append( endl );
                sb.append( "\t\t<a href=\"/users/" + userName + "/calendar\">Calendar</a><br />" );
                sb.append( endl );
                sb.append( "\t\t<a href=\"/users/" + userName + "/friends\">Friends</a><br />" );
                sb.append( endl );
                sb.append( "\t\t<a href=\"/users/" + userName + "/ljfriends\">LJ Friends</a><br />" );
                sb.append( endl );
                sb.append( "\t\t<a href=\"/profile.jsp?user=" + userName + "\">Profile</a><br />" );
                sb.append( endl );
                sb.append( "\t</p>" );
                sb.append( endl );

                // General stuff...
                sb.append( "\t<p id=\"mgen\">" );
                sb.append( endl );
                sb.append( "\t\t<a href=\"/update.jsp\">Update Journal</a><br />" );
                sb.append( endl );

                // Authentication menu choice
                if ( userID > 0 ) {
                    // User is logged in.. give them the option to log out.
                    sb.append( "\t\t<a href=\"/prefs/index.jsp\">Preferences</a><br />" );
                    sb.append( endl );
                    sb.append( "\t\t<a href=\"/logout.jsp\">Log Out</a>" );
                    sb.append( endl );
                } else {
                    // User is logged out.. give then the option to login.
                    sb.append( "\t\t<a href=\"/login.jsp\">Login</a>" );
                    sb.append( endl );
                }
                sb.append( "\t</p>" );
                sb.append( endl );

                sb.append( "\t<p><a href=\"/users/" + userName + "/rss\"><img src=\"/img/v4_xml.gif\" alt=\"RSS content feed\" /></a></p>" );
                sb.append( endl );

                sb.append( "\t</div>" );
                sb.append( endl );
                sb.append( "\t<!-- Menu: End -->\n" );
                sb.append( endl );

                    sb.append("\t<div id=\"content\">");
                    sb.append("\t\t<h2>Add Comment</h2>");
                    sb.append("\t\t<p><strong>Comment added sucessfully</strong></p>");
                    sb.append("\t</div>");

                    sb.append("</body>");
                    sb.append("</html>");
                }

            }


            // output the result of our processing
            ServletOutputStream outstream = response.getOutputStream();
            outstream.println( sb.toString() );
            outstream.flush();

        } else {
            // We couldn't authenticate.  Tell the user.
            webError.Display(
                    "Authentication Error",
                    "Unable to login.  Please check your username and password.",
                    sb);


            // output the result of our processing
            ServletOutputStream outstream = response.getOutputStream();
            outstream.println( sb.toString() );
            outstream.flush();
        }
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws  java.io.IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "adds a journal comment";
    }

}
