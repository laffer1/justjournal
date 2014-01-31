<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <title><c:out value="${user.journalName}"/></title>
    <link rel="stylesheet" type="text/css" href="../../styles/users.css">
    <link rel="stylesheet" type="text/css" media="screen" href="../../styles/<c:out value="${user.styleId}"/>.css">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link rel="alternate" type="application/rss+xml" title="RSS"
          href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/rss">
    <link rel="alternate" type="application/atom+xml" title="Atom"
          href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/atom">
    <link rel="EditURI" type="application/rsd+xml" title="RSD"
          href="http://www.justjournal.com/rsd?blogID=<c:out value="${user.userName}"/>">
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
    <h1><c:out value="${user.journalName}"/></h1>
</div>
<!-- Header: End -->

<!-- Menu: Begin -->
<div id="menu">
    <c:if test="${user.showAvatar == true}">
        <img alt="avatar" src="../../image?id=<c:out value="${user.userId}"/>">
    </c:if>

    <p id="muser">
        <a href="/users/<c:out value="${user.userName}"/>">Journal Entries</a><br/>
        <a href="/users/<c:out value="${user.userName}"/>/calendar">Calendar</a><br/>
        <a href="/users/<c:out value="${user.userName}"/>/friends">Friends</a><br/>
        <a href="/users/<c:out value="${user.userName}"/>/pictures">Pictures</a><br/>
        <a href="/profile.jsp?user=<c:out value="${user.userName}"/>">Profile</a><br/>
    </p>

    <p id="mgen">
        <a href="/#/entry">Update Journal</a><br/>
        <a href="/">Login</a>
    </p>

    <p id="mrssreader">
        <a href="/users/<c:out value="${user.userName}"/>/subscriptions">RSS Reader</a><br/>
    </p>

    <div id="mformats"><strong
            style="text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 10px; width: 100%; font-size: 10px;">Formats</strong><br/>

        <p><a rel="alternate" href="/users/<c:out value="${user.userName}"/>/rss"><i class="fa fa-rss"></i> RSS</a><br/>
            <a rel="alternate" href="/users/<c:out value="${user.userName}"/>/atom"><i class="fa fa-rss"></i>
                ATOM</a><br/>
            <img src="../../images/icon_pdf.gif" alt="PDF"/><a
                    href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/pdf">PDF</a><br/>
            <img src="../../images/icon_rtf.gif" alt="RTF"/><a
                    href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/rtf">RTF</a><br/>
        </p>
    </div>
    <div id="msearchbox"><strong
            style="text-transform: uppercase; letter-spacing: 2px; border: 0 none; border-bottom: 1px; border-style: dotted; border-color: #999999; margin-bottom: 5px; width: 100%; font-size: 10px;">Search</strong>

        <form id="msearch" action="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/search"
              method="get">
        <p><input type="text" name="bquery" id="bquery" style="width: 90px;"/><br/>
                <input type="submit" name="search" id="searchbtn" value="Search Blog"/></p>
        </form>
    </div>

    <c:out value="${calendarMini}"/>

    <c:out value="${recentEntries}"/>

    <c:out value="${links}"/>

    <c:out value="${archive}"/>

    <c:out value="${taglist}"/>
</div>
<!-- Menu: End -->


<!-- Content: Begin -->
<div id="content">
    <c:if test="${authenticatedUsername != null}">
        <p>You are logged in as <a href="/users/<c:out value="${user.userName}"/>"><img
                src="../../images/userclass_16.png"
                alt="user"> <c:out value="${user.userName}"/></a>
        </p>
    </c:if>

    <c:out value="${entries}"/>
    <c:out value="${entry}"/>
    <c:out value="${friends}"/>
    <c:out value="${pictures}"/>
    <c:out value="${search}"/>
    <c:out value="${subscriptions}"/>
    <c:out value="${tags}"/>
    <c:out value="${calendar}"/>
</div>

<!-- Footer: Begin -->
<div id="footer">
    <a href="/" title="JustJournal.com: Online Journals">JustJournal.com</a>
</div>
<!-- Footer: End -->

</body>

</html>