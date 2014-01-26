<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <title>${user.journalName}</title>
    <link rel="stylesheet" type="text/css" href="../../styles/users.css">
    <link rel="stylesheet" type="text/css" media="screen" href="../../styles/${user.styleId}.css">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link rel="alternate" type="application/rss+xml" title="RSS"
          href="http://www.justjournal.com/users/${user.userName}/rss">
    <link rel="alternate" type="application/atom+xml" title="Atom"
          href="http://www.justjournal.com/users/${user.userName}/atom">
    <link rel="EditURI" type="application/rsd+xml" title="RSD"
          href="http://www.justjournal.com/rsd?blogID=${user.userName}">
    <script type="text/javascript" src="../../js/switchcontent.js"></script>
    <script type="text/javascript" src="../../components/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="../../components/jquery-ui/ui/minified/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../../js/lightbox.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="../../lightbox.css">
    <c:if test="${user.spiderAllowed == true}">
        <meta name="robots" content="noindex, nofollow, noarchive">
        <meta name="googlebot" content="nosnippet">
    </c:if>
</head>

<body>

<!-- Header: Begin -->
<div id="header">
    <h1>${user.journalName}</h1>
</div>
<!-- Header: End -->

<!-- Menu: Begin -->
<div id="menu">
    <c:if test="${user.showAvatar == true}">
        <img alt="avatar" src="../../image?id=${user.userId}">
    </c:if>

    <p id="muser">
        <a href="/users/${user.userName}">Journal Entries</a><br/>
        <a href="/users/${user.userName}/calendar">Calendar</a><br/>
        <a href="/users/${user.userName}/friends">Friends</a><br/>
        <a href="/users/${user.userName}/pictures">Pictures</a><br/>
        <a href="/profile.jsp?user=${user.userName}">Profile</a><br/>
    </p>

    <p id="mgen">
        <a href="/#/entry">Update Journal</a><br/>
        <a href="/">Login</a>
    </p>

    <p id="mrssreader">
        <a href="/users/${user.userName}/subscriptions">RSS Reader</a><br/>
    </p>

    <div id="mformats"><strong
            style="text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 10px; width: 100%; font-size: 10px;">Formats</strong><br/>

        <p><a rel="alternate" href="/users/${user.userName}/rss"><i class="fa fa-rss"></i> RSS</a><br/>
            <a rel="alternate" href="/users/${user.userName}/atom"><i class="fa fa-rss"></i> ATOM</a><br/>
            <img src="../../images/icon_pdf.gif" alt="PDF"/><a
                    href="http://www.justjournal.com/users/${user.userName}/pdf">PDF</a><br/>
            <img src="../../images/icon_rtf.gif" alt="RTF"/><a
                    href="http://www.justjournal.com/users/${user.userName}/rtf">RTF</a><br/>
        </p>
    </div>
    <div id="msearchbox"><strong
            style="text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;">Search</strong>

        <form id="msearch" action="http://www.justjournal.com/users/${user.userName}/search" method="get">
            <p><input type="text" name="bquery" id="bquery" style="width: 90px;"/><br/>
                <input type="submit" name="search" id="searchbtn" value="Search Blog"/></p>
        </form>
    </div>
    <!-- could not render calendar -->
    <div class="menuentity" id="userRecentEntries">
        <strong style="text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;">Recent
            Entries</strong>
        <ul style="padding-left: 0; margin-left: 0;">
            <li><a href="/users/laffer1/entry/33626" title="(no subject)">(no subject)</a></li>
            <li><a href="/users/laffer1/entry/33617" title="Testing out the .NET 4.5 JJ Windows client">Testing out the
                .NET 4.5 JJ Windows client</a></li>
            <li><a href="/users/laffer1/entry/33229" title="Testing Windows 8">Testing Windows 8</a></li>
            <li><a href="/users/laffer1/entry/32892" title="JJ crashes too much">JJ crashes too much</a></li>
            <li><a href="/users/laffer1/entry/29581" title="(no subject)">(no subject)</a></li>
        </ul>
    </div>
    <div class="menuentity" id="userlinks" style="padding-top: 10px;">
        <strong style="text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;"><i
                class="fa fa-external-link-square"></i> Links</strong>
        <ul style="padding-left: 0; margin-left: 0;">
            <li><a href="http://www.foolishgames.net" title="Fg| Et Clan">Fg| Et Clan</a></li>
            <li><a href="http://www.foolishgames.com" title="Foolish Games">Foolish Games</a></li>
            <li><a href="http://www.foolishgames.org" title="Foolish Games WoW Guild">Foolish Games WoW Guild</a></li>
            <li><a href="http://www.justjournal.com" title="Just Journal">Just Journal</a></li>
            <li><a href="http://www.midnightbsd.org" title="MidnightBSD">MidnightBSD</a></li>
            <li><a href="http://people.emich.edu/lholt3" title="My EMICH site">My EMICH site</a></li>
            <li><a href="http://lholt328.blogspot.com/" title="My English 328 Class Blog">My English 328 Class Blog</a>
            </li>
            <li><a href="http://et.splatterladder.com/?mod=claninfo&amp;idx=120615" title="SplatterLadder for Fg">SplatterLadder
                for Fg</a></li>
            <li><a href="http://stealthy.foolishgames.net/" title="Wolfenstein: The FrontLine">Wolfenstein: The
                FrontLine</a></li>
        </ul>
    </div>
    <div class="menuentity" id="archive" style="padding-top: 10px;"><strong
            style="text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;">Archive</strong>
        <ul style="padding-left: 0; margin-left: 0;">
            <li><a href="/users/laffer1/2014">2014 (0)</a></li>
            <li><a href="/users/laffer1/2013">2013 (2)</a></li>
            <li><a href="/users/laffer1/2012">2012 (2)</a></li>
            <li><a href="/users/laffer1/2011">2011 (27)</a></li>
            <li><a href="/users/laffer1/2010">2010 (64)</a></li>
            <li><a href="/users/laffer1/2009">2009 (98)</a></li>
            <li><a href="/users/laffer1/2008">2008 (181)</a></li>
            <li><a href="/users/laffer1/2007">2007 (387)</a></li>
            <li><a href="/users/laffer1/2006">2006 (578)</a></li>
            <li><a href="/users/laffer1/2005">2005 (366)</a></li>
            <li><a href="/users/laffer1/2004">2004 (10)</a></li>
            <li><a href="/users/laffer1/2003">2003 (168)</a></li>
        </ul>
    </div>
    <div class="menuentity" id="usertags" style="padding-top: 10px;">
        <strong style="text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;"><i
                class="fa fa-tags"></i> Tags</strong>

        <p style="padding-left: 0; margin-left: 0;">
            tag cloud here
        </p>
    </div>
</div>
<!-- Menu: End -->


<!-- Content: Begin -->
<div id="content">
    <c:if test="${authenticatedUsername != null}">
        <p>You are logged in as <a href="/users/${user.userName}"><img src="../../images/userclass_16.png"
                                                                       alt="user"> ${user.userName}</a>
        </p>
    </c:if>


</div>

<!-- Footer: Begin -->
<div id="footer">
    <a href="/" title="JustJournal.com: Online Journals">JustJournal.com</a>
</div>
<!-- Footer: End -->

</body>

</html>