<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN"
  "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@ page contentType="application/xml+xhtml; charset=utf-8" language="java" %>
<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.db.SecurityDao" %>
<%@ page import="com.justjournal.db.SecurityTo" %>
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
<html>

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

        <form method="post" action="../updateJournal?mood=12&amp;location=0&amp;client=mobile" name="frmUpdateJournal">
          <fieldset>
    <legend><strong>Journal Entry</strong><br/></legend>

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


</fieldset>
       </form>

    <% } else { %>
    <p><a href="login.jsp">Login</a></p>
    <% } %>

    </body>
</html>