<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" %>
<%@ include file="../Connections/jjdb.jsp" %>
<%
    Integer userID = (Integer) session.getAttribute("auth.uid");
    int ival = 0;
    if (userID != null) {
        ival = userID.intValue();
    }
%>
<%
    Driver DriverRecordset1 = (Driver) Class.forName(MM_jjdb_DRIVER).newInstance();
    Connection ConnRecordset1 = DriverManager.getConnection(MM_jjdb_STRING, MM_jjdb_USERNAME, MM_jjdb_PASSWORD);
    PreparedStatement StatementRecordset1 = ConnRecordset1.prepareStatement("SELECT doc FROM user_style WHERE id='" + ival + "';");
    ResultSet Recordset1 = StatementRecordset1.executeQuery();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Preferences</title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">
    <h2>Preferences</h2>


    <%
        if (ival > 0) {
    %>

    <jsp:include page="inc_login.jsp" flush="false"/>
    <h3>Override Style Template</h3>

    <div style="width: 700px; padding: 5px; margin: 0">
        <form name="frmStyleOverride" method="post" action="/UpdateStyleSheetOverride">
            <fieldset>
                <legend><strong>Cascading Style Sheet (CSS)</strong><br/></legend>

                <div class="row">
                    <p>Input cascading style sheet markup in the box below. This markup will alter
                        the base template for your journal. Please do not include any HTML tags
                        as they will be filtered out in the future. Here is a brief example:</p>

                    <pre>
                        p {
                        color: navy;
                        background: silver;
                        font: 10pt Verdana, Helvetica, sans-serif;
                        }

                        h1 {
                        color: maroon;
                        }
                    </pre>
                </div>

                <div class="row">
                    <textarea id="css" name="css" cols="30" rows="8"><% if (Recordset1.next()) { %>
                        <%=Recordset1.getString("doc")%>
                        <% } %></textarea>
                </div>
                <!-- Hack to fix spacing problem.. especially with text boxes -->
                <div class="spacer">&nbsp;</div>

            </fieldset>

            <div class="row"><input type="submit" name="submit" value="submit"/></div>

        </form>
    </div>

    <% } else { %>
    <p>You must <a href="../login.jsp">login</a> before you can edit your preferences.</p>
    <% } %>


</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>
<%
    Recordset1.close();
    StatementRecordset1.close();
    ConnRecordset1.close();
%>