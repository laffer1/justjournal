<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.WebError" %>
<%@ page import="com.justjournal.core.Statistics" %>
<%@ page import="com.justjournal.db.*" %>
<%@ page import="com.justjournal.search.BaseSearch" %>
<%@ page import="com.justjournal.utility.StringUtil" %>
<%@ page import="com.justjournal.utility.Xml" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.ParsePosition" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
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
    float: left;
    width: 200px;
}

#quickmenu ul {
    margin-left: 0;
    padding-left: 0;
}

#quickmenu ul li {
    list-style-type: none;
    padding-bottom: .4em;
    padding-left: 0;
    margin-left: 0;
    line-height: 1.2em;
}

#blurb {
    float: right;
    width: 380px;
    height: 300px;
}

#ticker {
    background-color: #F2F2F2;
    border: thin solid silver;
    -moz-border-radius: 10px;
    -webkit-border-radius: 10px;
    width: 600px;
    margin-top: 5px;
    margin-bottom: 5px;
    margin: 0;
    padding: 0;
}

#ticker table {
/* background: transparent url( images/ticker744wide_btm.gif ) no-repeat bottom left;  */
    border: 0;
    margin: 0;
    padding: 0;
}

#ticker table td {
    font-size: 12px;
}

#ticker table td#tic-title {
/*  background: transparent url( images/ticker_top.gif ) no-repeat top left;  */
    font-size: 12px;
    font-weight: bold;
    padding: 4px 0;
    text-align: center;
    vertical-align: top;
    width: 184px;
}

#ticker table td#tic-item {
/*  background: transparent url( images/ticker_top.gif ) no-repeat top right;   */
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
    font-family: HelveticaNeuve, "Helvetica Neuve", Helvetica, Arial, sans-serif;
}

#wrapper {
    font-size: 1.2em;
    margin: 0 auto;
    text-align: left; /*width: 490px; */
}

#wrapper div.imgholder {
    margin-bottom: 15px;
}

#wrapper div.imgholder div.text {
    margin-top: 2px;
    padding: 2px;
}

#wrapper img {
    border: 5px solid #E6E2CF;
}
</style>
<link rel="stylesheet" type="text/css" href="default.css" id="default"/>
<!-- dummy stylesheet - href to be swapped -->
<link rel="stylesheet" type="text/css" href="dummy.css" id="dummy_css"/>
<script type="text/javascript" src="js/applesearch.js">/* ie7 sucks*/</script>

<script type="text/javascript">
    addEventToObject(window, 'onload', applesearch.init);
</script>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">

<div id="quickmenu">
    <jsp:include page="inc_login.jsp" flush="false"/>
    <ul>
        <li><a href="create.jsp">Create Account</a></li>
        <li><a href="cancel.jsp">Cancel Account</a></li>
        <li><a href="http://www.cafepress.com/justjournal">Purchase JJ Merchandise</a></li>
        <li><a href="memberlist.jsp">Member List</a></li>
        <li><a href="mob/index.jsp" title="Just Journal Mobile for Cell Phones">Just Journal Mobile</a></li>
        <li><a href="opensource/index.jsp">Open Source</a></li>
        <li><a href="users/jjsite">Site Journal</a></li>
        <li><a href="tags.jsp">Tag Cloud</a></li>
        <li><a href="update.jsp">Update Journal</a></li>
    </ul>

    <div id="wrapper">
        <form action="search/index.jsp" method="get" id="bsearch">
            <div id="applesearch">
                <span class="sbox_l"></span><span class="sbox"><input name="bquery" type="search"
                                                                      id="srch_fld" placeholder="Search..."
                                                                      autosave="applestyle_srch" results="5"
                                                                      onkeyup="applesearch.onChange('srch_fld','srch_clear')"/></span><span
                    class="sbox_r" id="srch_clear"></span>
            </div>

            <div style="clear:both;"></div>
        </form>
    </div>

    <p><a href="software/index.jsp">Download</a> the client software. </p>

    <%
        Statistics s = new Statistics();
    %>
    <table style="border: thin solid #F2F2F2;">
        <tr style="font-size: 10px; background: #F2F2F2;">
            <td>Users</td>
            <td><%=s.users()%>
            </td>
        </tr>
        <tr style="font-size: 10px;">
            <td>Entries</td>
            <td><%=s.entries()%>
            </td>
        </tr>
        <tr style="font-size: 10px; background: #F2F2F2;">
            <td>Public Entries</td>
            <td><%=String.format("%.2f", s.publicEntries())%>%</td>
        </tr>
        <tr style="font-size: 10px;">
            <td>Friends Entries</td>
            <td><%=String.format("%.2f", s.friendsEntries())%>%</td>
        </tr>
        <tr style="font-size: 10px; background: #F2F2F2;">
            <td>Private Entries</td>
            <td><%=String.format("%.2f", s.privateEntries())%>%</td>
        </tr>
        <tr style="font-size: 10px;">
            <td>Comments</td>
            <td><%=s.comments()%>
            </td>
        </tr>
        <tr style="font-size: 10px; background: #F2F2F2;">
            <td>Blog Styles</td>
            <td><%=s.styles()%>
            </td>
        </tr>
        <tr style="font-size: 10px;">
            <td>Tags</td>
            <td><%=s.tags()%>
            </td>
        </tr>
    </table>

    <p><img src="images/feed.gif" alt="Feed"/> <a href="RecentBlogs">Recent Blog Entries</a></p>
</div>

<div id="blurb">
    <p class="firstone"
       style="background: white url(images/firstone.png) no-repeat;
           text-align: justify; line-height: 1.8em; margin-right: 30px; padding-top: 0">
        Just
        Journal is an online journal service, also
        known as a "blog." You can publish private
        entries for yourself, friends' entries to share with those close to you or public entries you wish to share
        with the Internet.</p>

    <p style="padding-bottom: 10px;"><a href="create.jsp"><img src="images/jj_btn_create_an_account.gif"
                                                               alt="Create an account"/></a></p>

    <table style="border: thin solid #F2F2F2; width: 380px;">
        <caption style="font-size: 12px; font-family: Georgia, Times, serif;">New Members</caption>
        <thead>
            <tr style="font-size: 9px; color: white; background: black;">
                <th>Name</th>
                <th>User</th>
                <th>Since</th>
                <th>Profile</th>
            </tr>
        </thead>
        <%
            Collection users = UserDao.newUsers();
            int i = 0;
            for (java.util.Iterator iterator = users.iterator(); iterator.hasNext();) {
                UserTo o = (UserTo) iterator.next();

                if (i % 2 == 0) { %>
        <tr style="font-size: 10px; background: #F2F2F2;">
            <%      } else { %>
        <tr style="font-size: 10px;">
            <% } %>
            <td><%=o.getName()%>
                <%=o.getLastName()%>
            </td>
            <td><img src="images/userclass_16.png" alt="user icon"/>
                <a href="users/<%=o.getUserName()%>"><%=o.getUserName()%>
                </a></td>
            <td><%=o.getSince()%>
            </td>
            <td><a href="profile.jsp?user=<%=o.getUserName()%>">My Profile</a></td>
        </tr>
        <%
                i++;
            }
        %>
    </table>
</div>

<p style="clear: both;">&#160;</p>

<div id="ticker"></div>
</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
