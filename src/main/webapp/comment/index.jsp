<%@ page contentType="text/html; charset=iso-8859-1" language="java"
         import="com.justjournal.UserImpl, com.justjournal.WebError" %>
<%@ page import="com.justjournal.repository.*" %>
<%@ page import="com.justjournal.utility.StringUtil" %>
<%@ page import="com.justjournal.utility.Xml" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.justjournal.model.Comment" %>
<%@ page import="com.justjournal.model.Entry" %>
<%@ page import="com.justjournal.model.EmoticonTo" %>
<%@ page import="com.justjournal.model.DateTime" %>
<%--
Displays user comments on a particular journal entry.

$Id: index.jsp,v 1.5 2008/04/26 17:06:30 laffer1 Exp $
--%>
<%
    int eid;
    boolean nopermission = false;

    try {
        eid = Integer.parseInt(request.getParameter("id"));
    } catch (NumberFormatException ex1) {
        eid = 0;
    }

    if (eid <= 0) {
        PrintWriter out2 = response.getWriter();
        WebError.Display("Error", "Invalid entry id.", out2);
        return;
    }

    String aUser = (String) session.getAttribute("auth.user");

    CommentDao commentDao = new CommentDaoImpl();
    Collection comments = commentDao.list(eid);

    Entry entry = EntryDaoImpl.viewSinglePublic(eid);

    UserImpl pf;
    try {
        pf = new UserImpl(entry.getUserName());
    } catch (Exception e) {
        response.reset();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading entry owner preferences.");
        response.flushBuffer();
        return;
    }

    /* Entry owner wants a private journal. */
    if (pf.isPrivateJournal())
        nopermission = true;

    /* Entry owner doesn't want comments on this entry. */
    if (entry != null && !entry.getAllowComments())
        nopermission = true;

    /* Entry is private */
    if (entry != null && entry.getSecurityLevel() == 0)
        nopermission = true;

    /* Strange error.  Entry is garbage */
    if (entry != null && entry.getUserId() == 0)
        nopermission = true;

    /* non-Entry owner is viewing a friends entry */
    assert entry != null;
    if ((aUser == null || !aUser.equalsIgnoreCase(entry.getUserName())) && entry.getSecurityLevel() == 1)
        nopermission = true;
%>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>
        <% if (!(nopermission)) {
        %>
        <%=entry.getUserName()%>: <%=entry.getSubject()%>
        <% } %>
    </title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="${pageContext.request.contextPath}/"/>
    <link rel="contents" title="Site Map" href="${pageContext.request.contextPath}/#/sitemap"/>
    <link rel="help" title="Technical Support" href="${pageContext.request.contextPath}/#/support"/>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">

    <jsp:include page="../inc_login.jsp" flush="false"/>

    <% if (nopermission) { %>
    <h2>Error</h2>

    <p>The entry is private or does not allow comments.</p>
    <% } else { %>

    <p>
        <img src="../images/userclass_16.png" alt="user"/><a href="../users/<%=entry.getUserName()%>"
                                                             title="<%=entry.getUserName()%>"><%=entry.getUserName()%>
    </a>

        <% DateTime dte = entry.getDateTime(); %>
        wrote on <%=dte.toPubDate()%>
    </p>

    <h3><%=Xml.cleanString(entry.getSubject())%>
    </h3>
    <%

        if (entry.getAutoFormat()) {
            String tmpBody = entry.getBodyWithLinks();
    %>
    <p>
        <% if (tmpBody.contains("\n")) { %>
        <%=(StringUtil.replace(tmpBody, '\n', "<br />"))%>
        <% } else if (entry.getBody().contains("\r")) { %>
        <%=(StringUtil.replace(tmpBody, '\r', "<br />"))%>
        <% } else {
            // we do not have any "new lines" but it might be
            // one long line.
        %>
        <%=tmpBody%>
        <% } %>
    </p>
    <% } else { %>
    <%=entry.getBody()%>
    <% } %>

    <p>

        <% if (entry.getSecurityLevel() == 0) { %>
    <span class="security">security:
    <img src="../images/icon_private.gif" alt="private"/> private
    </span><br/>
        <% } else if (entry.getSecurityLevel() == 1) { %>
    <span class="security">security:
    <img src="../images/icon_protected.gif" alt="friends"/>
    friends
    </span><br/>
        <% } %>

        <% if (entry.getLocationId() > 0) { %>
    <span class="location">location:
    <%=entry.getLocationName()%>
    </span><br/>
        <% } %>

        <% if (entry.getMoodName().length() > 0 && entry.getMoodId() != 12) { %>
        <% final EmoticonTo emoto = EmoticonDao.get(1, entry.getMoodId()); %>
    <span class="mood">mood: <img src="../images/emoticons/1/<%=emoto.getFileName()%>"
                                  width="<%=emoto.getWidth()%>"
                                  height="<%=emoto.getHeight()%>"
                                  alt="<%=entry.getMoodName()%>"/>
    <%=entry.getMoodName()%>
    </span><br/>
        <% } %>

        <% if (entry.getMusic().length() > 0) { %>
    <span class="music">music:
    <%=Xml.cleanString(entry.getMusic())%>
    </span><br/>
        <% } %>

    </p>

    <div class="commentcount">
        <%=entry.getCommentCount()%> comments
    </div>

    <div class="rightflt">
        <a href="add.jsp?id=<%=eid%>" title="Add Comment">Add Comment</a>
    </div>

    <%
        Comment o;
        final Iterator itr = comments.iterator();

        for (int i = 0, n = comments.size(); i < n; i++) {
            o = (Comment) itr.next();

    %>
    <div class="comment">

        <div class="chead">
            <h3><span class="subject"><%=Xml.cleanString(o.getSubject())%></span></h3>
            <img src="../images/userclass_16.png" alt="user"/>
            <a href="../users/<%=o.getUserName()%>" title="<%=o.getUserName()%>">
                <%=o.getUserName()%>
            </a>

            <br/><span class="time"><%=o.getDate().toPubDate()%></span>

            <%
                if (aUser != null && aUser.equalsIgnoreCase(o.getUserName())) {
            %>
            <br/><span class="actions">
				    <a href="edit.h?commentId=<%=o.getId()%>" title="Edit Comment">
                        <img src="../images/compose-message.png" alt="Edit Comment" width="24" height="24"/>
                    </a>

                    <a href="delete.h?commentId=<%=o.getId()%>" title="Delete Comment">
                        <img src="../images/stock_calc-cancel.png" alt="Delete Comment" width="24" height="24"/>
                    </a>
                    </span>
            <% } %>
        </div>

        <p><%=Xml.cleanString(o.getBody())%>
        </p>
    </div>
    <%
            } // end for loop
        } // end nopermission if
    %>
</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>
