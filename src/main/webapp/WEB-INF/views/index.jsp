<!doctype html>
<!--[if lt IE 7]>
<html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>
<html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>
<html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Just Journal: Free Online Journals</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
    <link rel="stylesheet" href="styles/bootstrap.min.css">
    <!-- build:css styles/main.css -->
    <link rel="stylesheet" href="components/angular-ui/build/angular-ui.css"/>
    <link rel="stylesheet" href="styles/main.css">
    <!-- endbuild -->
    <!-- IE 10 support -->
    <style type="text/css">
        @-webkit-viewport   { width: device-width; }
        @-moz-viewport      { width: device-width; }
        @-ms-viewport       { width: device-width; }
        @-o-viewport        { width: device-width; }
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
<body ng-app="wwwApp">
<!--[if lt IE 7]>
<p class="chromeframe">You are using an outdated browser. <a href="http://browsehappy.com/">Upgrade your browser
    today</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to better
    experience this site.</p>
<![endif]-->

<!--[if lt IE 9]>
<script src="components/es5-shim/es5-shim.js" type="text/javascript"></script>
<script src="components/json3/lib/json3.min.js" type="text/javascript"></script>
<![endif]-->

<!-- Header: Begin -->
<div id="header">
    <img src="images/jj_header.gif" alt="JustJournal" width="608" height="202" style="border:0;" usemap="#Map"/>
    <map name="Map" id="Map">
        <area shape="rect" coords="483,163,509,196" href="#/update" alt="Write"/>
        <area shape="rect" coords="514,163,544,198" href="#/search" alt="Search"/>
        <area shape="rect" coords="549,165,575,195" href="#/support" alt="Help"/>
        <area shape="rect" coords="135,93,392,146" href="#/" alt="JustJournal"/>
    </map>
</div>
<!-- Header: End -->

<!-- Add your site or application content here -->
<div class="container" ng-view></div>

<!-- Footer: Begin -->

<div id="footer">
    <p id="copyright">&#169; 2003 - 2014 Lucas Holt. All rights reserved.</p>

    <p><a href="#/privacy" title="Privacy Policy">Privacy</a> |
        <a href="#/search" title="Search">Search</a> |
        <a href="#/sitemap" title="Sitemap">Sitemap</a></p>
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

<!-- Footer: End -->

<script type="text/javascript" src="components/jquery/jquery.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="components/angular/angular.js"></script>
<script type="text/javascript" src="components/angular-resource/angular-resource.js"></script>
<script type="text/javascript" src="components/angular-cookies/angular-cookies.js"></script>
<script type="text/javascript" src="components/angular-sanitize/angular-sanitize.js"></script>

<script type="text/javascript" src="components/angular-ui/build/angular-ui.js"></script>

<!-- build:js scripts/scripts.js -->
<script type="text/javascript" src="scripts/app.js"></script>
<script type="text/javascript" src="scripts/controllers/main.js"></script>
<script type="text/javascript" src="scripts/controllers/cancel.js"></script>
<script type="text/javascript" src="scripts/controllers/memberslist.js"></script>
<script type="text/javascript" src="scripts/controllers/moodlist.js"></script>
<script type="text/javascript" src="scripts/controller/profile.js"></script>
<script type="text/javascript" src="scripts/controller/stats.js"></script>
<script type="text/javascript" src="scripts/controllers/sitemap.js"></script>
<script type="text/javascript" src="scripts/controllers/tags.js"></script>
<script type="text/javascript" src="scripts/controllers/update.js"></script>

<script type="text/javascript" src="scripts/services/accountservice.js"></script>
<script type="text/javascript" src="scripts/services/friendservice.js"></script>
<script type="text/javascript" src="scripts/services/locationservice.js"></script>
<script type="text/javascript" src="scripts/services/memberservice.js"></script>
<script type="text/javascript" src="scripts/services/moodervice.js"></script>
<script type="text/javascript" src="scripts/services/securityservice.js"></script>
<script type="text/javascript" src="scripts/services/statisticsservice.js"></script>
<script type="text/javascript" src="scripts/services/tagservice.js"></script>
<!-- endbuild -->

<script>
    var _gaq = [
        ['_setAccount', 'UA-560995-1'],
        ['_trackPageview']
    ];
    (function (d, t) {
        var g = d.createElement(t), s = d.getElementsByTagName(t)[0];
        g.src = ('https:' == location.protocol ? '//ssl' : '//www') + '.google-analytics.com/ga.js';
        s.parentNode.insertBefore(g, s)
    }(document, 'script'));
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
