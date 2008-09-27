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

    <h3>Windows</h3>

    <p>There are advantages and disadvantages to both methods. The Windows client sits in the
        taskbar when its running and allows you to quickly post entries throughout the day. It also allows
        you to auto-detect the music playing on your computer.
        You can even use the Microsoft Word spell checker if Word is installed. The website
        version works from any computer, pda or other device with a web browser that can
        view normal HTML webpages.</p>

    <p>Learn more about the <a href="windows.jsp">Windows client</a>.</p>

    <p>Some third party clients work with Just Journal including Microsoft's Windows Live client.  Use
    http://www.justjournal.com/xml-rpc  for the interface URL with these clients.  (Blogger 1.0 or metaweblog api).</p>

    <h3>Mac OS X</h3>

    <p><a href="JustJournal_Dash.zip">Just Journal widget 1.0</a> for Dashboard.
        This client only allows you to post public blog entries. </p>

    <p><strong>Beta!</strong> <a href="JustJournal_Dash_1.2.zip">JJ widget 1.2</a> for Dashboard.
        This is a development version that supports public/friends only/private posts
        and tries to fix bugs saving the username and password. It has a new theme,
        larger text area for entering text, and warns you when you forget to enter
        a username and password.</p>

    <p>Mac OS X 10.4 Tiger is required. If you're using Safari, click the download link. When
        the widget download is complete, Show Dashboard, click the Plus sign to display
        the Widget Bar and click the widget's icon in the Widget Bar to open it.
        If you're using a browser other than Safari, click the download link. When
        the widget download is complete, unarchive it and place it in /Library/Widgets/
        in your home folder. Show Dashboard, click the Plus sign to display the Widget
        Bar and click the widget's icon in the Widget Bar to open it.</p>

    <h3>*NIX</h3>

    <p>A command line client for MidnightBSD and other operating systems is
        now available. (Feb 27, 2008) <a href="unix.jsp">Learn more</a></p>

    <p>A Gtk based graphical client is available in the command line source package.  It is included in
    MidnightBSD mports.  You may also use Drivel which is hosted by the Gnome project.  Set the app to use
    Blogger 1.0 and use the URL  http://www.justjournal.com/xml-rpc </p>

    <h3>Java</h3>

    <p>A Java client is under development for use on all computers that can support
        jre 1.4 including Macintosh. When the client is stable, it will be posted on this
        site. A native Macintosh client is planned for the future.</p>

    <p>Learn more about the <a href="http://groups.mac.com/javajj">Java Client </a></p>

    <h3>Write your own</h3>

    <p>If you are interested in using Just Journal server software
        on your website or in developing Just Journal, please view
        the <a href="../opensource/index.jsp">open source</a> page.
        Just Journal supports Blogger 1.0 API and part of the Metaweblog API.  Use http://www.justjournal.com/xml-rpc
        as the &quot;interface&quot; url. 
    </p>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>

