<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

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


<head>
    <title>Just Journal: Preferences</title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">
    <h2>Preferences</h2>

    <%
        Integer userID = (Integer) session.getAttribute("auth.uid");
        int ival = 0;
        if (userID != null) {
            ival = userID.intValue();
        }

        if (ival > 0) {
    %>
    <jsp:include page="inc_login.jsp" flush="false"/>

    <h3>Journal</h3>

    <dl>
        <dt><a href="title.jsp"><strong>Journal Title</strong></a></dt>
        <dd>Change the name of your journal as seen at the top of it.</dd>

        <dt><a href="friends.jsp"><strong>Friends</strong></a></dt>
        <dd>Define who your friends are. This will effect who will appear on your friends page along with
            the security of your journal entries.</dd>

        <dt><a href="ljfriends.jsp"><strong>LJ Friends</strong></a></dt>
        <dd>Define who your lj friends are. This will allow you to view live journal users
            public entries on the lj friends page.</dd>

        <dt><a href="security.jsp"><strong>Security</strong></a></dt>
        <dd>Allows you to define defaults for your account. In addition,
            you can make your journal private only. (not viewable by anyone
            but you using your login as verification.)</dd>

        <dt><a href="subscriptions.jsp"><strong>Subscriptions</strong></a></dt>
        <dd>Control the RSS feeds displayed on your journal under subscriptions.</dd>

        <dt><a href="../link/index.jsp"><strong>Add/Delete Links</strong></a></dt>
        <dd>Add or delete links on the side of your journal.</dd>
    </dl>

    <h3>Style</h3>

    <dl>
        <dt><a href="style_default.jsp"><strong>Base Template</strong></a></dt>
        <dd>Select the style of your journal. Pick from several predefined
            templates.</dd>

        <dt><a href="style_override.jsp"><strong>Overrides</strong></a></dt>
        <dd>Modify the default template look using Cascading Style Sheets (CSS).</dd>
    </dl>


    <h3>User</h3>

    <dl>
        <dt><a href="passwd.jsp"><strong>Password</strong></a></dt>
        <dd>Change your account password.</dd>

        <dt><a href="profile.jsp"><strong>Account &amp; Profile</strong></a></dt>
        <dd>Change your e-mail address, chat client, and other information.</dd>

        <dt><a href="avatar_upload.jsp"><strong>Avatar</strong></a></dt>
        <dd>Upload an avatar to represent your account. (user picture)</dd>
    </dl>

    <h3>Pictures</h3>

    <dl>
        <dt><a href="picture_upload.jsp"><strong>Add Picture</strong></a></dt>
        <dd>Add a picture to your journal.</dd>

        <dt><a href="picture_remove.jsp"><strong>Remove Picture</strong></a></dt>
        <dd>Remove a picture from your journal.</dd>
    </dl>

    <% } else { %>
    <p>You must <a href="../login.jsp">login</a> before you can edit your preferences.</p>
    <% } %>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>