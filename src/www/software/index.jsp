<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Just Journal: Software</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">

    <h2>Software</h2>

    <jsp:include page="../inc_login.jsp" flush="false"/>

    <p class="firstone">There are two different methods for updating your journal. You
        can either post entries via this website or using one of the desktop clients. In order
        to use one of the desktop clients, you must download and install the program on your computer.
    </p>

    <p>There are advantages and disadvantages to both methods. The Windows client sits in the
        taskbar when its running and allows you to quickly post entries throughout the day. It also allows
        you to auto-detect the music playing on your computer and syncronize posts with Microsoft Outlook.
        You can even use the Microsoft Word spell checker if Word is installed. The website
        version works from any computer, pda or other device with a web browser that can
        view normal HTML webpages.</p>

    <p>Learn more about the <a href="windows.jsp">Windows client</a>.</p>

    <p>A Java client is under development for use on all computers that can support
        jre 1.4 including Macintosh. When the client is stable, it will be posted on this
        site. A native Macintosh client is planned for the future.</p>

    <p>Learn more about the <a href="http://groups.mac.com/javajj">Java Client </a></p>

    <p>If you are interested in using Just Journal server software
        on your website or in developing Just Journal, please view
        the <a href="../opensource/index.jsp">open source</a> page.</p>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>

