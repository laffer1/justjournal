<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.db.SecurityDao" %>
<%@ page import="com.justjournal.db.SecurityTo" %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN"
  "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@ page contentType="application/xml+xhtml; charset=utf-8" language="java" %>
<%
    session.invalidate();
%>
<html>
  <head>
      <title>JustJournal.com: Log Out</title>
  </head>

  <body>
     <h1>JustJournal.com</h1>
     <h2>Log Out</h2>

    <p>Your are now logged out. You may
        <a href="login.jsp">login</a> again.</p>

  </body>
</html>