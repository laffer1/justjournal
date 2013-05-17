<%@ page import="com.justjournal.core.Statistics" %>
<%--
  @version $Id: stats.jsp,v 1.7 2009/02/21 23:11:51 laffer1 Exp $
  @author Lucas Holt
--%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Statistics</title>
    <link rel="stylesheet" type="text/css" href="./layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="./font-normal.css" media="all"/>
    <link rel="home" title="Home" href="./index.jsp"/>
    <link rel="contents" title="Site Map" href="./sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="./support/index.jsp"/>
    <style type="text/css">
        @import "js/dijit/themes/soria/soria.css";
        @import "js/dojo/resources/dojo.css";
    </style>
    <script type="text/javascript" src="js/dojo/dojo.js" djConfig="isDebug:false, parseOnLoad:true"></script>
<%
    Statistics s = new Statistics();
%>
    <script type="text/javascript">
    dojo.require("dojox.charting.Chart2D");

	makeCharts = function(){

		var chart1 = new dojox.charting.Chart2D("simplechart");
		chart1.addPlot("default", {type: "Pie", font: "9pt Helvetica, Arial", labelOffset: -10, precision: 0});
        chart1.addSeries("Series A", [
			{y: <%=s.publicEntries()%>,  color: "#006699"},
			{y: <%=s.privateEntries()%>, color: "#003399"},
			{y: <%=s.friendsEntries()%>, color: "#000033"}
		]);
        chart1.render()
	};

dojo.addOnLoad(makeCharts);
</script>
</head>

<body class="soria">

<jsp:include page="header.inc" flush="false"/>

<div id="content">

    <jsp:include page="inc_login.jsp" flush="false"/>

    <h2>Statistics</h2>

    <p>Below is an overview of usage statistics for this website including the number of users and blog entry types: public, friends, private.</p>

    <h3>Blog Entry Type</h3>
    <p>Key: <span style="color: #006699">public</span>
    <span style="color: #003399">private</span> <span style="color: #000033">friends</span></p>
    
    <div id="simplechart" style="width: 500px; height: 400px;"></div>

    <h3>Details</h3>
    <table style="border: thin solid #F2F2F2; width: 300px;" cellspacing="2" cellpadding="2">
        <tr style="font-size: 14px; background: #F2F2F2;">
            <td>Users</td>
            <td><%=s.users()%>
            </td>
        </tr>
        <tr style="font-size: 14px;">
            <td>Entries</td>
            <td><%=s.entries()%>
            </td>
        </tr>
        <tr style="font-size: 14px; background: #F2F2F2;">
            <td>Comments</td>
            <td><%=s.comments()%>
            </td>
        </tr>
        <tr style="font-size: 14px;">
            <td>Blog Styles</td>
            <td><%=s.styles()%>
            </td>
        </tr>
        <tr style="font-size: 14px; background: #F2F2F2;">
            <td>Tags</td>
            <td><%=s.tags()%>
            </td>
        </tr>
    </table>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>

</html>