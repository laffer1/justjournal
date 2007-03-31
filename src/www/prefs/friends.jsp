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
    PreparedStatement StatementRecordset1 = ConnRecordset1.prepareStatement("SELECT friends.friendid, user.username FROM friends, user WHERE friends.id='" + ival + "' AND friends.friendid=user.id;");
    ResultSet Recordset1 = StatementRecordset1.executeQuery();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Preferences: Friends</title>
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

    <h3>Friends</h3>

    <table style="border: thin solid silver;">
        <tbody>
            <%
                while (Recordset1.next()) {
            %>

            <tr>
                <td><%=Recordset1.getString("username")%></td>
                <td><a href="/RemoveFriend?id=<%=Recordset1.getString( "friendid" )%>">[remove]</a></td>
            </tr>

            <%
                }

            %>
        </tbody>
    </table>

    <h3>Add Friends</h3>

    <div style="width: 700px; padding: 5px; margin: 0;">
        <form name="frmAddFriend" method="post" action="../AddFriend">
            <fieldset>
                <legend><strong>Friends Info</strong><br/></legend>

                <div class="row">
                    <span class="label"><label for="fr1">Friend 1</label></span>
                    <span class="formw"><input type="text" name="fr1" id="fr1" size="15" maxlength="15"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="fr2">Friend 2 (opt)</label></span>
                    <span class="formw"><input type="text" name="fr2" id="fr2" size="15" maxlength="15"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="fr3">Friend 3 (opt)</label></span>
                    <span class="formw"><input type="text" name="fr3" id="fr3" size="15" maxlength="15"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="fr4">Friend 4 (opt)</label></span>
                    <span class="formw"><input type="text" name="fr4" id="fr4" size="15" maxlength="15"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="fr5">Friend 5 (opt)</label></span>
                    <span class="formw"><input type="text" name="fr5" id="fr5" size="15" maxlength="15"/></span>
                </div>
            </fieldset>
            <!-- Hack to fix spacing problem.. especially with text boxes -->
            <div class="spacer">&nbsp;</div>

            <div class="row"><span class="formw"><input type="submit" name="submit" value="submit"/></span></div>

            <!-- Hack to fix spacing problem.. especially with text boxes -->
            <div class="spacer">&nbsp;</div>
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