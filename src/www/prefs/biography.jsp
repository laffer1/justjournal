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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Preferences: Biography</title>
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
    
    User user = new User(ival);
%>

<jsp:include page="inc_login.jsp" flush="false"/>

<h3>Biography</h3>

<p>Publish a little information about yourself.  This is viewable on your profile unless you have marked
your blog private. Plain text only.</p>

<div style="width: 600px; padding: 5px; margin: 0">
    <!-- Servlet mapped to /prefs/Biography -->
    <form name="frmProfile" method="post" action="Biography">
        <fieldset>
            <legend><strong>User Bio</strong><br/></legend>

            <div class="row">
                <textarea id="bio" name="bio" style="width: 100%" cols="50" rows="20"><%=user.getBiography()%></textarea>
            </div>

        </fieldset>

        <div class="row"><input type="submit" name="submit" value="submit"/></div>

        <!-- Hack to fix spacing problem.. especially with text boxes -->
        <div class="spacer">&nbsp;</div>
    </form>
</div>

<% } else { %>
<p>You must <a href="../login.jsp">login</a> before you can edit your preferences.</p>
<% } %>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>
