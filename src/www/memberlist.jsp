<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.WebError" %>
<%@ page import="com.justjournal.core.Statistics" %>
<%@ page import="static com.justjournal.db.UserDao.*" %>
<%@ page import="com.justjournal.db.*"%>
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

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title>JustJournal.com: Member List</title>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>
    <style type="text/css" xml:space="preserve">
        .ltm {
           background-color: white;
        }

        .dtm {
            background-color: #F2F2F2;
        }

        .tbhead {
            background-color: black;
            color: white;
            font: Verdana, Arial 10px;    
        }
    </style>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">
    <h2>Member List</h2>

    <p>A list of public journals.  Private blogs are not listed.</p>

    <table border="0" cellpadding="1" cellspacing="1" style="border: thin solid #F2F2F2; font-size: 10px;">
        <thead>
            <tr class="tbhead">
                <th>Members</th>
                <th>Name</th>
                <th>Journal Created</th>
                <th>Profile</th>
            </tr>
        </thead>

        <tbody>
            <%
                Collection members = memberList();
                UserTo o;
                Iterator itr = members.iterator();
                int a = 0; // added user

                for (int i = 0, n = members.size(); i < n; i++) {
                    o = (UserTo) itr.next();

                    try {
                        if (!new User(o.getUserName()).isPrivateJournal())
                        {
                            if (a % 2 == 0) { %>
            <tr class="ltm">
                        <% } else { %>
            <tr class="dtm">
                         <% } %>
                <td><a href="users/<%=o.getUserName() %>"><%=o.getUserName() %></a></td>
                <td><%=o.getFirstName() %></td>
                <td><%=o.getSince() %></td>
                <td><a href="profile.jsp?user=<%=o.getUserName() %>">view profile</a></td>
            </tr>
            <%              a++;
                       }
            } catch (Exception e) {

            }
                }%>
        </tbody>
    </table>

    <p>Total members: <%=members.size()%>, Listed members: <%=a%></p>
</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>