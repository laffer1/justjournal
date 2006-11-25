<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.justjournal.db.UserLinkTo" %>
<%@ page import="com.justjournal.db.UserLinkDao" %>
<%
    Integer userID = (Integer) session.getAttribute("auth.uid");
    int ival = 0;
    if (userID != null) {
        ival = userID.intValue();
    }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title>JustJournal.com: Member List</title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">
    <h2>Preferences</h2>

    <h3>Links</h3>

    <p>View and delete your links below. You may also
        <a href="add.jsp" title="Add Links">add</a> a link.
    </p>

    <table border="0" cellpadding="1" cellspacing="1">
        <thead>
            <tr style="background: black; color: white; font: Verdana, Arial 10pt;">
                <th>Link</th>
                <th>Actions</th>
            </tr>
        </thead>

        <tbody style="font: 8pt Verdana, Arial, sans-serif;">
            <%
                Collection<UserLinkTo> links;
                links = UserLinkDao.view(ival);

                UserLinkTo o;
                Iterator itr = links.iterator();

                for (int i = 0, n = links.size(); i < n; i++) {
                    o = (UserLinkTo) itr.next();

                    if (i % 2 == 0) { %>
            <tr style="background: white;">
                <%      } else { %>
            <tr style="background: #F2F2F2;">
                <% } %>
                <tr>
                    <td>
                        <a href="<%=o.getUri()%>"
                           title="<%=o.getTitle()%>"><%=o.getTitle()%></a>
                    </td>
                    <td>
                        <a href="delete.h?<%=o.getId()%>" title="Delete Link">Delete</a>

                    </td>
                </tr>
            </tr>
            <% } %>
        </tbody>
    </table>
</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>