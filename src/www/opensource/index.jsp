<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.WebError" %>
<%@ page import="com.justjournal.core.Statistics" %>
<%@ page import="com.justjournal.db.*" %>
<%@ page import="com.justjournal.search.BaseSearch" %>
<%@ page import="com.justjournal.utility.StringUtil" %>
<%@ page import="com.justjournal.utility.Xml" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
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
    <title>Just Journal: Open Source</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">

    <h2>Open Source</h2>

    <jsp:include page="../inc_login.jsp" flush="false"/>

    <p>You can get a newer copy of the source at <a href="http://sf.net">Source Forge</a>. Our project
        name is <a href="http://sourceforge.net/projects/justjournal/">JustJournal</a>. This includes
    the server software, windows client (GPL2), unix client (BSD 2-clause), DashBoard widget (for OSX, BSD 2-clause),
    and GTK app.</p>

    <h3>Old Work</h3>

    <p>Windows client <a href="../software/jj_windows_src_1.41.zip">source</a>
        for 1.41 release (under GPL)</p>

    <p>Java client <a href="http://groups.mac.com/javajj">source</a></p>

    <p>Here is a copy of the april 2005 snapshot minus the web page
        templates and configuration files. More will be added at a later
        time. This is released under the BSD license.
        <a href="src.zip">First snapshot</a></p>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>