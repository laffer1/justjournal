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
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>JustJournal.com: Site Map</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">
    <jsp:include page="inc_login.jsp" flush="false"/>

    <h2>Site Map</h2>

    <p>Account</p>
    <ul>
        <li><a href="create.jsp">New</a></li>
        <li><a href="cancel.jsp">Cancel</a></li>
        <li><a href="prefs/index.jsp">Preferences</a></li>
        <li><a href="login.jsp">Login</a></li>
        <li><a href="logout.jsp">Logout</a></li>
        <li><a href="memberlist.jsp">Member List</a></li>
    </ul>

    <p>Support</p>
    <ul>
        <li><a href="support/index.jsp">Index</a></li>
        <li><a href="support/bugs.jsp">Bugs</a></li>
        <li><a href="users/jjsite">Site Journal</a></li>
        <li><a href="privacy.jsp">Privacy Policy</a></li>
        <li><a href="moodlist.jsp">Mood List</a></li>
    </ul>

    <p>Software</p>
    <ul>
        <li><a href="software/index.jsp">Index</a></li>
        <li><a href="software/windows.jsp">Windows Client</a></li>
        <li><a href="opensource/index.jsp">Open Source</a></li>
        <li><a href="http://sourceforge.net/projects/justjournal">Source Forge CVS</a></li>
    </ul>

    <p>Journal</p>
    <ul>
        <li><a href="update.jsp">Update</a></li>
        <li><a href="tags.jsp">Tag Cloud</a></li>
    </ul>
</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>