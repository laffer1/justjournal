<!doctype html>
<!--[if lt IE 7]>
<html class="no-js lt-ie9 lt-ie8 lt-ie7" ng-app="wwwApp"> <![endif]-->
<!--[if IE 7]>
<html class="no-js lt-ie9 lt-ie8" ng-app="wwwApp"> <![endif]-->
<!--[if IE 8]>
<html class="no-js lt-ie9" ng-app="wwwApp"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" ng-app="wwwApp"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Just Journal: Free Online Journals</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    <meta name="msvalidate.01" content="D1E040678EBA2490CC51CABAABCE7928">
    <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/bootstrap.min.css">
    <!-- build:css styles/main.css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/angular-ui/build/angular-ui.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
    <!-- endbuild -->
    <!-- IE 10 support -->
    <style type="text/css">
        /*noinspection ALL*/
        @-webkit-viewport   { width: device-width; }
        /*noinspection ALL*/
        @-moz-viewport      { width: device-width; }
        /*noinspection ALL*/
        @-ms-viewport       { width: device-width; }
        /*noinspection ALL*/
        @-o-viewport        { width: device-width; }
        /*noinspection ALL*/
        @viewport           { width: device-width; }
    </style>
    <script type="text/javascript">
        if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
          var msViewportStyle = document.createElement("style");
          msViewportStyle.appendChild(
            document.createTextNode(
              "@-ms-viewport{width:auto!important}"
            )
          );
          document.getElementsByTagName("head")[0].appendChild(msViewportStyle);
        }
    </script>
</head>

<body data-ng-controller="wwwAppCtrl">
<!--[if lt IE 7]>
<p class="chromeframe">You are using an outdated browser. <a href="http://browsehappy.com/">Upgrade your browser
    today</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to better
    experience this site.</p>
<![endif]-->

<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/components/es5-shim/es5-shim.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/components/json3/lib/json3.min.js" type="text/javascript"></script>
<![endif]-->

<!-- Header: Begin -->
<header>
    <div id="header">
        <a href="${pageContext.request.contextPath}#/"><img
                src="${pageContext.request.contextPath}/images/jj_header.gif" alt="JustJournal" width="608"></a>
    </div>
</header>
<!-- Header: End -->

<!-- Add your site or application content here -->
<div class="container" ng-view></div>

<!-- Footer: Begin -->

<footer>
<div id="footer">
    <p id="copyright">&#169; 2003 - 2014 Lucas Holt. All rights reserved.</p>

    <p><a href="${pageContext.request.contextPath}#/privacy" title="Privacy Policy">Privacy</a> |
        <a href="${pageContext.request.contextPath}#/search" title="Search">Search</a> |
        <a href="${pageContext.request.contextPath}#/sitemap" title="Sitemap">Sitemap</a></p>
</div>

<div style="text-align: center; margin: auto;">
    <script type="text/javascript"><!--
    google_ad_client = "pub-1321195614665440";
    /* 468x60, created 8/27/08 */
    google_ad_slot = "8080888486";
    google_ad_width = 468;
    google_ad_height = 60;
    //-->
    </script>
    <script type="text/javascript" src="//pagead2.googlesyndication.com/pagead/show_ads.js">
    </script>
</div>
</footer>
<!-- Footer: End -->

<script type="text/javascript" src="${pageContext.request.contextPath}components/jquery/jquery.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}components/jquery.validation/jquery.validate.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}components/angular/angular.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}components/angular-resource/angular-resource.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}components/angular-route/angular-route.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}components/angular-cookies/angular-cookies.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}components/angular-sanitize/angular-sanitize.js"></script>

<script type="text/javascript"
        src="${pageContext.request.contextPath}components/angular-ui/build/angular-ui.js"></script>

<!-- build:js scripts/scripts.js -->
<script type="text/javascript" src="scripts/app.js"></script>
<script type="text/javascript" src="scripts/controllers/wwwApp.js"></script>
<script type="text/javascript" src="scripts/controllers/main.js"></script>
<script type="text/javascript" src="scripts/controllers/bugs.js"></script>
<script type="text/javascript" src="scripts/controllers/cancel.js"></script>
<script type="text/javascript" src="scripts/controllers/entry.js"></script>
<script type="text/javascript" src="scripts/controllers/faq.js"></script>
<script type="text/javascript" src="scripts/controllers/members.js"></script>
<script type="text/javascript" src="scripts/controllers/moodlist.js"></script>
<script type="text/javascript" src="scripts/controllers/privacy.js"></script>
<script type="text/javascript" src="scripts/controllers/profile.js"></script>
<script type="text/javascript" src="scripts/controllers/search.js"></script>
<script type="text/javascript" src="scripts/controllers/sitemap.js"></script>
<script type="text/javascript" src="scripts/controllers/stats.js"></script>
<script type="text/javascript" src="scripts/controllers/support.js"></script>
<script type="text/javascript" src="scripts/controllers/tags.js"></script>
<script type="text/javascript" src="scripts/controllers/tech.js"></script>

<script type="text/javascript" src="scripts/services/AccountService.js"></script>
<script type="text/javascript" src="scripts/services/BiographyService.js"></script>
<script type="text/javascript" src="scripts/services/EntryService.js"></script>
<script type="text/javascript" src="scripts/services/FriendService.js"></script>
<script type="text/javascript" src="scripts/services/LocationService.js"></script>
<script type="text/javascript" src="scripts/services/LoginService.js"></script>
<script type="text/javascript" src="scripts/services/MemberService.js"></script>
<script type="text/javascript" src="scripts/services/MoodService.js"></script>
<script type="text/javascript" src="scripts/services/SecurityService.js"></script>
<script type="text/javascript" src="scripts/services/StatisticsService.js"></script>
<script type="text/javascript" src="scripts/services/TagService.js"></script>
<!-- endbuild -->

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
<script type="text/javascript">
    (function () {
        var po = document.createElement('script');
        po.type = 'text/javascript';
        po.async = true;
        po.src = 'https://apis.google.com/js/client:plusone.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(po, s);
    })();
</script>
</body>
</html>
