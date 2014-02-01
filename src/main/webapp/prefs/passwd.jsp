<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
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
        Integer userID = (Integer) session.getAttribute("auth.uid");
        int ival = 0;
        if (userID != null) {
            ival = userID;
        }

        if (ival > 0) {
    %>
    <jsp:include page="inc_login.jsp" flush="false"/>

    <h3>Change Password</h3>

    <div style="width: 600px; padding: 5px; margin: 0">
        <form name="frmPasswd" method="post" action="changepass.h">
            <fieldset>
                <legend><strong>Account Password</strong><br/></legend>

                <div class="row">
                    <span class="label"><label for="passCurrent">Current Password</label></span>
                    <span class="formw"><input type="text" name="passCurrent" id="passCurrent" size="15"
                                               maxlength="15"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="passNew">New Password</label></span>
                    <span class="formw"><input type="text" name="passNew" id="passNew" size="15" maxlength="15"/></span>
                </div>

            </fieldset>

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