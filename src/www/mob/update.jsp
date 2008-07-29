<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN"
    "http://www.openmobilealliance.org/tech/DTD/xhtml-mobile11.dtd">
<%@ page contentType="application/xhtml+xml; charset=utf-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.WebError" %>
<%@ page import="com.justjournal.core.Statistics" %>
<%@ page import="com.justjournal.db.*" %>
<%@ page import="com.justjournal.search.BaseSearch" %>
<%@ page import="com.justjournal.utility.StringUtil" %>
<%@ page import="com.justjournal.utility.Xml" %>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.ParsePosition" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%
    response.setHeader("Vary", "Accept"); // content negotiation
    response.setDateHeader("Expires", System.currentTimeMillis());
    response.setDateHeader("Last-Modified", System.currentTimeMillis());
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.setHeader("Pragma", "no-cache");

    // date stuff
    java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    java.sql.Date now = new java.sql.Date(System.currentTimeMillis());

// Get the session user input
    String sbody = (String) session.getAttribute("spell.body");
    String smusic = (String) session.getAttribute("spell.music");
    String ssubject = (String) session.getAttribute("spell.subject");
    String stags = (String) session.getAttribute("spell.tags");
    String strackback = (String) session.getAttribute("spell.trackback");

    if (sbody == null)
        sbody = "";

    if (smusic == null)
        smusic = "";

    if (ssubject == null)
        ssubject = "";

    if (stags == null)
        stags = "";

    if (strackback == null)
        strackback = "";

    Integer userID = (Integer) session.getAttribute("auth.uid");
    int ival = 0;
    if (userID != null) {
        ival = userID.intValue();
    }

%>

    <head>
        <title>JustJournal.com: Update Journal</title>
    </head>

    <body>
        <h1>JustJournal.com</h1>
        <h2>Update Journal</h2>
         <% if (session.getAttribute("auth.user") != null) { %>
        <p><%= session.getAttribute("auth.user") %>, you can: </p>
         <ul>
             <li><a href="logout.jsp">Log out</a></li>
          </ul>

        <form method="post" action="../updateJournal" id="frmUpdateJournal">
          <fieldset>

               <input type="hidden" name="mood" id="mood" value="12"/>
               <input type="hidden" name="location" id="location" value="0"/>
               <input type="hidden" name="client" id="client" value="mobile"/>

    <div class="row">
        <span class="label"><label for="date">Date</label></span>
	  <span class="formw"><input name="date" id="date" value="<%=fmt.format( now ) %>" size="25" maxlength="19"/>
	  </span>
    </div>

    <div class="row">
        <span class="label"><label for="subject">Subject</label></span>
	  <span class="formw"><input name="subject" type="text" id="subject" size="25" maxlength="150"
                                 value="<%=ssubject%>"/>
	  (optional)</span>
    </div>

    <div class="row">
        <span class="label"><label for="body">Body</label></span>
        <span class="formw"><textarea id="body" name="body" style="width: 100%" cols="50"
            rows="20"><%=sbody%></textarea></span>
    </div>

        <div class="row">
    <span class="label"><label for="security">Security</label></span>
	  <span class="formw">
	  	<select id="security" name="security" size="1">
              <%
                  Integer ssec = (Integer) session.getAttribute("spell.security");
                  int issec = 0;
                  if (ssec != null) {
                      issec = ssec.intValue();
                  }

                  /* Check to see if the Journal is marked private */
                  boolean prvt = false;
                  try {
                      prvt = (ival > 0 &&
                              new User((String) session.getAttribute("auth.user")).isPrivateJournal());
                  } catch (Exception e) {
                      // ignore exception.
                  }


                  for (java.util.Iterator iterator = SecurityDao.view().iterator(); iterator.hasNext();) {
                      SecurityTo o = (SecurityTo) iterator.next();

                      out.print("\t<option value=\"" + o.getId());

                      if (prvt && o.getName().compareTo("private") == 0)
                          out.print("\" selected=\"selected\">");
                      else if ((o.getName().compareTo("public") == 0 && !prvt)
                              || o.getId() == issec)
                          out.print("\" selected=\"selected\">");
                      else
                          out.print("\">");

                      out.println(o.getName() + "</option>");
                  }
              %>
          </select>
	  </span>
</div>

              <div class="row"><input type="submit" name="submit" value="submit"/></div>
   
</fieldset>
       </form>

    <% } else { %>
    <p><a href="login.jsp">Login</a></p>
    <% } %>

    </body>
</html>