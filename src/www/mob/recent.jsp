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
        <ul>
            <li><a href="logout.jsp">Log out</a></li>
        </ul>
        <%}%>

<%
    EntryTo o;
    Collection entries = EntryDAO.viewRecentUniqueUsers();
    Iterator<EntryTo> itr = entries.iterator();

    for (int x = 0, n = entries.size(); x < n; x++) {
        o = itr.next();
%>
        <p><%=o.getUserName()%> - <%=o.getSubject()%>
        <br />
            <%=o.getBodyWithoutHTML()%>
        </p>
<%
    }
%>
    </body>
</html>