<%@ page language="java"
         import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<%@ include file="Connections/jjdb.jsp" %>
<%
    response.setContentType("text/html");
    response.setDateHeader("Expires", System.currentTimeMillis());
    response.setDateHeader("Last-Modified", System.currentTimeMillis());
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.setHeader("Pragma", "no-cache");

    String username = (String) request.getParameter("user");

    if (username == null)
        username = "";

    Connection ConnRecordset1 = DriverManager.getConnection(MM_jjdb_STRING, MM_jjdb_USERNAME, MM_jjdb_PASSWORD);
    Statement stmt = ConnRecordset1.createStatement();
    Statement stmt2 = ConnRecordset1.createStatement();
    Statement stmt3 = ConnRecordset1.createStatement();
    String commandText = "SELECT user.id, user.type, user.name, user_bio.content As bio, user_contact.email, user_contact.icq, user_contact.aim, user_contact.yahoo, user_contact.msn, user_contact.hp_uri, user_contact.hp_title, user_contact.phone, user_location.city, user_location.state, user_location.country, user_location.zip FROM user, user_bio, user_contact, user_location WHERE user.username=\"" + username + "\" AND user_bio.id = user.id  AND user_contact.id = user.id AND user_location.id = user.id LIMIT 1;";
    String commandText2 = "select friends.friendid as fif, (SELECT user.username from user WHERE user.id=fif) FROM friends, user WHERE user.username=\"" + username + "\" AND user.id=friends.id;";
    String commandText3 = "select friends.id as fif, (SELECT user.username from user WHERE user.id=fif) FROM friends, user WHERE user.username=\"" + username + "\" AND user.id=friends.friendid;";
    ResultSet Recordset1 = stmt.executeQuery(commandText);
    ResultSet Recordset2 = stmt2.executeQuery(commandText2);
    ResultSet Recordset3 = stmt3.executeQuery(commandText3);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: User Profile: <%=username%></title>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">

    <% if (Recordset1.next()) { %>
    <h2>Profile for <%=username%></h2>

    <h3>General Information</h3>

    <p>user: <a href="users/<%=username%>"><%=username%></a> (<%=Recordset1.getString("id")%>)
        <br/>name: <%=Recordset1.getString("name")%>
        <% if (Recordset1.getString("hp_uri") != null && Recordset1.getString("hp_title") != null) { %>
        <br/>website: <a href="<%=Recordset1.getString("hp_uri")%>"><%=Recordset1.getString("hp_title")%></a>
        <% } %>
        <!--<br />location: <%=Recordset1.getString("city")%>, <%=Recordset1.getString("state")%>, <%=Recordset1.getString("country")%> <%=Recordset1.getString("zip") == null ? "" : Recordset1.getString("zip")%>-->
        <!--<br />birth date: (not yet available) -->
    </p>

    <h3>Contact Information</h3>

    <p>e-mail: <a href="mailto:<%=Recordset1.getString("email")%>"><%=Recordset1.getString("email")%></a>
        <br/>aim: <%=Recordset1.getString("aim") == null ? "N/A" : Recordset1.getString("aim") %> (<strong><a
            href='aim:addbuddy?screenname=<%=Recordset1.getString("aim")%>'>Add Buddy</a>, <a
            href='aim:goim?screenname=<%=Recordset1.getString("aim")%>&amp;message=Hello+there!+How+are+you?'>Send
        Message</a></strong>)
        <br/>icq:
        <% if (Recordset1.getString("icq") != null) { %>
        <img src="http://web.icq.com/whitepages/online?icq=<%=Recordset1.getString("icq")%>&amp;img=5" alt="ICQ"/>
        <%=Recordset1.getString("icq")%>
        <% } else { %>
        N/A
        <% } %>
        <br/>msn messanger: <%=Recordset1.getString("msn") == null ? "N/A" : Recordset1.getString("msn")%>
        <br/>yahoo messanger:
        <% if (Recordset1.getString("yahoo") != null) {%>
        <img alt='Yahoo status' src='http://opi.yahoo.com/online?u=<%=Recordset1.getString("yahoo")%>&amp;m=g&amp;t=0'
             width='12' height='12'/> <a
            href='http://profiles.yahoo.com/<%=Recordset1.getString("yahoo")%>'><%=Recordset1.getString("yahoo")%></a>
        (<strong><a
            href='http://edit.yahoo.com/config/set_buddygrp?.src=&amp;.cmd=a&amp;.bg=Friends&amp;.bdl=<%=Recordset1.getString("yahoo")%>'>Add
        User</a>, <a href='http://edit.yahoo.com/config/send_webmesg?.target=<%=Recordset1.getString("yahoo")%>'>Send
        Message</a></strong>)
        <% } else { %>
        N/A
        <% } %>
        <br/>phone: <%=Recordset1.getString("phone") == null ? "N/A" : Recordset1.getString("phone")%>
    </p>

    <h3>Biography</h3>

    <p>
        <%
            String bio = Recordset1.getString("bio");

            if (bio.length() > 1) { %>
        <%= bio %>
        <%
        } else {
        %>
        <%= "No bio available for this user." %>
        <% } %>
    </p>

    <h3><a href="users/<%=username%>/friends">Friends</a></h3>

    <p>
        <% if (Recordset2.next()) { %>
        <a href="users/<%=Recordset2.getString(2)%>"><%=Recordset2.getString(2)%></a>
        <% }
            while (Recordset2.next()) { %>
        , <a href="users/<%=Recordset2.getString(2)%>"><%=Recordset2.getString(2)%></a>
        <% } %>
    </p>

    <h3>Friend of</h3>

    <p>
        <% if (Recordset3.next()) { %>
        <a href="users/<%=Recordset3.getString(2)%>"><%=Recordset3.getString(2)%></a>
        <% }
            while (Recordset3.next()) { %>
        , <a href="users/<%=Recordset3.getString(2)%>"><%=Recordset3.getString(2)%></a>
        <% } %>
    </p>

    <% } else { %>
    <p>Error accessing profile. The user either doesn't exist or there is an unknown error.</p>
    <% } %>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
<%
    Recordset1.close();
    stmt.close();
    Recordset2.close();
    stmt2.close();
    Recordset3.close();
    stmt3.close();
    ConnRecordset1.close();
%>