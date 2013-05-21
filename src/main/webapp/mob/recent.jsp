<%@ page import="com.justjournal.db.EntryDAO" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.justjournal.db.EntryTo" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN"
    "http://www.openmobilealliance.org/tech/DTD/xhtml-mobile11.dtd">
<%@ page contentType="application/xhtml+xml; charset=utf-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>
        <title>JustJournal.com: Recent Blogs</title>
        <link rel="stylesheet" type="text/css" href="mobile.css" />
    </head>

    <body>
        <div id="header">
        <h1>JustJournal.com</h1>
        </div>
        <h2>Recent Blogs</h2>
            <% if (session.getAttribute("auth.user") != null) { %>
        <p><%= session.getAttribute("auth.user") %>, you can: </p>
         <div class="arrowgreen">
         <ul>
             <li><a href="update.jsp">Update Journal</a></li>
             <li><a href="recent.jsp">Recent Blogs</a></li>
             <li><a href="logout.jsp">Log out</a></li>
          </ul>
          </div>
    <% } else { %>
    <div class="arrowgreen">
        <ul>
        <li><a href="login.jsp">Login</a></li>
            <li><a href="recent.jsp">Recent Blogs</a></li>
        </ul>
    </div>
    <% } %>

<%
    EntryTo o;
    Collection entries = EntryDAO.viewRecentUniqueUsers();
    Iterator<EntryTo> itr = entries.iterator();

    for (int x = 0, n = entries.size(); x < n; x++) {
        o = itr.next();
%>
        <p><%=o.getUserName()%> - <b><%=o.getSubject()%></b>
        <br />
            <%=o.getBodyWithoutHTML().replace("&nbsp;", " ")%>
        </p>
<%
    }
%>
    </body>
</html>