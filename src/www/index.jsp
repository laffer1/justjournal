<%@ page import="com.justjournal.core.Statistics" %>
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>Just Journal: Free Online Journals</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>
    <link rel="alternate" type="application/rss+xml" href="RecentBlogs" title="Recent JJ Blog Posts"/>
    <script src="js/ticker.js" type="text/javascript">/* ie7 sucks*/</script>
    <style type="text/css">
        #quickmenu {
            float: right;
            width: 200px;
            height: 300px;
        }

        #blurb {
            float: left;
            width: 380px;
            height: 300px;
        }

        #ticker {
            width: 600px;
            margin-top: 5px;
            margin-bottom: 5px;
        }

        #ticker table {
            background: transparent url( images/ticker744wide_btm.gif ) no-repeat bottom left;
            border: 0;
            margin: 0;
            padding: 0;
        }

        #ticker table td {
            font-size: 12px;
        }

        #ticker table td#tic-title {
            background: transparent url( images/ticker_top.gif ) no-repeat top left;
            font-size: 12px;
            font-weight: bold;
            padding: 4px 0;
            text-align: center;
            vertical-align: top;
            width: 184px;
        }

        #ticker table td#tic-item {
            background: transparent url( images/ticker_top.gif ) no-repeat top right;
            padding: 4px 10px 4px 16px;
            width: 534px;
            text-align: left;
        }

        #ticker table td#tic-item a {
            opacity: 0.999;
        }

        #ticker table a {
            color: #222;
            text-decoration: none;
        }

        #ticker table a:hover {
            color: #111;
            text-decoration: underline;
        }

        body {
            width: 700px;
        }

        #content {
            border: 0 none;
            padding-top: 0;
        }

        .firstone:first-letter {
            color: white;
            background: #993366;
            margin: 3px;
            padding: 2px;
        }

        .firstone {
            color: #993366;
            padding: 10px;
            background-color: white;
            font: HelveticaNeuve, "Helvetica Neuve", Helvetica, Arial, sans-serif;
        }
    </style>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">

    <jsp:include page="inc_login.jsp" flush="false"/>

    <div id="quickmenu">
        <ul>
            <li><a href="create.jsp">Create Account</a></li>
            <li><a href="cancel.jsp">Cancel Account</a></li>
            <li><a href="http://www.cafepress.com/justjournal">Purchase JJ Merchandise</a></li>
            <li><a href="memberlist.jsp">Member List</a></li>
            <li><a href="users/jjsite">Site Journal</a></li>
            <li><a href="opensource/index.jsp">Open Source</a></li>
        </ul>

        <form action="https://www.paypal.com/cgi-bin/webscr" method="post">
            <p>
                <input type="hidden" name="cmd" value="_s-xclick"/>
                <input type="image" src="https://www.paypal.com/en_US/i/btn/x-click-but21.gif" name="submit"
                       alt="Make payments with PayPal - it's fast, free and secure!"/>
                <input type="hidden" name="encrypted" value="-----BEGIN PKCS7-----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-----END PKCS7-----
    "/>
            </p>
        </form>

        <p><a href="software/index.jsp">Download</a> the client software. </p>

        <%
            Statistics s = new Statistics();
        %>
        <table style="border: thin solid #F2F2F2;">
            <tr style="font-size: 10px; background: #F2F2F2;">
                <td>Users</td>
                <td><%=s.users()%></td>
            </tr>
            <tr style="font-size: 10px;">
                <td>Entries</td>
                <td><%=s.entries()%></td>
            </tr>
            <tr style="font-size: 10px; background: #F2F2F2;">
                <td>Comments</td>
                <td><%=s.comments()%></td>
            </tr>
            <tr style="font-size: 10px;">
                <td>Blog Styles</td>
                <td><%=s.styles()%></td>
            </tr>
        </table>

        <p><a href="RecentBlogs"><img src="images/rss2.gif" alt="Recent Blogs RSS"/></a></p>
    </div>

    <div id="blurb">
        <p class="firstone"
           style="background: white url(images/firstone.png) no-repeat; text-align: justify; line-height: 1.8em; margin-right: 30px; padding-top: 0">
            Just
            Journal is an online journal service, also
            known as a "blog." You can publish private
            entries for yourself, friends' entries to share with those close to you or public entries you wish to share
            with the Internet.</p>

        <p style=" padding-bottom: 10px"><a href="create.jsp"><img src="images/jj_btn_create_an_account.gif"
                                                                   alt="Create an account"/></a></p>
    </div>

    <p style="clear: both;"></p>

    <div id="ticker"></div>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
