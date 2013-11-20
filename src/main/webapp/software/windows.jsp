<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Just Journal: Software: Windows Client</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all" />
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all" />
    <link rel="home" title="Home" href="../index.jsp" />
    <link rel="contents" title="Site Map" href="../sitemap.jsp" />
    <link rel="help" title="Technical Support" href="../support/index.jsp" />
</head>

<body>

<%@ include file="../header.inc" %>

<div id="content">
    <h2>Windows Client</h2>

    <jsp:include page="../inc_login.jsp" flush="false" />

       <p class="firstone">The Windows client sits in the
        taskbar when its running and allows you to quickly post entries throughout the day.  It also allows
        you to auto-detect the music playing on your computer.
        You can even use the Microsoft Word spell checker if Word is
        installed or add blog entries to Microsoft Outlook.</p>

    <h3>1.7 Release</h3>

    <p>This version includes a slightly new theme, Outlook integration is back, and runs on .NET framework 3.5.  Tagging
    was also introduced into the client.  It works just like the online form with spaces or commas separating tags.  You
    can turn on the music detection feature in options. At this time, it only ships as a 32bit program, but can run
    on 64bit Vista.  Released May 29, 2009.</p>

    <p>Windows 7, Vista, XP Client <a href="jj_windows_1.7.zip">zip</a></p>


    <h3>1.6 Release</h3>

    <p>The Windows clients require the .NET framework 2.0.  You can get it from Microsoft Windows Update (in XP or 2000) if you do not
       have it. Version 1.6 released on January 15, 2007. The Outlook integration feature was removed.</p>

    <ul>
        <li>Windows Vista/XP/2000 Client <a href="jj_windows_1.6.zip">zip</a></li>
        <li>Windows Vista x64/ XP 64bit Client <a href="jj_windows_amd64_1.6.zip">zip</a></li>
    </ul>
</div>

<%@ include file="../footer.inc" %>

</body>
</html>
