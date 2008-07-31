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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Preferences: Account &amp; Profile</title>
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

<h3>Account &amp; Profile</h3>

<p>Most of the information on this form will be searchable and will be published publicly
    on your profile page. Name and email address are the only required fields.</p>

<p>Security settings for profiles will be added at a later date.</p>

<div style="width: 600px; padding: 5px; margin: 0">
    <form name="frmProfile" method="post" action="profilesubmit.h">
        <fieldset>
            <legend><strong>Basic Account Information</strong><br/></legend>

            <div class="row">
                <span class="label"><label for="name">first name</label></span>
                <span class="formw"><input type="text" name="name" id="name" size="20" maxlength="50"/></span>
            </div>

            <div class="row">
                <span class="label"><label for="email">e-mail address</label></span>
                <span class="formw"><input type="text" name="email" id="email" size="20" maxlength="100"/></span>
            </div>

        </fieldset>
        <!-- Hack to fix spacing problem.. especially with text boxes -->
        <div class="spacer">&nbsp;</div>
        <fieldset>
            <legend><strong>Instant Messaging</strong><br/></legend>

            <div class="row">
                <span class="label"><label for="icq">icq</label></span>
                <span class="formw"><input type="text" name="icq" id="icq" size="20" maxlength="20"/></span>
            </div>

            <div class="row">
                <span class="label"><label for="aim">aim</label></span>
                <span class="formw"><input type="text" name="aim" id="aim" size="20" maxlength="30"/></span>
            </div>

            <div class="row">
                <span class="label"><label for="yahoo">yahoo</label></span>
                <span class="formw"><input type="text" name="yahoo" id="yahoo" size="20" maxlength="100"/></span>
            </div>

            <div class="row">
                <span class="label"><label for="msn">msn</label></span>
                <span class="formw"><input type="text" name="msn" id="msn" size="20" maxlength="100"/></span>
            </div>

        </fieldset>
        <!-- Hack to fix spacing problem.. especially with text boxes -->
        <div class="spacer">&nbsp;</div>
        <fieldset>
            <legend><strong>Home Page</strong><br/></legend>

            <p>If you have a website, fill this section in to have it appear on your profile.
                A uri is a full address to a website, like http://www.justjounal.com/. Many people
                refer to this incorrectly as a url. uri = urn + url</p>

            <div class="row">
                <span class="label"><label for="hptitle">title</label></span>
                <span class="formw"><input type="text" name="hptitle" id="hptitle" size="20" maxlength="50"/></span>
            </div>

            <div class="row">
                <span class="label"><label for="hpuri">uri</label></span>
                <span class="formw"><input type="text" name="hpuri" id="hpuri" size="20" maxlength="250"/></span>
            </div>
        </fieldset>
        <!-- Hack to fix spacing problem.. especially with text boxes -->
        <div class="spacer">&nbsp;</div>
        <fieldset>
            <legend><strong>Telephone</strong><br/></legend>

            <p>It is not wise to list your phone number, however some users might want to.
                Do not list your number if you are under 18 years old!</p>

            <div class="row">
                <span class="label"><label for="phone">number</label></span>
                <span class="formw"><input type="text" name="phone" id="phone" size="14" maxlength="14"/></span>
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