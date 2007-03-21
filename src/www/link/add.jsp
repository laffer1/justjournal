<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
    Integer userID = (Integer) session.getAttribute("auth.uid");
    int ival = 0;
    if (userID != null) {
        ival = userID.intValue();
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Links</title>
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

    <jsp:include page="../inc_login.jsp" flush="false"/>

    <h3>Add Links</h3>

    <p>Each journal has a links section. You can add your favorite links
        or links that relate to your journal's content.</p>

    <%
        if (ival > 0) {
    %>
    <jsp:include page="../prefs/inc_login.jsp" flush="false"/>

    <div style="width: 500px; padding: 5px; margin: 0;">
        <form name="frmAddLinks" method="post" action="addsubmit.h">
            <fieldset>
                <legend><strong>Link Information</strong><br/></legend>

                <div class="row">
                    <span class="label"><label for="title">Title</label></span>
                    <span class="formw"><input type="text" name="title" id="title" size="20" maxlength="50"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="uri">URI</label></span>
                    <span class="formw"><input type="text" name="uri" id="uri" size="20" maxlength="255"/></span>
                </div>

                <!-- Hack to fix spacing problem.. especially with text boxes -->
                <div class="spacer">&nbsp;</div>
            </fieldset>

            <div class="row"><span class="formw"><input type="submit" name="submit" value="Add Link"/></span></div>

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