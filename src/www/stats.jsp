<%@ page import="com.justjournal.core.Statistics" %>
<%--
  @version $Id: stats.jsp,v 1.2 2009/02/21 23:01:09 laffer1 Exp $
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
    addEventToObject(window, 'onload', applesearch.init);

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

    <div id="simplechart" style="width: 200px; height: 150px;"></div>
    <p><span style="color: #006699">public</span>
    <span style="color: #003399">private</span> <span style="color: #000033">friends</span></p>

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
            <td>Comments</td>
            <td><%=s.comments()%>
            </td>
        </tr>
        <tr style="font-size: 10px;">
            <td>Blog Styles</td>
            <td><%=s.styles()%>
            </td>
        </tr>
        <tr style="font-size: 10px; background: #F2F2F2;">
            <td>Tags</td>
            <td><%=s.tags()%>
            </td>
        </tr>
    </table>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>

</html>