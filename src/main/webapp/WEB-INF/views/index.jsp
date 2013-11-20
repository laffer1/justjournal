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
    <!-- build:css styles/main.css -->
    <link rel="stylesheet" href="styles/bootstrap.css">
    <link rel="stylesheet" href="components/angular-ui/build/angular-ui.css"/>
    <link rel="stylesheet" href="styles/main.css">

    <!-- endbuild -->
</head>
<body ng-app="wwwApp">
<!--[if lt IE 7]>
<p class="chromeframe">You are using an outdated browser. <a href="http://browsehappy.com/">Upgrade your browser
    today</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to better
    experience this site.</p>
<![endif]-->

<!--[if lt IE 9]>
<script src="components/es5-shim/es5-shim.js"></script>
<script src="components/json3/lib/json3.min.js"></script>
<![endif]-->

<!-- Header: Begin -->
<div id="header">
    <img src="images/jj_header.gif" alt="JustJournal" width="608" height="202" style="border:0;" usemap="#Map"/>
    <map name="Map" id="Map">
        <area shape="rect" coords="483,163,509,196" href="/update.jsp" alt="Write"/>
        <area shape="rect" coords="514,163,544,198" href="/search/index.jsp" alt="Search"/>
        <area shape="rect" coords="549,165,575,195" href="/support/index.jsp" alt="Help"/>
        <area shape="rect" coords="135,93,392,146" href="/index.jsp" alt="JustJournal"/>
    </map>
</div>
<!-- Header: End -->

<!-- Add your site or application content here -->
<div class="container" ng-view></div>

<!-- Footer: Begin -->

<div id="footer">
	<p id="copyright">&#169; 2003 - 2013 Lucas Holt.  All rights reserved.</p>
	<p><a href="/privacy.jsp" title="Privacy Policy">Privacy</a> |
	   <a href="/search/index.jsp" title="Search">Search</a>
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
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
</div>

<!-- Footer: End -->

<script src="components/angular/angular.js"></script>
<script src="components/angular-resource/angular-resource.js"></script>
<script src="components/angular-cookies/angular-cookies.js"></script>
<script src="components/angular-sanitize/angular-sanitize.js"></script>

<script src="components/angular-ui/build/angular-ui.js"></script>

<!-- build:js scripts/scripts.js -->
<script src="scripts/app.js"></script>
<script src="scripts/controllers/main.js"></script>
<!-- endbuild -->

<!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
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
      (function() {
       var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
       po.src = 'https://apis.google.com/js/client:plusone.js';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
     })();
    </script>
</body>
</html>
