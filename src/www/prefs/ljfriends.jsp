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
    PreparedStatement StatementRecordset1 = ConnRecordset1.prepareStatement("SELECT username, community FROM friends_lj WHERE id='" + ival + "';");
    ResultSet Recordset1 = StatementRecordset1.executeQuery();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Just Journal: Preferences: LJ Friends</title>
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
    <% if (ival > 0) {%>
    <jsp:include page="inc_login.jsp" flush="false"/>
    <h3>LJ Friends</h3>

    <p>An LJ friend is a user on live journal that you would like to watch.
        Their public RSS syndicated journal entries are displayed on your lj friends page.
        If the account is a pay user, you can view the entire journal entry,
        otherwise part of it is displayed.</p>
    <table style="border: thin solid silver;">
        <tbody>
            <%
                while (Recordset1.next()) {
            %>
            <tr>
                <td><%=Recordset1.getString("username")%></td>
                <td><a href="deleteljfriend.h?userName=<%=Recordset1.getString( "username" )%>">[remove]</a></td>
            </tr>
            <% } %>
        </tbody>
    </table>

    <h3>Add LJ Friend</h3>

    <div style="width: 700px; padding: 5px; margin: 0">
        <form name="frmAddFriend" method="post" action="addljfriend.h">
            <fieldset>
                <legend><strong>Friend Info</strong><br/></legend>

                <div class="row">
                    <span class="label"><label for="userName">Friend</label></span>
                    <span class="formw"><input type="text" name="userName" id="userName" size="15"
                                               maxlength="15"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="community">Community</label></span>
                    <span class="formw"><input type="checkbox" name="community" id="community"/></span>
                </div>
            </fieldset>
            <!-- Hack to fix spacing problem.. especially with text boxes -->
            <div class="spacer">&nbsp; </div>

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
