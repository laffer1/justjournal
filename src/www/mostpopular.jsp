<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="com.justjournal.db.SQLHelper" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.sql.SQLException" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>JustJournal.com: Most Popular</title>
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

    <h2>Most Popular</h2>

    <p>The fifty most popular pages on Just Journal.</p>

    <ul>
<%
    try {
        CachedRowSet rs = SQLHelper.executeResultSet("SELECT * from hitcount order by count desc limit 50;");

        while (rs.next()) {

        %>
           <li><a href="<%=rs.getString(1)%>"><%=rs.getString(1)%> (<%=rs.getString(2)%>)</a></li>
        <%
         }
        rs.close();
    } catch (SQLException sex) {

    }
%>
    </ul>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
