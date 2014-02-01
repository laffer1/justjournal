<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.UserImpl" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
    Integer userID = (Integer) session.getAttribute("auth.uid");
    int ival = 0;
    if (userID != null) {
        ival = userID;
    }

    User usr = new UserImpl((String) session.getAttribute("auth.user"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Preferences: Journal Title</title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="${pageContext.request.contextPath}/#/"/>
    <link rel="contents" title="Site Map" href="${pageContext.request.contextPath}/#/sitemap"/>
    <link rel="help" title="Technical Support" href="${pageContext.request.contextPath}/#/support"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">
    <h2>Preferences</h2>


    <%
        if (ival > 0) {
    %>
    <jsp:include page="inc_login.jsp" flush="false"/>

    <h3>Journal Title</h3>

    <div style="width: 500px; padding: 5px; margin: 0;">
        <form name="frmJournalTitle" method="post" action="JournalTitle">
            <fieldset>
                <div class="row">
                    <span class="label"><label for="jtitle">Title</label></span>
                    <span class="formw"><input type="text" name="jtitle" id="jtitle" size="30" maxlength="150"
                                               value="<%=usr.getJournalName() %>"/></span>
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
    <p>You must <a href="${pageContext.request.contextPath}/#/">login</a> before you can edit your preferences.</p>
    <% } %>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>