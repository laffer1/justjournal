<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="com.justjournal.User" %>
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
<%--
  User: laffer1
  Date: Jul 23, 2008
  Time: 12:03:56 AM
--%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>JustJournal.com: Tag Cloud</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>
    <style type="text/css" xml:space="preserve">
        .TagCloudLarge {
            font-size: 2.3em;
        }
        .TagCloudMedium {
            font-size: 1.4em;
        }
        .TagCloudSmall {
            font-size: 0.8em;
        }
    </style>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">
    <jsp:include page="inc_login.jsp" flush="false"/>

    <h2>Tag Cloud</h2>

    <p>
<%
    try {
        CachedRowSet rs = SQLHelper.executeResultSet("select tags.name as name, count(*) as count from entry_tags, tags where tags.id=entry_tags.tagid GROUP by tags.name;");

        int largest = 0;
        int smallest = 10;
        int cutsmall;
        int cutlarge;

        while (rs.next()) {
            int tmpcount = rs.getInt(2);

            if (tmpcount > largest)
                largest = tmpcount;

             if (tmpcount < smallest)
                smallest = tmpcount;
        }

        cutsmall = largest / 3;
        cutlarge = cutsmall * 2;

        rs.first();
        
        while (rs.next()) {
             if (rs.getInt(2) > cutlarge)
             {
        %>
           <span class="TagCloudLarge"><%=rs.getString(1)%></span>
        <%
             }
             else if (rs.getInt(2) < cutsmall) {
        %>
           <span class="TagCloudSmall"><%=rs.getString(1)%></span>
        <%
             } else {
        %>
           <span class="TagCloudMedium"><%=rs.getString(1)%></span>
        <%   }
         }
        rs.close();
    } catch (SQLException sex) {

    }
%>
    </p>

    
</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
