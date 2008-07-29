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
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN"
    "http://www.openmobilealliance.org/tech/DTD/xhtml-mobile11.dtd" >
<%@ page contentType="application/xhtml+xml; charset=utf-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
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