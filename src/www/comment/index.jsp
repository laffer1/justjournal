<%@ page contentType="text/html; charset=iso-8859-1" language="java"
         import="com.justjournal.User, com.justjournal.db.*" %>
<%@ page import="com.justjournal.utility.StringUtil" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.justjournal.utility.Xml" %>
<%@ page import="com.justjournal.WebError"%>
<%@ page import="java.io.PrintWriter"%>
<%--
Displays user comments on a particular journal entry.

$Id: index.jsp,v 1.4 2007/03/21 01:45:53 laffer1 Exp $
--%>
<%
    int eid;
    boolean nopermission = false;

    try {
        eid = new Integer(request.getParameter("id")).intValue();
    } catch (NumberFormatException ex1) {
        eid = 0;
    }

    if (eid <= 0) {
        //response.reset();
        //response.sendError(500, "bad entry id");
        //response.flushBuffer();
        PrintWriter out2 = response.getWriter();
        WebError.Display("Error", "Invalid entry id.",out2);
        return;
    }

    String aUser = (String) session.getAttribute("auth.user");

    Collection comments = CommentDao.view(eid);

    EntryTo entry = EntryDAO.viewSingle(eid, true);

    User pf;
    try {
        pf = new User(entry.getUserName());
    } catch (Exception e) {
        response.reset();
        response.sendError(500, "Error loading entry owner preferences.");
        response.flushBuffer();
        return;
    }

    /* Error retrieving user preferences. */
    if (pf == null)
        nopermission = true;

    /* Entry owner wants a private journal. */
    if (pf != null && pf.isPrivateJournal())
        nopermission = true;

    /* Entry owner doesn't want comments on this entry. */
    if (entry != null && !entry.getAllowComments())
        nopermission = true;

    /* Entry is private */
    if (entry != null && entry.getSecurityLevel() == 0)
        nopermission = true;

    /* Strange error.  Entry is garbage */
    if (entry.getUserId() == 0)
        nopermission = true;

    /* non-Entry owner is viewing a friends entry */
    if ((aUser == null || !aUser.equalsIgnoreCase(entry.getUserName())) &&
            entry.getSecurityLevel() == 1)
        nopermission = true;
%>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>
        <% if (!(nopermission)) { %>
        <%=entry.getUserName()%>: <%=entry.getSubject()%>
        <% } %>
    </title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
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
                                                         title="<%=entry.getUserName()%>"><%=entry.getUserName()%></a>

    <% DateTime dte = entry.getDate(); %>
    wrote on <%=dte.toPubDate()%>
</p>

<h3><%=Xml.cleanString(entry.getSubject())%></h3>
<%

    if (entry.getAutoFormat()) {
        String tmpBody = entry.getBodyWithLinks();
%>
<p>
    <% if (tmpBody.indexOf("\n") > -1) { %>
    <%=(StringUtil.replace(tmpBody, '\n', "<br />"))%>
    <% } else if (entry.getBody().indexOf("\r") > -1) { %>
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
    <img src="../img/icon_private.gif" alt="private"/> private
    </span><br/>
    <% } else if (entry.getSecurityLevel() == 1) { %>
    <span class="security">security:
    <img src="../img/icon_protected.gif" alt="friends"/>
    friends
    </span><br/>
    <% } %>

    <% if (entry.getLocationId() > 0) { %>
    <span class="location">location:
    <%=entry.getLocationName()%>
    </span><br/>
    <% } %>

    <% if (entry.getMoodName().length() > 0 && entry.getMoodId() != 12) { %>
    <% final EmoticonTo emoto = EmoticonDao.view(1, entry.getMoodId()); %>
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
    CommentTo o;
    final Iterator itr = comments.iterator();

    for (int i = 0, n = comments.size(); i < n; i++) {
        o = (CommentTo) itr.next();

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

    <p><%=Xml.cleanString(o.getBody())%></p>
</div>
<%
        } // end for loop
    } // end nopermission if
%>
</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>
