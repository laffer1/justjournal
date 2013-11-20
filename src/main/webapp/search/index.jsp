<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
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
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Just Journal: Search</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<%

    String type = request.getParameter("type");
    String query = request.getParameter("query");
    if (query == null)
        query = "";
%>

<div id="content">

<h2>Search</h2>

<jsp:include page="../inc_login.jsp" flush="false"/>

<div style="width: 600px;">
    <form action="index.jsp" method="get" name="search" id="search">
        <fieldset>
            <legend><strong>Search members by</strong><br/></legend>
            <p>
                <select name="type">
                    <option value="username">username</option>
                    <option value="e-mail">e-mail</option>
                    <option value="name">name</option>
                    <option value="aim">aim</option>
                    <option value="icq">icq</option>
                    <option value="yahoo">yahoo</option>
                    <option value="msn">msn</option>
                    <option value="phone">phone number</option>
                </select>

                <input type="text" name="query" id="query" value="<%=query%>"/>
                <input type="submit" name="Submit" value="Find" id="Submit"/>
            </p>
        </fieldset>
    </form>
</div>

<%

    Boolean err = false;
    int a = 0;

    if (query != null && type != null) {
        if (query.length() > 0 && type.length() > 0) {


            Connection conn;
            PreparedStatement pstmt;
            ResultSet rs = null;
            String sqlStmt = "SELECT user.id AS id, user.username AS username, user.name AS name, user_contact.email As email FROM user, user_contact, user_pref WHERE user.id=user_pref.id AND user_pref.owner_view_only = 'N' AND ";

            if (type.equalsIgnoreCase("username"))
                sqlStmt += "username like ?";
            else if (type.equalsIgnoreCase("name"))
                sqlStmt += "name like ?";
            else if (type.equalsIgnoreCase("e-mail"))
                sqlStmt += "user_contact.email like ?";
            else if (type.equalsIgnoreCase("aim"))
                sqlStmt += "user_contact.aim like ?";
            else if (type.equalsIgnoreCase("yahoo"))
                sqlStmt += "user_contact.yahoo like ?";
            else if (type.equalsIgnoreCase("msn"))
                sqlStmt += "user_contact.msn like ?";
            else if (type.equalsIgnoreCase("icq"))
                sqlStmt += "user_contact.icq like ?";
            else if (type.equalsIgnoreCase("phone"))
                sqlStmt += "user_contact.phone like ?";
            else
                err = true;

            sqlStmt += " and user.id=user_contact.id;";

            try {
                if (!err) {
                    conn = SQLHelper.getConn();
                    pstmt = conn.prepareStatement(sqlStmt);
                    pstmt.setString(1, "%" + query + "%");
                    rs = pstmt.executeQuery();

%>
<!-- Member search data -->
<table border="0" cellpadding="1" cellspacing="1" style="padding-bottom: 20px;">
    <caption>Members Search Results</caption>
    <thead>
        <tr style="background: black; color: white; font: Verdana, Arial 10pt;">
            <th>Name</th>
            <th>Username</th>
            <th>E-mail Address</th>
        </tr>
    </thead>
    <tbody style="font: 8pt Verdana, Arial, sans-serif;">

        <%

            while (rs.next()) {
                if (a % 2 == 0) {
                    out.println("<tr style=\"background: white;\">");
                } else {
                    out.println("<tr style=\"background: #F2F2F2;\">");
                }
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td><a href=\"../users/" + rs.getString("username") + "\">" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("</tr>");
                a++;
            }

            rs.close();
            conn.close();

        %>
    </tbody>
</table>
<%
                }

            } catch (Exception e1) {
                //out.print(e1.getMessage());
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // NOTHING TO DO
                    }
                }
            }
        }
    }

    // blog search    start

    String bsort = request.getParameter("bsort");
    String bmax = request.getParameter("bmax");
    String bquery = request.getParameter("bquery");
    if (bquery == null)
        bquery = "";
%>

<div style="width: 600px;">
    <form action="index.jsp" method="get" name="bsearch" id="bsearch">
        <fieldset>
            <legend><strong>Search Blogs for</strong><br/></legend>
            <p>
                <input type="text" name="bquery" id="bquery" value="<%=bquery%>"/>
                <input type="submit" name="BlogSearch" value="Find" id="BlogSearch"/>
            </p>

            <p>Displays first 20 public entries containing the search terms.</p>
        </fieldset>
    </form>
</div>

<%
    CachedRowSet brs = null;

    if (bquery != null) {
        if (bquery.length() > 0) {
            try {
                BaseSearch b = new BaseSearch();
                b.setBaseQuery("SELECT entry.subject AS subject, entry.body AS body, entry.date AS date, entry.id AS id, user.username AS username from entry, user, user_pref WHERE entry.uid = user.id AND entry.uid=user_pref.id AND user_pref.owner_view_only = 'N' AND entry.security=2 AND ");
                b.setFields("subject body");
                if (bmax != null && bmax.length() > 0)
                    try {
                        b.setMaxResults(Integer.parseInt(bmax));
                    } catch (Exception e) {
                        b.setMaxResults(20);
                    }
                else
                    b.setMaxResults(20);
                if (bsort != null && bsort.equalsIgnoreCase("d"))
                    b.setSortDescending("date");
                else
                    b.setSortAscending("date");
                brs = b.search(bquery);
%>

<h3>Blog Search Results</h3>

<%

                while (brs.next()) {

                    // Format the current time.
                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");
                    final SimpleDateFormat formatmytime = new SimpleDateFormat("h:mm a");
                    String curDate;

                    // Parse the previous string back into a Date.
                    final ParsePosition pos = new ParsePosition(0);
                    final java.util.Date currentDate = formatter.parse(brs.getString("date"), pos);

                    curDate = formatmydate.format(currentDate);

                    out.print("<h4 style=\"color: orange;\">");
                    out.print(curDate);
                    out.println("</h4>");


                    out.println("<div class=\"ebody\">");

                    out.print("<h5>");
                    out.print("<img src=\"/images/userclass_16.png\" alt=\"user\"/> <a href=\"../users/" + brs.getString("username") + "\" style=\"font-size: 12px;\">" + brs.getString("username") + "</a>");
                    out.print(" - <span class=\"time\">");
                    out.print(formatmytime.format(currentDate));
                    out.print("</span> - <span class=\"subject\"><a href=\"../users/" + brs.getString("username") + "/entry/" + brs.getString("id") + "\">");
                    out.print(Xml.cleanString(brs.getString("subject")));
                    out.println("</a></span></h5> ");

                    out.println("<div class=\"ebody\">");
                    out.println(brs.getString("body"));
                    out.println("</div>");
                }

                brs.close();

            } catch (Exception e1) {
                if (brs != null) {
                    try {
                        brs.close();
                    } catch (Exception e) {
                        // NOTHING TO DO
                    }
                }
            }
        }
    }
%>


<h3>Google Search of this site</h3>

<form action="http://www.google.com/cse" id="cse-search-box" target="_blank">
  <div>
    <input type="hidden" name="cx" value="partner-pub-1321195614665440:ar3t7aj0zxt" />
    <input type="hidden" name="ie" value="ISO-8859-1" />
    <input type="text" name="q" size="31" />
    <input type="submit" name="sa" value="Search" />
  </div>
</form>
<script type="text/javascript" src="http://www.google.com/coop/cse/brand?form=cse-search-box&amp;lang=en"></script>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>