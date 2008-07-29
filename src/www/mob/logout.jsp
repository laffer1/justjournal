<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN"
    "http://www.openmobilealliance.org/tech/DTD/xhtml-mobile11.dtd" >
<%@ page contentType="application/xhtml+xml; charset=utf-8" language="java" %>
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
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <%
        session.invalidate();
    %>
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