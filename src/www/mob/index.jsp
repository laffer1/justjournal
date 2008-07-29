<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN"
  "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@ page contentType="application/xml+xhtml; charset=utf-8" language="java" %>
<html>

    <head>
        <title>JustJournal.com: Mobile</title>
    </head>

    <body>
        <h1>JustJournal.com</h1>
         <% if (session.getAttribute("auth.user") != null) { %>
        <p><%= session.getAttribute("auth.user") %>, you can: </p>
         <ul>
             <li><a href="update.jsp">Update Journal</a></li>
             <li><a href="logout.jsp">Log out</a></li>
          </ul>
    <% } else { %>
    <p><a href="login.jsp">Login</a></p>
    <% } %>
    </body>
</html>