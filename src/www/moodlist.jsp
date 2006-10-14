<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="com.justjournal.db.MoodDao" %>
<%@ page import="com.justjournal.db.MoodTo" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>JustJournal.com: Mood List</title>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">

    <h2>Mood List</h2>

    <p>This page is primarily useful for developers.</p>

    <p>This is the complete list of moods that you can choose from when
        posting a journal entry. Parent moods are used to display an emoticon
        for a mood if the mood does not have its own emoticon. (smiley, etc)
        Picking a mood is optional when posting a journal entry.</p>

    <table border="0" cellpadding="1" cellspacing="1">
        <thead>
            <tr style="background: black; color: white; font: Verdana, Arial 12pt;">
                <th>id</th>
                <th>parentmood</th>
                <th>title</th>
            </tr>
        </thead>

        <tbody style="font: 8pt Verdana, Arial, sans-serif;">
            <%
                Collection moods = MoodDao.viewByRelationship();
                MoodTo o;
                Iterator itr = moods.iterator();

                for (int i = 0, n = moods.size(); i < n; i++) {
                    o = (MoodTo) itr.next();

                    if (i % 2 == 0) { %>
            <tr style="background: white;">
                <%

             } else {
                %>
            <tr style="background: #F2F2F2;">
                <% } %>
                <td><%=o.getId() %></td>
                <td><%=o.getParent()%></td>
                <td><%=o.getName()%></td>
            </tr>
            <% } %>
        </tbody>
    </table>
</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
