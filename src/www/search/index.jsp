<%@ page import="com.justjournal.db.SQLHelper" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
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

                <input type="text" name="query" id="query"/>
                <input type="submit" name="Submit" value="Find" id="Submit"/>
            </p>
        </fieldset>
    </form>
</div>

<%
    String query = request.getParameter("query");
    Boolean err = false;
    int a = 0;

    if (query != null && type != null) {
        if (query.length() > 0 && type.length() > 0) {


            Connection conn = null;
            PreparedStatement pstmt;
            ResultSet rs = null;
            String sqlStmt = "SELECT user.id AS id, user.username AS username, user.name AS name, user_contact.email As email FROM user, user_contact WHERE ";

            if (type.equalsIgnoreCase("username"))
                sqlStmt += "username like '%?%'";
            else if (type.equalsIgnoreCase("name"))
                sqlStmt += "name like '%?%'";
            else if (type.equalsIgnoreCase("e-mail"))
                sqlStmt += "user_contact.email like '%?%'";
            else if (type.equalsIgnoreCase("aim"))
                sqlStmt += "user_contact.aim like '%?%'";
            else if (type.equalsIgnoreCase("yahoo"))
                sqlStmt += "user_contact.yahoo like '%?%'";
            else if (type.equalsIgnoreCase("msn"))
                sqlStmt += "user_contact.msn like '%?%'";
            else if (type.equalsIgnoreCase("icq"))
                sqlStmt += "user_contact.icq like '%?%'";
            else if (type.equalsIgnoreCase("phone"))
                sqlStmt += "user_contact.phone like '%?%'";
            else
                err = true;

            sqlStmt += " and user.id=user_contact.id;";

            try {
                if (!err) {
                    conn = SQLHelper.getConn();
                    pstmt = conn.prepareStatement(sqlStmt);
                    pstmt.setString(1, query);
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
%>

<!-- SiteSearch Google -->
<form method="get" action="http://www.google.com/custom" target="_top">
    <table border="0" bgcolor="#ffffff">
        <tr>
            <td nowrap="nowrap" valign="top" align="left" height="32">
                <a href="http://www.google.com/">
                    <img src="http://www.google.com/logos/Logo_25wht.gif" border="0" alt="Google" align="middle"/></a>
            </td>
            <td nowrap="nowrap">
                <input type="hidden" name="domains" value="www.justjournal.com"/>
                <input type="text" name="q" size="31" maxlength="255" value=""/>
                <input type="submit" name="sa" value="Search"/>
        <tr>
            <td>&nbsp;</td>
            <td nowrap="nowrap">
                <table>
                    <tr>
                        <td>
                            <input type="radio" name="sitesearch" value="" checked="checked"/>
                        </td>
                        <td>
                            <input type="radio" name="sitesearch" value="www.justjournal.com"/>
                            <font size="-1" color="black">www.justjournal.com</font>
                        </td>
                    </tr>
                </table>
                <input type="hidden" name="client" value="pub-1321195614665440"/>
                <input type="hidden" name="forid" value="1"/>
                <input type="hidden" name="ie" value="ISO-8859-1"/>
                <input type="hidden" name="oe" value="ISO-8859-1"/>
                <input type="hidden" name="safe" value="active"/>
                <input type="hidden" name="flav" value="0000"/>
                <input type="hidden" name="sig" value="9aOfGVz0Kz6fXBrN"/>
                <input type="hidden" name="cof"
                       value="GALT:#9A2C06;GL:1;DIV:#33FFFF;VLC:D03500;AH:center;BGC:99CCFF;LBGC:CCE5F9;ALC:440066;LC:440066;T:336699;GFNT:223472;GIMP:223472;LH:50;LW:267;L:http://www.justjournal.com/images/jjngtitle2.gif;S:http://www.justjournal.com/;FORID:1"/>
                <input type="hidden" name="hl" value="en"/>
            </td>
        </tr>
    </table>
</form>
<!-- SiteSearch Google -->

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>