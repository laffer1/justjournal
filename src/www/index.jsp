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
<link rel="alternate" media="handheld" href="mob/index.jsp" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
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
    background-color: #006699;
    color: white;
    border: thin solid silver;
    border-radius: 10px;   /* css3 */
    -moz-border-radius: 10px;
    -webkit-border-radius: 10px;
    width: 600px;
    margin-top: 5px;
    margin-bottom: 5px;
    margin: 0;
    padding: 0;
}

#ticker table {
    border: 0;
    margin: 0;
    padding: 0;
}

#ticker table td {
    font-size: 12px;
}

#ticker table td#tic-title {
    font-size: 12px;
    font-weight: bold;
    padding: 4px 0;
    text-align: center;
    vertical-align: top;
    width: 184px;
}

#ticker table td#tic-item {
    padding: 4px 10px 4px 16px;
    width: 534px;
    text-align: left;
}

#ticker table td#tic-item a {
    opacity: 0.999;
}

#ticker table a {
    color: white;
    text-decoration: none;
}

#ticker table a:hover {
    color: silver;
    text-decoration: underline;
}

.firstone:first-letter {
    color: white;
    background: #006699;
    margin: 3px;
    padding: 2px;
}

.firstone {
    color: #006699;
    background: #F2F2F2;
    padding: 10px;
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

a.ovalbutton{
background: transparent url('images/oval-blue-left.gif') no-repeat top left;
display: block;
float: left;
font: normal 13px Tahoma; /* Change 13px as desired */
line-height: 16px; /* This value + 4px + 4px (top and bottom padding of SPAN) must equal height of button background (default is 24px) */
height: 24px; /* Height of button background height */
padding-left: 11px; /* Width of left menu image */
text-decoration: none;
}

a:link.ovalbutton, a:visited.ovalbutton, a:active.ovalbutton{
color: #494949; /*button text color*/
}

a.ovalbutton span{
background: transparent url('images/oval-blue-right.gif') no-repeat top right;
display: block;
padding: 4px 11px 4px 0; /*Set 11px below to match value of 'padding-left' value above*/
}

a.ovalbutton:hover{ /* Hover state CSS */
background-position: bottom left;
}

a.ovalbutton:hover span{ /* Hover state CSS */
background-position: bottom right;
color: black;
}

.buttonwrapper{ /* Container you can use to surround a CSS button to clear float */
overflow: hidden; /*See: http://www.quirksmode.org/css/clearing.html */
width: 100%;
}
</style>
<link rel="stylesheet" type="text/css" href="default.css" id="default"/>
<!-- dummy stylesheet - href to be swapped -->
<link rel="stylesheet" type="text/css" href="dummy.css" id="dummy_css"/>
<script type="text/javascript" src="js/applesearch.js">/* ie7 sucks*/</script>
   <style type="text/css" media="all">
        <!--

        div.row {
            clear: both;
            padding-top: 5px;
        }

        div.row span.label {
            float: left;
            width: 120px;
            text-align: right;
        }

        div.row span.formw {
            float: right;
            width: 205px;
            text-align: left;
        }

        div.spacer {
            clear: both;
        }

        -->
    </style>
    <script language="JavaScript" type="text/javascript" src="js/sha1.js"></script>
    <script language="JavaScript" type="text/javascript">
        function sendForm(formid, checkuser)
        {

            if (formid == null)
                formid = 'alogin';

            // 'checkuser' is the element id name of the username textfield.
            // only use it if you care to verify a username exists before hashing.

            if (! document.getElementById)
                return true;

            var loginform = document.getElementById(formid);
            if (! loginform) return true;

            // Avoid accessing the password field if there is no username.
            // This works around Opera < 7 complaints when commenting.
            if (checkuser) {
                var username = null;
                for (var i = 0; username == null && i < loginform.elements.length; i++) {
                    if (loginform.elements[i].id == checkuser) username = loginform.elements[i];
                }
                if (username != null && username.value == "") return true;
            }

            if (! loginform.password)
                return true;

            var pass = loginform.password.value;

            loginform.password_hash.value = hex_sha1(pass);
            loginform.password.value = "";  // dont send clear-text password!
            return true;
        }

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

    <p>View <a href="stats.jsp">site statistics</a>.</p>

    <p><img src="images/feed.gif" alt="Feed"/> <a href="RecentBlogs">Recent Blog Entries</a></p>
</div>

<div id="blurb">
    <p class="firstone"
       style=" text-align: justify; line-height: 1.8em; margin-right: 30px; padding-top: 0">
        Just
        Journal is an online journal service, also
        known as a "blog." You can publish private
        entries for yourself, friends' entries to share with those close to you or public entries you wish to share
        with the Internet.</p>

    <div style="width: 360px; padding: 5px; margin: 0;">
        <form method="post" name="alogin" action="./loginAccount" id="blogin">
            <input type="hidden" name="password_hash" id="ipassword_hash" value=""/>

            <fieldset>
                <legend><strong>Just Journal Account</strong><br/>
                </legend>


                <div class="row">
                    <span class="label">Username</span>
      <span class="formw">
     <input type="text" name="username" id="iusername" size="18" maxlength="15"
            style="background: url(images/userclass_16.png) no-repeat; background-color: #fff; background-position: 0px 1px; padding-left: 18px; color: black; font-weight: bold;"/></span>
                </div>

                <div class="row">
                    <span class="label">Password</span>
      <span class="formw"><input type="password" name="password" id="ipassword" size="18"
                                 style="background: white; color: black; font-weight: bold;"/>
      </span>
                </div>

                <!-- Hack to fix spacing problem.. especially with text boxes -->
                <div class="spacer"> &nbsp; </div>
            </fieldset>

            <div class="row">
                <input type="submit" name="submitlogin" value="Login" onclick="return sendForm()"/>
            </div>

            <!-- Hack to fix spacing problem.. especially with text boxes -->
            <div class="spacer"> &nbsp; </div>
        </form>
       </div>

</div>

<p style="clear: both;">&#160;</p>

<div id="ticker"></div>

    <p style="clear: both;">&#160;</p>

    <table style="border: thin solid #F2F2F2; width: 600px;">
        <caption style="font-size: 12px; font-family: Georgia, Times, serif;">New Members</caption>
        <thead>
            <tr style="font-size: 9px; color: white; background: #006699;">
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
                <% if (o.getLastName() != null) { %>
                <%=o.getLastName()%>
               <% } %>
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

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>
