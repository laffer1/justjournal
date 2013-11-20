<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
    session.invalidate();
%>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Log Out</title>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">
    <h2>Log Out</h2>

    <p>Your are now logged out. You may <a href="login.jsp">login</a> again.</p>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
