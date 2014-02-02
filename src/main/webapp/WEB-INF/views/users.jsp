<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <title><c:out value="${user.journalName}"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/users.css">
    <link rel="stylesheet" type="text/css" media="screen"
          href="${pageContext.request.contextPath}/styles/<c:out value="${user.styleId}"/>.css">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link rel="alternate" type="application/rss+xml" title="RSS"
          href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/rss">
    <link rel="alternate" type="application/atom+xml" title="Atom"
          href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/atom">
    <link rel="EditURI" type="application/rsd+xml" title="RSD"
          href="http://www.justjournal.com/rsd?blogID=<c:out value="${user.userName}"/>">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/switchcontent.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/components/jquery/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/components/jquery-ui/ui/minified/jquery-ui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lightbox.js"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/lightbox.css">
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
        <img alt="avatar" src="${pageContext.request.contextPath}image?id=<c:out value="${user.userId}"/>">
    </c:if>

    <p id="muser">
        <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>">Journal Entries</a><br/>
        <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/calendar">Calendar</a><br/>
        <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/friends">Friends</a><br/>
        <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/pictures">Pictures</a><br/>
        <a href="${pageContext.request.contextPath}profile.jsp?user=<c:out value="${user.userName}"/>">Profile</a><br/>
    </p>

    <p id="mgen">
        <a href="${pageContext.request.contextPath}#/entry">Update Journal</a><br/>
        <a href="${pageContext.request.contextPath}">Login</a>
    </p>

    <p id="mrssreader">
        <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/subscriptions">RSS
            Reader</a><br/>
    </p>

    <div id="mformats"><strong
            style="text-transform: uppercase; letter-spacing: 2px; border: 0 dotted #999999;border-bottom-width: 1px;margin-bottom: 10px; width: 100%; font-size: 10px;">Formats</strong><br/>

        <p><a rel="alternate" href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/rss"><i
                class="fa fa-rss"></i> RSS</a><br/>
            <a rel="alternate" href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/atom"><i
                    class="fa fa-rss"></i>
                ATOM</a><br/>
            <img src="${pageContext.request.contextPath}/images/icon_pdf.gif" alt="PDF"/><a
                    href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/pdf">PDF</a><br/>
            <img src="${pageContext.request.contextPath}/images/icon_rtf.gif" alt="RTF"/><a
                    href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/rtf">RTF</a><br/>
        </p>
    </div>
    <div id="msearchbox"><strong
            style="text-transform: uppercase; letter-spacing: 2px; border: 0 dotted #999999;border-bottom-width: 1px;margin-bottom: 5px; width: 100%; font-size: 10px;">Search</strong>

        <form id="msearch" action="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/search"
              method="get">
            <p><input type="search" name="bquery" id="bquery" style="width: 90px;" placeholder="Search"><br>
                <input type="submit" name="search" id="searchbtn" value="Search Blog"></p>
        </form>
    </div>

    <c:out escapeXml="false" value="${calendarMini}"/>

    <c:out escapeXml="false" value="${recentEntries}"/>

    <c:out escapeXml="false" value="${links}"/>

    <c:out escapeXml="false" value="${archive}"/>

    <c:out escapeXml="false" value="${taglist}"/>
</div>
<!-- Menu: End -->


<!-- Content: Begin -->
<div id="content">
    <c:if test="${authenticatedUsername != null}">
        <p>You are logged in as <a
                href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>"><img
                src="${pageContext.request.contextPath}/images/userclass_16.png"
                alt="user"> <c:out value="${user.userName}"/></a>
        </p>
    </c:if>

    <c:out escapeXml="false" value="${entries}"/>
    <c:out escapeXml="false" value="${entry}"/>
    <c:out escapeXml="false" value="${friends}"/>
    <c:out escapeXml="false" value="${pictures}"/>
    <c:out escapeXml="false" value="${search}"/>
    <c:out escapeXml="false" value="${subscriptions}"/>
    <c:out escapeXml="false" value="${tags}"/>
    <c:out escapeXml="false" value="${calendar}"/>

    <c:if test="${startYear} != null">
    <p>The calendar lists months with journal entries.</p>

        <p>
            <c:forEach begin="${startYear}" end="${currentYear}" var="yr">
                <a href="../${yr}">${yr}</a>
            </c:forEach>
        </p>
    </c:if>
</div>

<!-- Footer: Begin -->
<div id="footer">
    <a href="${pageContext.request.contextPath}" title="JustJournal.com: Online Journals">JustJournal.com</a>
</div>
<!-- Footer: End -->

</body>

</html>