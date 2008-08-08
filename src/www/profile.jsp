<%@ page language="java"
         import="com.justjournal.User" %>
<%@ page import="com.justjournal.WebError" %>
<%@ page import="com.justjournal.db.SQLHelper" %>
<%@ page import="com.justjournal.db.UserDao" %>
<%@ page import="com.justjournal.utility.StringUtil"%>
<%@ page import="javax.sql.rowset.CachedRowSet"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator" %>
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

    if (!StringUtil.lengthCheck(username, 3, 15)) {
        PrintWriter out2 = response.getWriter();
        WebError.Display("Input Error",
                        "username must be 3-15 characters.",
                        out2);
        return;
    }

    CachedRowSet Recordset1;

    try {
        String commandText = "SELECT user.id, user.type, user.name, user_bio.content As bio, user_contact.email, user_contact.icq, user_contact.aim, user_contact.yahoo, user_contact.msn, user_contact.hp_uri, user_contact.hp_title, user_contact.phone, user_location.city, user_location.state, user_location.country, user_location.zip FROM user, user_bio, user_contact, user_location WHERE user.username=\"" + username + "\" AND user_bio.id = user.id AND user_contact.id = user.id AND user_location.id = user.id LIMIT 1;";
       Recordset1 = SQLHelper.executeResultSet(commandText);
    } catch (Exception sqlex) {
        PrintWriter out2 = response.getWriter();
        WebError.Display("Error",
                        "Unable to retrieve profile.",
                        out2);
        return;
    }

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

<% if (Recordset1 != null && Recordset1.next()) {
       User user = new User(username);

       if (!user.isPrivateJournal())
       {
%>


    <h2>Profile for <%=username%></h2>

<%

    if (user.showAvatar())
    { %>
    <p><img src="image?id=<%=user.getUserId()%>" alt="<%=username%> 's avatar" /></p>
<%  } %>

    <h3>General Information</h3>

    <p>user: <a href="users/<%=username%>"><%=username%></a> (<%=Recordset1.getString("id")%>)
        <br/>name: <%=Recordset1.getString("name")%>
        <% if (Recordset1.getString("hp_uri") != null && Recordset1.getString("hp_title") != null) { %>
        <br/>website: <a href="<%=Recordset1.getString("hp_uri")%>"><%=Recordset1.getString("hp_title")%></a>
        <% } %>
        <!--<br />location: <%=Recordset1.getString("city")%>, <%=Recordset1.getString("state")%>, <%=Recordset1.getString("country")%> <%=Recordset1.getString("zip") == null ? "" : Recordset1.getString("zip")%>-->
        <!--<br />birth date: (not yet available) -->
        <br />my journal: <%=user.getJournalName()%>
    </p>

    <h3>Contact Information</h3>

    <p>e-mail: <a href="mailto:<%=Recordset1.getString("email")%>"><%=Recordset1.getString("email")%></a>
        <br/>aim:
        <% if (Recordset1.getString("aim") != null) { %>
        <img alt="AIM status" src="http://big.oscar.aol.com/<%=Recordset1.getString("aim")%>?on_url=http://www.aol.com/aim/gr/online.gif&amp;off_url=http://www.aol.com/aim/gr/offline.gif"
             height="17" width="14" /> <%=Recordset1.getString("aim") == null ? "N/A" : Recordset1.getString("aim") %>
            (<strong><a
            href='aim:addbuddy?screenname=<%=Recordset1.getString("aim")%>'>Add Buddy</a>, <a
            href='aim:goim?screenname=<%=Recordset1.getString("aim")%>&amp;message=Hello+there!+How+are+you?'>Send
        Message</a></strong>)
        <% } else { %>
        N/A
        <% } %>
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

            if (bio != null && bio.length() > 1) { %>
        <%= bio %>
        <%
        } else {
        %>
        No bio available for this user.
        <% } %>
    </p>

    <h3><a href="users/<%=username%>/friends">Friends</a></h3>

<%

    Collection friends = UserDao.friends(username);

    Iterator f = friends.iterator();
    String fr;
%>

    <p>
        <% if (f.hasNext()) { %>
        <img src="images/userclass_16.png" alt="users" />
        <a href="users/<%= fr = (String) f.next()%>"><%=fr%></a>
        <% }
            while (f.hasNext() ) {
               fr = (String) f.next();
        %>
        , <a href="users/<%=fr%>"><%=fr%></a>
        <% } %>
    </p>

    <h3>Friend of</h3>
<%

    Collection friendsof = UserDao.friendsof(username);

    Iterator fo = friendsof.iterator();
    String fro;
%>

    <p>
        <% if (fo.hasNext()) { %>
        <a href="users/<%= fro = (String) fo.next()%>"><%=fro%></a>
        <% }
            while (fo.hasNext()) {
                fro = (String) fo.next(); %>
        , <a href="users/<%=fro%>"><%=fro%></a>
        <% } %>
    </p>

<%  }
} else { %>
    <p>Error accessing profile. The user either doesn't exist or there is an unknown error.</p>
<% } %>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
<%
    try {
        Recordset1.close();
    } catch (java.sql.SQLException sqlex) {

    } catch (NullPointerException nex) {

    }
%>