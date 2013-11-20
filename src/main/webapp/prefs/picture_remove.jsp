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
    PreparedStatement StatementRecordset1 = ConnRecordset1.prepareStatement("SELECT id, title FROM user_images WHERE owner='" + ival + "' ORDER by title, id;");
    ResultSet Recordset1 = StatementRecordset1.executeQuery();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Preferences: Remove Picture</title>
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

    <h3>Remove Picture</h3>

    <table style="border: thin solid silver;">
        <tbody>
            <%
                while (Recordset1.next()) {
            %>

            <tr>
                <td><%=Recordset1.getString("title")%></td>
                <td><a href="DeletePicture?id=<%=Recordset1.getString( "id" )%>">[remove]</a></td>
            </tr>

            <%
                }

            %>
        </tbody>
    </table>

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