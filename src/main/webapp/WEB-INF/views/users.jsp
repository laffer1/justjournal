<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>

<head>
    <title><c:out value="${user.journalName}"/></title>
    <c:if test="${user.spiderAllowed == true}">
        <meta name="robots" content="noindex, nofollow, noarchive">
        <meta name="googlebot" content="nosnippet">
    </c:if>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/users.css">
    <!-- TODO: write new themes <link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/styles/<c:out value="${user.styleId}"/>.css">
          -->
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" media="screen"
          href="${pageContext.request.contextPath}/components/lightbox2/css/lightbox.css">
    <link rel="alternate" type="application/rss+xml" title="RSS"
          href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/rss">
    <link rel="alternate" type="application/atom+xml" title="Atom"
          href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/atom">
    <link rel="EditURI" type="application/rsd+xml" title="RSD"
          href="http://www.justjournal.com/rsd?blogID=<c:out value="${user.userName}"/>">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/switchcontent.js" defer></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/components/jquery/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/components/jquery-ui/ui/minified/jquery-ui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/components/lightbox2/js/lightbox-2.6.min.js" defer></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
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
                        <a class="navbar-brand" href="${pageContext.request.contextPath}/#/">JustJournal</a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <ul class="nav navbar-nav">
                            <li <c:if test="${entries != null}">class="active"</c:if>>
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>"><i
                                        class="fa fa-home"></i> Home</a>
                            </li>
                            <li <c:if test="${calendar != null}">class="active"</c:if>>
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/calendar"><i
                                        class="fa fa-calendar"></i> Calendar</a>
                            </li>
                            <li <c:if test="${friends != null}">class="active"</c:if>>
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/friends"><i
                                        class="fa fa-group"></i> Friends</a>
                            </li>
                            <li <c:if test="${pictures != null}">class="active"</c:if>>
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/pictures"><i
                                        class="fa fa-picture-o"></i> Pictures</a>
                            </li>
                            <li <c:if test="${subscriptions != null}">class="active"</c:if>>
                            <a href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/subscriptions"><i
                                        class="fa fa-rss"></i> RSS
                                    Reader</a></li>

                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Formats <b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a rel="alternate"
                                           href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/rss"><i
                                            class="fa fa-rss"></i> RSS</a></li>
                                    <li><a rel="alternate"
                                           href="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/atom"><i
                                            class="fa fa-rss"></i>
                                        ATOM</a></li>
                                    <li>
                                        <a href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/pdf"><i
                                                class="fa fa-download"></i> PDF</a>
                                    </li>
                                    <li>
                                        <a href="http://www.justjournal.com/users/<c:out value="${user.userName}"/>/rtf"><i
                                                class="fa fa-download"></i> RTF</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <form class="navbar-form navbar-left" role="search" method="get"
                                      action="${pageContext.request.contextPath}/users/<c:out value="${user.userName}"/>/search">
                                    <div class="form-group">
                                        <input type="search" name="bquery" id="bquery" class="form-control input-sm"
                                               placeholder="Search">
                                    </div>
                                    <button type="submit" class="btn btn-default btn-sm"><i class="fa fa-search"></i>
                                        Search
                                    </button>
                                </form>
                            </li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="${pageContext.request.contextPath}/#/entry"><i
                                    class="glyphicon glyphicon-pencil"></i> New Entry</a></li>
                            <li>
                                <a href="${pageContext.request.contextPath}/#/profile/<c:out value="${user.userName}"/>"><i
                                        class="fa fa-user"></i> Profile</a>
                            </li>
                            <c:if test="${authenticatedUsername == null}">
                                <li><a href="${pageContext.request.contextPath}/#/"><i class="fa-sign-in"></i> Login</a>
                                </li>
                            </c:if>
                            <c:if test="${authenticatedUsername != null}">
                                <li><a href="${pageContext.request.contextPath}/logout"><i class="fa-sign-out"></i> Log
                                    Out</a></li>
                            </c:if>
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
            <div id="menu" class="col-xs-6 col-md-4">
            <c:if test="${user.showAvatar == true}">
                <p><img class="img-rounded img-responsive" alt="avatar"
                        src="${pageContext.request.contextPath}/image?id=<c:out value="${user.userId}"/>">
                </p>
            </c:if>

            <c:out escapeXml="false" value="${calendarMini}"/>

            <c:out escapeXml="false" value="${recentEntries}"/>

            <c:out escapeXml="false" value="${links}"/>

            <c:out escapeXml="false" value="${archive}"/>

            <c:out escapeXml="false" value="${taglist}"/>
        </div>
        </section>

        <section>
            <div id="content" class="col-xs-12 col-md-8">
            <div class="page-header">
                <h1><c:out value="${user.journalName}"/></h1>
            </div>

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

</body>

</html>