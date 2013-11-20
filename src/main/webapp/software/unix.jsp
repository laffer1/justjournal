<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Just Journal: Software: *NIX Client</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../chrometheme/chromestyle.css"/>
    <script type="text/javascript" src="../chromejs/chrome.js">/*menu script */</script>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">
    <h2>*NIX Client</h2>

    <jsp:include page="../inc_login.jsp" flush="false"/>

    <p class="firstone">The command line client was written for
        <a href="http://www.midnightbsd.org">MidnightBSD</a>, but should work on other unix-like systems.</p>

    <h3>Compiling and Installing jjclient</h3>

    <p>To compile the client, use make. make install will install it into a directory (default /usr/local/bin/jjclient)
        . DESTDIR and prefix are available to be set for advanced users or developers working with software
        repositories.</p>

    <h3>Usage</h3>

    <p>jjclient -u yourusername -p yourpassword < sometexttoblog.txt or
        jjclient -u yourusername -p yourpassword
        type entry
        control + D (EOF) on it's own line and return
    </p>

    <h3>Download</h3>
    <ul>
        <li><a href="unix/jjclient-1.0.2.tar.gz">Just Journal for *NIX 1.0.2</a></li>
    </ul>
    <p>You may also download the client from <a href="http://sf.net/projects/justjournal">sourceforge</a></p>

    <h3>Next Release</h3>

    <p>The next release will be 1.0.3. It includes several new features.
        <br/>-d allows you to debug connection problems
        <br/>-h host allows you to use it with other JJ installs.
        <br/>-s subject allows you to write a subject with your blog post.
    </p>

    <p>You may try out this version from CVS on sourceforge.</p>
</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>
