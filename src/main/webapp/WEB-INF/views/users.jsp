<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <title><c:out value="${user.userPref.journalName}"/></title>
    <c:if test="${user.userPref.allowSpider == 'Y'}">
        <meta name="robots" content="noindex, nofollow, noarchive">
        <meta name="googlebot" content="nosnippet">
    </c:if>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/users.css">
    <!-- TODO: write new themes <link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/styles/<c:out value="${user.userPref.style}"/>.css">
          -->
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" media="screen"
          href="${pageContext.request.contextPath}/components/lightbox2/css/lightbox.css">
    <link rel="alternate" type="application/rss+xml" title="RSS"
          href="http://www.justjournal.com/users/<c:out value="${user.username}"/>/rss">
    <link rel="alternate" type="application/atom+xml" title="Atom"
          href="http://www.justjournal.com/users/<c:out value="${user.username}"/>/atom">
    <link rel="EditURI" type="application/rsd+xml" title="RSD"
          href="http://www.justjournal.com/rsd?blogID=<c:out value="${user.username}"/>">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/switchcontent.js" defer></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/components/jquery/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/components/jquery-ui/ui/minified/jquery-ui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/components/lightbox2/js/lightbox-2.6.min.js" defer></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</head>

<body>

<div class="container">

    <header>
        <div class="row">
            <div class="navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse"
                                data-target=".navbar-collapse">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="${pageContext.request.contextPath}/#!/" title="JustJournal">
                            JustJournal
                        </a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <ul class="nav navbar-nav">
                            <li <c:if test="${entries != null}">class="active"</c:if>>
                                <a title="Home"
                                   href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>"><i
                                        class="fa fa-home"></i></a>
                            </li>
                            <li
                                    <c:if test="${calendar != null}">class="active"</c:if>
                                    <c:if test="${startYear > 2002}">class="active"</c:if>>
                                <a title="Calendar"
                                   href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/calendar"><i
                                        class="fa fa-calendar"></i></a>
                            </li>
                            <li <c:if test="${friends != null}">class="active"</c:if>>
                                <a title="Friends"
                                   href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/friends"><i
                                        class="fa fa-group"></i></a>
                            </li>
                            <li <c:if test="${pictures != null}">class="active"</c:if>>
                                <a title="Photos"
                                   href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/pictures"><i
                                        class="fa fa-picture-o"></i></a>
                            </li>
                            <li <c:if test="${subscriptions != null}">class="active"</c:if>>
                                <a title="Feed Reader"
                                   href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/subscriptions"><i
                                        class="fa fa-rss"></i>r</a></li>

                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Formats <b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a rel="alternate"
                                           href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/rss"><i
                                            class="fa fa-rss"></i> RSS</a></li>
                                    <li><a rel="alternate"
                                           href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/atom"><i
                                            class="fa fa-rss"></i>
                                        ATOM</a></li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/pdf"><i
                                                class="fa fa-download"></i> PDF</a>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/rtf"><i
                                                class="fa fa-download"></i> RTF</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <form class="navbar-form navbar-left" role="search" method="get"
                                      action="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>/search">
                                    <input type="hidden" name="max" value="20">

                                    <div class="form-group">
                                        <input type="search" name="bquery" id="bquery"
                                               class="form-control input-sm search-query"
                                               placeholder="Search">
                                    </div>
                                </form>
                            </li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="${pageContext.request.contextPath}/#!/entry" title="New Entry"><i
                                    class="fa fa-pencil-square"></i> New Entry</a></li>
                            <li>
                                <a href="${pageContext.request.contextPath}/#!/profile/<c:out value="${user.username}"/>"><i
                                        class="fa fa-user"></i> Profile</a>
                            </li>

                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" title="Login & Settings"><i
                                        class="fa fa-cog"></i> <b
                                        class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <c:if test="${authenticatedUsername == null}">
                                        <li><a href="${pageContext.request.contextPath}/#/"><i
                                                class="fa fa-sign-in"></i> Login</a>
                                        </li>
                                    </c:if>
                                    <c:if test="${authenticatedUsername != null}">
                                        <li><a href="${pageContext.request.contextPath}/logout"><i
                                                class="fa fa-sign-out"></i>
                                            Log
                                            Out</a></li>
                                    </c:if>
                                </ul>
                            </li>
                            <li>&nbsp;</li>
                        </ul>
                    </div>
                    <!--/.nav-collapse -->
                </div>
            </div>
        </div>
    </header>

    <div class="row">
        <section>
            <div id="menu" class="col-xs-6 col-md-3">

                <p><img class="img-rounded img-responsive" alt="avatar"
                        src="${pageContext.request.contextPath}/image?id=<c:out value="${user.id}"/>">
                </p>

                <c:out escapeXml="false" value="${recentEntries}"/>

                <c:out escapeXml="false" value="${links}"/>

                <c:out escapeXml="false" value="${archive}"/>

                <c:out escapeXml="false" value="${taglist}"/>

                <c:out escapeXml="false" value="${calendarMini}"/>
            </div>
        </section>

        <section>
            <div id="content" class="col-xs-12 col-md-8 col-md-offset-1">
                <div class="page-header">
                    <h1><c:out value="${user.userPref.journalName}"/></h1>
                </div>

                <c:if test="${authenticatedUsername != null}">
                    <p>You are logged in as <a
                            href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>"><img
                            src="${pageContext.request.contextPath}/images/userclass_16.png"
                            alt="user"> <c:out value="${user.username}"/></a>
                    </p>
                </c:if>

                <c:if test="${pageable != null}">
                    <ul class="pager">
                        <li class="previous">
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>?page=<c:out value="${pageable.getPageNumber() + 2}"/>">&larr;
                                Older</a>
                        </li>
                        <li class="next <c:if test="pageable.getPageNumber() == 1">disabled</c:if>">
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>?page=<c:out value="${pageable.getPageNumber()}"/>">Newer &rarr;</a>
                        </li>
                    </ul>
                </c:if>

                <c:out escapeXml="false" value="${entries}"/>

                <c:if test="${pageable != null}">
                    <ul class="pager">
                        <li class="previous">
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>?page=<c:out value="${pageable.getPageNumber() + 2}"/>">&larr;
                                Older</a>
                        </li>
                        <li class="next <c:if test="pageable.getPageNumber() == 0">disabled</c:if>">
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.username}"/>?page=<c:out value="${pageable.getPageNumber()}"/>">Newer &rarr;</a>
                        </li>
                    </ul>
                </c:if>

                <c:out escapeXml="false" value="${entry}"/>
                <c:out escapeXml="false" value="${friends}"/>
                <c:out escapeXml="false" value="${pictures}"/>
                <c:out escapeXml="false" value="${search}"/>
                <c:out escapeXml="false" value="${subscriptions}"/>
                <c:out escapeXml="false" value="${tags}"/>
                <c:out escapeXml="false" value="${calendar}"/>

                <c:if test="${currentYear > 0}">
                    <p>The calendar lists months with journal entries.</p>

                    <p>
                        <c:forEach begin="${startYear}" end="${currentYear}" var="yr">
                            <a href="../<c:out value="${yr}"/>"><c:out value="${yr}"/></a>
                        </c:forEach>
                    </p>
                </c:if>
            </div>
        </section>
    </div>
</div>

<script>
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
            (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date();
        a = s.createElement(o),
                m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

    ga('create', 'UA-560995-1', 'justjournal.com');
    ga('require', 'linkid', 'linkid.js');
    ga('send', 'pageview');
</script>

</body>

</html>