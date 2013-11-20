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
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
    Integer userID = (Integer) session.getAttribute("auth.uid");
    int ival = 0;
    if (userID != null) {
        ival = userID.intValue();
    }
%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Preferences: Add Avatar</title>
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
        if (ival > 0) {
    %>

    <jsp:include page="inc_login.jsp" flush="false"/>

    <h3>Add Avatar</h3>

    <p>An avatar is a picture that represents your account. You can upload a gif,
        png, or jpeg image up to 10KB in size. Each account may have 1 avatar. Often
        users choose pictures of themselves, athletic icons, etc. There are no limits
        on dimensions of the image, but 100 x 100 pixels would be large.</p>

    <div style="width: 500px; padding: 5px; margin: 0;">
        <form id="upload" name="upload" method="POST" action="../avatar/submit.h"
              enctype="multipart/form-data">

            <fieldset>
                <legend>
                    <strong>Avatar</strong>
                    <br/>
                </legend>

                <div class="row">
                    <span class="label">File to upload</span>

                           <span class="formw">
                               <input name="avatar" type="file"/>
                           </span>
                </div>

            </fieldset>

            <div class="row">
                <input type="submit" name="submit" value="Upload file"/>
            </div>
        </form>
    </div>


    <% } else { %>
    <p>You must <a href="../login.jsp">login</a> before you can edit your preferences.</p>
    <% } %>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>